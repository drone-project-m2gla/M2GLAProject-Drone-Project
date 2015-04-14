package fr.m2gla.istic.projet.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.constantes.Constant;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InterventionDetailFragment extends Fragment {
    private static final String TAG = "InterventionDetailFragment";
    private String idIntervention = "";
    private String[] titles;
    private int[] images;


    // Declaring the Integer Array with resourse Id's of Images for the Spinners

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intervention_detail, container, false);


        titles = new String[]{Constant.VALUE_VEHICULE_EPA, Constant.VALUE_VEHICULE_FPT,
                Constant.VALUE_VEHICULE_VSR, Constant.VALUE_VEHICULE_VLCG, Constant.VALUE_VEHICULE_VSAV};

        images = new int[]{Constant.DRAWABLE_IMG_VEHICULE_EPA, Constant.DRAWABLE_IMG_VEHICULE_FPT,
                Constant.DRAWABLE_IMG_VEHICULE_VSR, Constant.DRAWABLE_IMG_VEHICULE_VLCG, Constant.DRAWABLE_IMG_VEHICULE_VSAV};

        List<Drawable> drawables = new ArrayList<Drawable>();
        for (int imageId : images) {
            if (imageId!=0) {
                drawables.add(getResources().getDrawable(imageId));
            } else {
                drawables.add(getResources().getDrawable(R.drawable.bubble_shadow));
            }
        }


        ListView listMoyen = (ListView) view.findViewById(R.id.intervention_detail_list);
        Drawable[] imagesArray = drawables.toArray(new Drawable[drawables.size()]);

        Log.i(TAG, TAG+"\nTaille   "+imagesArray.length);

        listMoyen.setAdapter(new ItemsAdapter(getActivity(), R.layout.custom_detail_moyen, titles,  imagesArray));


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,, listMoyen);
//        setListAdapter(adapter);
//


        return view;

    }



    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;
        Toast.makeText(getActivity(),"bonjour  " + idIntervention,Toast.LENGTH_LONG).show();


        if(!idIntervention.equals("")){
            Map<String,String> map = new HashMap<>();
            map.put("id",idIntervention);
            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION,map, Intervention.class,new Command() {
                        @Override
                        public void execute(Object response) {
                           Intervention intervention = (Intervention)response;
                            Toast.makeText(getActivity(),"  test intervetion return " + intervention.getId(),Toast.LENGTH_LONG).show();
                        }
                    },new Command() {
                        @Override
                        public void execute(Object response) {

                        }
                    });
        }
        // idTextView.setText(idIntervention);

    }
}
