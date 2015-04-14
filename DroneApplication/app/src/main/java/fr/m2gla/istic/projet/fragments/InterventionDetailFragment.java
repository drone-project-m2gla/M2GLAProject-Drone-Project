package fr.m2gla.istic.projet.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.constantes.Constant;

import com.google.android.gms.plus.PlusOneButton;


public class InterventionDetailFragment extends Fragment {
    String[] titles = new String[]{"Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread"};
    String[] Version = new String[]{"1.5", "1.6", "2.0-2.1", "2.2", "2.3", "3.0-3.2", "4.0", "4.1-4.3", "4.4"};

    // Declaring the String Array with the Text Data for the Spinners
    String[] Languages = {"Select a Language", "C# Language", "HTML Language",
            "XML Language", "PHP Language"};

    // Declaring the Integer Array with resourse Id's of Images for the Spinners


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intervention_detail, container, false);

        return view;

    }

}
