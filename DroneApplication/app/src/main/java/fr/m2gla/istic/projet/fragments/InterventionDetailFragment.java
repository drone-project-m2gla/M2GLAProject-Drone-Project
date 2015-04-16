package fr.m2gla.istic.projet.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.constantes.Constant;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;


public class InterventionDetailFragment extends Fragment {
    private static final String TAG = "Inter";
    private Intervention intervention;

    public String getIdIntervention() {
        return idIntervention;
    }

    private String idIntervention = "";
    private String[] titles;
    private String[] images;
    private View view;


    // Declaring the Integer Array with resourse Id's of Images for the Spinners

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_intervention_detail, container, false);

        return view;
    }

    /**
     * Set intervention id from another fragment or activity
     *
     * @param idIntervention
     */

    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;

        if (!idIntervention.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.put("id", idIntervention);
            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION, map, Intervention.class, getCallbackSuccess(), getCallbackError());
        }
    }

    /**
     * Command error
     *
     * @return
     */
    private Command getCallbackError() {
        return new Command() {
            @Override
            public void execute(Object response) {
                Toast.makeText(getActivity(), "ERROR\nRequête HTTP en échec", Toast.LENGTH_LONG);
            }
        };
    }

    /**
     * Command success
     *
     * @return
     */
    private Command getCallbackSuccess() {
        return new Command() {
            @Override
            public void execute(Object response) {
                intervention = (Intervention) response;
                Toast.makeText(getActivity(), "  test intervetion return " + intervention.getId(), Toast.LENGTH_LONG).show();
                int i = 0;
                List<Mean> meanList = intervention.getMeansXtra();
                // Initialisation des titres et images.
                initImagesTitles(intervention, i, meanList);
                List<Drawable> drawables = new ArrayList<Drawable>();

                if (images.length > 0) {
                    for (String imageId : images) {
                        Log.wtf(TAG,"iamge id " +(imageId !=null));
                        if (!imageId.equals("")) {
                            SVG svg = null;
                            try {
                                svg = SVG.getFromResource(getActivity(), getResources().getIdentifier(imageId, "raw", getActivity().getPackageName()));
                            } catch (SVGParseException e) {
                                e.printStackTrace();
                            }
                            drawables.add(new PictureDrawable(svg.renderToPicture()));

                        } else {
                            drawables.add(getResources().getDrawable(R.drawable.bubble_shadow));
                        }
                    }

                }

                ListView moyensListView = (ListView) view.findViewById(R.id.intervention_detail_list);
                Drawable[] imagesArray = drawables.toArray(new Drawable[drawables.size()]);
                Context activity = InterventionDetailFragment.this.getActivity();
                ListAdapter adapter = new ItemsAdapter(activity, R.layout.custom_detail_moyen, titles, imagesArray);
                Log.i(TAG, "adapter  " + (adapter == null) + " \nImage array  " + imagesArray.length + " \ntitles " + (titles == null) + "\nactivity  " + (activity == null));

                Log.i(TAG, "List\t" + (moyensListView == null));

                moyensListView.setAdapter(adapter);
            }
        };
    }
    /**
     * Formattage des moyens extra pour l'adapter
     *
     * @param intervention
     * @param position
     * @param listXtra
     */
    /**
     * Formattage des moyens extra pour l'adapter
     *
     * @param intervention
     * @param position
     * @param listXtra
     */
    private void initImagesTitles(Intervention intervention, int position, List<Mean> listXtra) {
        int listXtraNotDeclinedSize =0;
        for (Mean m : listXtra) {
            if (!m.getIsDeclined()){
                listXtraNotDeclinedSize++;
            }
        }
        titles = new String[listXtraNotDeclinedSize];
        images = new String[listXtraNotDeclinedSize];

        TextView titleFragement = (TextView) this.view.findViewById(R.id.titre);
        TextView addresseIntervention = (TextView) this.view.findViewById(R.id.addre);
        TextView titleNoMoyen = (TextView) this.view.findViewById(R.id.moy);
        TextView codeIntervention = (TextView) this.view.findViewById(R.id.code);
        addresseIntervention.setText("adresse : \n" + intervention.getAddress().toString() + " " + intervention.getPostcode().toString() + " " + intervention.getCity());
        codeIntervention.setText("Code : " + intervention.getDisasterCode().toString());


        if (listXtraNotDeclinedSize > 0) {
            addresseIntervention.setVisibility(View.VISIBLE);
            titleFragement.setVisibility(View.VISIBLE);
            titleNoMoyen.setVisibility(View.GONE);
            codeIntervention.setVisibility(View.VISIBLE);
            for (Mean m : listXtra) {

                if (!m.getIsDeclined()) {

                    titles[position] = m.getVehicle().toString();

                    images[position] = Constant.getImage(m.getVehicle().toString());

                    position++;
                }

            }

        } else {
            titleNoMoyen.setVisibility(View.VISIBLE);
            titleFragement.setVisibility(View.GONE);
            addresseIntervention.setVisibility(View.VISIBLE);
            codeIntervention.setVisibility(View.VISIBLE);
//            Toast.makeText(getActivity(), "intervention " + intervention.getId() + "\n n'a pas de demandes de moyens extra ", Toast.LENGTH_LONG).show();
        }
    }

    public Mean getMeanXtra(int position) {
        return intervention.getMeansXtra().get(position);
    }
}
