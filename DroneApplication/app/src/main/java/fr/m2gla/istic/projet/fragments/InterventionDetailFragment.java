package fr.m2gla.istic.projet.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.constantes.Constant;
import fr.m2gla.istic.projet.model.Intervention;

import com.google.android.gms.plus.PlusOneButton;

import java.util.ArrayList;



public class InterventionDetailFragment extends Fragment {
    private String idIntervention;
    private String[] titles;
    private int[] images;


    // Declaring the Integer Array with resourse Id's of Images for the Spinners

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intervention_detail, container, false);


        titles = new String[]{"", Constant.VALUE_VEHICULE_EPA, Constant.VALUE_VEHICULE_FPT,
                Constant.VALUE_VEHICULE_VSR, Constant.VALUE_VEHICULE_VLCG, Constant.VALUE_VEHICULE_VSAV};


        images = new int[]{0, Constant.DRAWABLE_IMG_VEHICULE_EPA, Constant.DRAWABLE_IMG_VEHICULE_FPT,
                Constant.DRAWABLE_IMG_VEHICULE_VSR, Constant.DRAWABLE_IMG_VEHICULE_VLCG, Constant.DRAWABLE_IMG_VEHICULE_VSAV};


        ListView listMoyen = (ListView) view.findViewById(R.id.interventionDetailList);
        listMoyen.setAdapter(new ItemsAdapter(getActivity(), R.layout.custom, titles, images));

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,, listMoyen);
//        setListAdapter(adapter);
//


        return view;

    }




    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;
        Toast.makeText(getActivity(),"bonjour  " + idIntervention,Toast.LENGTH_LONG).show();
        TextView idTextView = (TextView) getView().findViewById(R.id.id_detail_intervention);
        idTextView.setText(idIntervention);

    }
}
