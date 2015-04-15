package fr.m2gla.istic.projet.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String TAG = "InterventionDetailFragment";
    private String idIntervention = "";
    private String[] titles;
    private int[] images;
    private View view;


    // Declaring the Integer Array with resourse Id's of Images for the Spinners

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_intervention_detail, container, false);

        return view;
    }


    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;

        if (!idIntervention.equals("")) {
            Map<String, String> map = new HashMap<>();
            final ArrayList<String> maListe = new ArrayList<String>();
            map.put("id", idIntervention);
            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION, map, Intervention.class, new Command() {
                        @Override
                        public void execute(Object response) {
                            Intervention intervention = (Intervention) response;
                            Toast.makeText(getActivity(), "  test intervetion return " + intervention.getId(), Toast.LENGTH_LONG).show();
                            int i = 0;
                            List<Mean> listXtra = intervention.getMeansXtra();
                            // Initialisation des titres et images.
                            initImagesTitles(intervention, i, listXtra);
                            List<Drawable> drawables = new ArrayList<Drawable>();

                            for (int imageId : images) {
                                if (imageId != 0) {
                                    drawables.add(getResources().getDrawable(imageId));
                                } else {
                                    drawables.add(getResources().getDrawable(R.drawable.bubble_shadow));
                                }
                            }

                            ListView listMoyen = (ListView) view.findViewById(R.id.intervention_detail_list);
                            Drawable[] imagesArray = drawables.toArray(new Drawable[drawables.size()]);

                            listMoyen.setAdapter(new ItemsAdapter(getActivity(), R.layout.custom_detail_moyen, titles, imagesArray));
                        }
                    }, new Command() {
                        @Override
                        public void execute(Object response) {
                            Toast.makeText(getActivity(), "ERROR\nRequête HTTP en échec", Toast.LENGTH_LONG);
                        }
                    });
        }
        // idTextView.setText(idIntervention);

    }

    private void initImagesTitles(Intervention intervention, int position, List<Mean> listXtra) {
        titles = new String[listXtra.size()];
        images = new int[listXtra.size()];
        TextView titleFragement = (TextView) this.view.findViewById(R.id.titre);
        TextView addresseIntervention = (TextView) this.view.findViewById(R.id.addre);
        TextView titleNoMoyen = (TextView) this.view.findViewById(R.id.moy);
        TextView codeIntervention = (TextView) this.view.findViewById(R.id.code);
        addresseIntervention.setText("adresse : \n"+ intervention.getAddress().toString() +" "+ intervention.getPostcode().toString()+" "+ intervention.getCity());
        codeIntervention.setText("Code : "+ intervention.getDisasterCode().toString());
        Button valid = (Button)this.view.findViewById(R.id.valid);
        Button annuler = (Button)this.view.findViewById(R.id.annuler);
        if (listXtra.size() > 0) {


            addresseIntervention.setVisibility(View.VISIBLE);
            titleFragement.setVisibility(View.VISIBLE);
            titleNoMoyen.setVisibility(View.GONE);
            codeIntervention.setVisibility(View.VISIBLE);
            for (Mean m : listXtra) {

                titles[position] = m.getVehicle().toString();

                images[position] = Constant.getImage(m.getVehicle().toString());

                position++;
            }

        } else {

            titleNoMoyen.setVisibility(View.VISIBLE);
            titleFragement.setVisibility(View.GONE);
            addresseIntervention.setVisibility(View.VISIBLE);
            codeIntervention.setVisibility(View.VISIBLE);
        }
    }

}
