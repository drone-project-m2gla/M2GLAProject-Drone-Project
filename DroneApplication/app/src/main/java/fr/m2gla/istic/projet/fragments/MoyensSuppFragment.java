package fr.m2gla.istic.projet.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Map;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.constantes.Constant;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.Vehicle;
import fr.m2gla.istic.projet.service.RestService;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;


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
    int[] images;
    private String meanID = "-1113935408";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.moyens_supp_fragment, container, false);
        text = (TextView) view.findViewById(R.id.AndroidOs);
        vers = (TextView) view.findViewById(R.id.Version);

        moyensSpinner = (Spinner) view
                .findViewById(R.id.moyensSpinner);

        // Setting a Custom Adapter to the Spinner
//        images = new String[]{"", Constant.SVG_COLONNE_INCENDIE_ACTIVE, Constant.SVG_MOYEN_INTERVENTION_AERIEN,
//                Constant.SVG_GROUPE_INCENDIE_ACTIF, Constant.SVG_SECOUR_A_PERSONNE_PREVU};
        titles = new String[]{"", Constant.VALUE_VEHICULE_EPA, Constant.VALUE_VEHICULE_FPT,
                Constant.VALUE_VEHICULE_VSR, Constant.VALUE_VEHICULE_VLCG, Constant.VALUE_VEHICULE_VSAV};


        images = new int[]{0, Constant.DRAWABLE_IMG_VEHICULE_EPA, Constant.DRAWABLE_IMG_VEHICULE_FPT,
                Constant.DRAWABLE_IMG_VEHICULE_VSR, Constant.DRAWABLE_IMG_VEHICULE_VLCG, Constant.DRAWABLE_IMG_VEHICULE_VSAV};


        moyensSpinner.setAdapter(new ItemsAdapter(getActivity(), R.layout.custom, titles, images));

        ImageButton addButton = (ImageButton) view.findViewById(R.id.add_moyen);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = moyensSpinner.getSelectedItemPosition();
                SpinnerAdapter adapter = moyensSpinner.getAdapter();
                String moyen = String.valueOf(position);
                Toast.makeText(getActivity(), "Bonjour\n" + moyen, Toast.LENGTH_SHORT).show();

                sendRequestMeanAsync();
            }
        });

        return view;

    }

    /**
     * Méthode d'envoi d'une demande d'un moyen supplémentaire.
     *
     * @return
     */
    private String sendRequestMeanAsync() {
        RestService requestSnd = RestServiceImpl.getInstance();
        final Mean mean = new Mean();
        mean.setVehicle(Vehicle.VLCG);
        Map<String, String> map = new HashMap<>();
        map.put("id", meanID);

        requestSnd.post(RestAPI.POST_SEND_MEAN_REQUEST, map, mean, Mean.class, new Command() {
            /**
             * Success connection
             * @param response Response object type User
             */
            @Override
            public void execute(Object response) {

                // Demander la prise en compte de la validation de l'identification
                Toast.makeText(getActivity(), "Moyen suppl.", Toast.LENGTH_SHORT).show();
                Mean moyen = (Mean) response;
                Log.i("sow", "On  Post execute\t" + moyen.getId() + "\tVehicule\t" + moyen.getVehicle());
            }
        }, new Command() {
            /**
             * Error connection
             * @param response Response error type HttpClientErrorException
             */
            @Override
            public void execute(Object response) {
                // Echec d'identification. Retours à l'activity principale
                Toast.makeText(getActivity(), "Echec demande de moyen.", Toast.LENGTH_SHORT).show();
                //return;
            }
        });

        return "Cool";
    }

    public void change(String txt, String txt1) {
        text.setText(txt);
        vers.setText(txt1);
    }
}