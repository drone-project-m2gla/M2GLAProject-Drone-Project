package fr.m2gla.istic.projet.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.constantes.Constant;


public class MoyensSuppFragment extends Fragment {
    TextView text, vers;

    public static ListView maListViewPerso;
    public static ArrayList<HashMap<String, String>> listItem;
    public static SimpleAdapter mListAdapter;
    private Spinner moyensSpinner;
    private static ArrayAdapter<CharSequence> adapter;

    // Declaring the String Array with the Text Data for the Spinners
    String[] titles;
    // Declaring the Integer Array with resourse Id's of Images for the Spinners
    String[] images;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.moyens_supp_fragment, container, false);
        text = (TextView) view.findViewById(R.id.AndroidOs);
        vers = (TextView) view.findViewById(R.id.Version);

        moyensSpinner = (Spinner) view
                .findViewById(R.id.moyensSpinner);

        // Setting a Custom Adapter to the Spinner
        images = new String[]{"", Constant.SVG_COLONNE_INCENDIE_ACTIVE, Constant.SVG_MOYEN_INTERVENTION_AERIEN,
                Constant.SVG_GROUPE_INCENDIE_ACTIF, Constant.SVG_SECOUR_A_PERSONNE_PREVU};
        titles = new String[]{"", Constant.VALUE_COLONNE_INCENDIE_ACTIVE, Constant.VALUE_MOYEN_INTERVENTION_AERIEN,
                Constant.VALUE_GROUPE_INCENDIE_ACTIF, Constant.VALUE_SECOUR_A_PERSONNE_PREVU};


        moyensSpinner.setAdapter(new ItemsAdapter(getActivity(), R.layout.custom, titles, images));

        ImageButton addButton = (ImageButton) view.findViewById(R.id.add_moyen);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = moyensSpinner.getSelectedItemPosition();
                SpinnerAdapter adapter = moyensSpinner.getAdapter();
                String moyen = String.valueOf(position);
                Toast.makeText(getActivity(), "Bonjour\n" + moyen, Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }

    public void change(String txt, String txt1) {
        text.setText(txt);
        vers.setText(txt1);
    }
}