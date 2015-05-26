package fr.m2gla.istic.projet.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.ItemsAdapter;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.context.SVGAdapter;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.model.Vehicle;
import fr.m2gla.istic.projet.service.RestService;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

import static fr.m2gla.istic.projet.model.Symbol.SymbolType.valueOf;


public class MoyensSuppFragment extends Fragment {
    public static final String TAG = "Moyen suppl.";
    TextView text, vers;

    public static ListView maListViewPerso;
    public static ArrayList<HashMap<String, String>> listItem;
    public static SimpleAdapter mListAdapter;
    private Spinner moyensSpinner;
    private static ArrayAdapter<CharSequence> adapter;

    // Declaring the String Array with the Text Data for the Spinners
    String[] titles;
    // Declaring the Integer Array with resource Id's of Images for the Spinners
    int[] images;
    private String interventionID = "";
    private Symbol[] means;

    /**
     * Methode principale du fragment de gestion des demandes de moyens supplémentaires
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.moyens_supp_fragment, container, false);

        moyensSpinner = (Spinner) view
                .findViewById(R.id.moyensSpinner);
        List<Drawable> drawables = new ArrayList<Drawable>();

        titles = new String[Vehicle.values().length];
        int i = 0;
        for (Vehicle v : Vehicle.values()) {
            String vehicule = v.toString();
            String vehiculeName = Symbol.getImage(vehicule);

            Symbol symbol = new Symbol(valueOf(vehiculeName), vehicule, Symbol.getCityTrigram(), Symbol.getMeanColor(v));

            String title = vehicule;

            titles[i++] = title;
            drawables.add(SVGAdapter.convertSymbolToDrawable(getActivity().getApplicationContext(), symbol));
        }

        moyensSpinner.setAdapter(new ItemsAdapter(getActivity(), R.layout.custom, titles, drawables.toArray(new Drawable[drawables.size()])));

        ImageButton addButton = (ImageButton) view.findViewById(R.id.add_moyen);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = moyensSpinner.getSelectedItemPosition();
                final Mean mean = new Mean();
                mean.setVehicle(Vehicle.valueOf(titles[position]));
                sendRequestMeanAsync(mean);

            }
        });

        return view;
    }

    /**
     * Formattage des moyens extra pour l'adapterMeans
     *
     * @param intervention
     * @param position
     */
    private void initImagesTitles(Intervention intervention, int position, List<Mean> listXtra) {
        int xtraSize = listXtra.size(); // taille des moyens supplémentaires
        means = new Symbol[xtraSize];

        if (xtraSize > 0) {
            for (Mean m : listXtra) {
                String meanClass = m.getVehicle().toString();
                String meanType = Symbol.getImage(meanClass);
                means[position] = new Symbol(m.getId(),
                        Symbol.SymbolType.valueOf(meanType),
                        meanClass,
                        Symbol.getCityTrigram(),
                        Symbol.getMeanColor(m.getVehicle()));
                position++;
            }
            Toast.makeText(getActivity(), "Nombre de demandes supplémentaires " + xtraSize, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "intervention " + intervention.getId() + "\n n'a pas de demandes de moyens extra ", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Méthode d'envoi d'une demande d'un moyen supplémentaire.
     *
     * @param mean
     * @return : -
     */
    private void sendRequestMeanAsync(final Mean mean) {
        RestService requestSnd = RestServiceImpl.getInstance();

        Map<String, String> map = new HashMap<>();
        map.put("id", interventionID);

        requestSnd.post(RestAPI.POST_SEND_MEAN_REQUEST, map, mean, Mean.class, new Command() {
            /**
             * Success connection
             * @param response Response object type User
             */
            @Override
            public void execute(Object response) {
                // Demander la prise en compte de la validation de l'identification
                Mean moyen = (Mean) response;
                Log.i(TAG, "On  Post execute\t" + moyen.getId() + "\tVehicule\t" + moyen.getVehicle());
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
    }

    /**
     * Positionnement de l'intervention courante via son identifiant
     * @param interventionID
     */
    public void setInterventionID(String interventionID) {
        this.interventionID = interventionID;
    }
}