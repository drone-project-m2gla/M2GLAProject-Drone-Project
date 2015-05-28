package fr.m2gla.istic.projet.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.ItemsAdapter;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.context.SVGAdapter;
import fr.m2gla.istic.projet.context.ViewHolder;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.model.Vehicle;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanArrived;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanBackToCRM;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanFree;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanMovingMap;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanSupplAdd;

import static fr.m2gla.istic.projet.model.Symbol.SymbolType.valueOf;

/**
 * Fragment d'initialisation des moyens
 */
public class MoyensInitFragment extends ListFragment {
    private static final String TAG = "MoyensInitFragment";

    private String idIntervention = "";
    private View view;
    private Symbol[] means;
    //List<String> titles;
    private Intervention intervention;
    // Moyens disponibles
    private List<String> moyensDisponiblesTitle = new ArrayList<>();
    private List<Drawable> moyensDisponiblesDrawable = new ArrayList<>();

    // Moyens en transit
    private List<String> moyensRefusedTitle = new ArrayList<>();
    private List<Drawable> moyensRefusedDrawable = new ArrayList<>();

    // Moyens en transit
    //private List<String> moyensTransitTitle = new ArrayList<>();
    //private List<Drawable> moyensTransitDrawable = new ArrayList<>();

    // Moyens non validés
    List<String> meansRequestedTitle = new ArrayList<>();
    List<Drawable> meansRequestedDrawable = new ArrayList<>();

    // Moyens en transit
    private List<String> moyensInTransitTitle = new ArrayList();
    private List<Drawable> moyensInTransitDrawable = new ArrayList();
    private List<Mean> availableMeanList;
    private List<Mean> refusedMeanList;
    private List<Mean> transitMeanList;
    private List<Mean> requestedMeanList;

    /**
     * Methode principale
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.moyens_init_fragment, container, false);

        StrategyMeanSupplAdd.getINSTANCE().setFragment(this);
        StrategyMeanMovingMap.getINSTANCE().setFragment(this);
        StrategyMeanFree.getINSTANCE().setFragment(this);
        StrategyMeanBackToCRM.getINSTANCE().setFragment(this);
        StrategyMeanArrived.getINSTANCE().setFragment(this);// Strategie des moyens arrivés.

        return view;
    }

    /**
     * /**
     * Methode de creation du menu du fragment
     *
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        getListView().setSelector(android.R.color.holo_blue_dark);
    }

    /**
     * Methode de gestion de l'usage du menu du fragment
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Méthode drag and drop des moyens sur la map.
     */
    private void listenerDragAndDrop() {
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           int position, long id) {

                ((MapActivity) getActivity()).setDraggingMode(true);
                // Create a new ClipData.
                // This is done in two steps to provide clarity. The convenience method
                // ClipData.newPlainText() can create a plain text ClipData in one step.

                // Create a new ClipData.Item from the ImageView Symbol Name
                Symbol symbol = means[position];

                ClipData.Item item0 = new ClipData.Item(symbol.getId());

                // Create a new ClipData using the tag as a label, the plain text MIME type, and
                // the already-created item. This will create a new ClipDescription object within the
                // ClipData, and set its MIME type entry to "text/plain"
                ViewHolder holder = (ViewHolder) v.getTag();
                ClipData dragData = new ClipData(holder.toString(),
                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item0);

                // Instantiates the drag shadow builder.
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);

                // Starts the drag
                v.startDrag(dragData,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        null,      // no need to use local data
                        0          // flags (not currently used, set to 0)
                );

                return true;
            }
        });


    }

    /**
     * Methode permettant de specifier l'intervention en cours
     *
     * @param idIntervention
     */
    public void setInterventionID(String idIntervention) {
        this.idIntervention = idIntervention;

        if (!idIntervention.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.put("id", idIntervention);
            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION, map, Intervention.class, getCallbackSuccess(), new Command() {
                        @Override
                        public void execute(Object response) {
                            Toast.makeText(getActivity(), "ERROR\nRequête HTTP en échec", Toast.LENGTH_LONG).show();
                        }
                    });

            // Appel du drag and drop.
            listenerDragAndDrop();
        }
    }

    /**
     * Récupération de l'identifiant de l'intervention en cours
     *
     * @return : l'identifiant de l'intervention en cours
     */
    public String getIdIntervention() {
        return idIntervention;
    }

    /**
     * Methode de gestion des retours en erreur d'un appel à un service REST
     *
     * @return la classe "command" de gestion de succes attentue par le service REST
     */
    private Command getCallbackError() {
        return new Command() {
            @Override
            public void execute(Object response) {
                Toast.makeText(getActivity(), "ERROR\nRequête HTTP en échec", Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * Methode de gestion des retours en succes d'un appel à un service REST
     *
     * @return la classe "command" de gestion de succes attentue par le service REST
     */
    private Command getCallbackSuccess() {
        return new Command() {
            @Override
            public void execute(Object response) {
                intervention = (Intervention) response;
                int i = 0;

                if (intervention != null) {
                    List<Mean> meanList = intervention.meansArrived();
                    List<Mean> requestedList = intervention.meansRequested();
                    List<Mean> meanRefused = intervention.meansRefused();

                    // Appel de la méthode qui crée la view des moyens demandés.
                    createRequestedMeansView(requestedList.toArray(new Mean[requestedList.size()]));

                    // Appel de la méthode qui crée la view des moyens disponibles.
                    createAvailableMeansView(meanList);

                    // Appel de la méthode qui crée la view des moyens refusés
                    createRefusedMeansView(meanRefused);

                    // Appel de la méthode qui crée la view des moyens en transit
                    List<Mean> activedList = intervention.meansTransit();
                    createTransitMeansView(activedList);
                }
            }
        };
    }

    /**
     * Création de la liste des moyens en transit
     *
     * @param transitList : liste des moyensen transit
     */
    private void createTransitMeansView(final List<Mean> transitList) {

        moyensInTransitTitle.clear();
        moyensInTransitDrawable.clear();

        if (transitList.size() > 0) {
            for (Mean m : transitList) {
                String vehicule = m.getVehicle().toString();
                String vehiculeName = Symbol.getImage(vehicule);
                String title;

                Symbol symbol = new Symbol(m.getId(),
                        valueOf(vehiculeName), vehicule, m.getName(), Symbol.getMeanColor(m.getVehicle()));

                if ("".compareTo(m.getName()) == 0) {
                    title = vehicule;
                }
                else {
                    title = vehicule + " * " + m.getName();
                }


                moyensInTransitTitle.add(title);
                moyensInTransitDrawable.add(SVGAdapter.convertSymbolToDrawable(getActivity().getApplicationContext(), symbol));
            }
        }

        //Listes pour générer tableaux pour adapterMeans
        ListView transitMeansListView = (ListView) view.findViewById(R.id.list_en_transit);

        int sizeOfRefusedMeans = moyensInTransitDrawable.size();
        if (sizeOfRefusedMeans > 0) {

            // Set drawable to adapterMeans
            Drawable[] imagesArray = moyensInTransitDrawable.toArray(new Drawable[sizeOfRefusedMeans]);

            // Set image title to adapterMeans
            String[] titlesArray = moyensInTransitTitle.toArray(new String[sizeOfRefusedMeans]);


            ArrayAdapter adapterXtraRefused = new ItemsAdapter(getActivity(), R.layout.custom, titlesArray, imagesArray);

            transitMeansListView.setAdapter(adapterXtraRefused);

        } else {
            transitMeansListView.setVisibility(View.GONE);
        }
        TextView refusedTextView = (TextView) view.findViewById(R.id.moyens_transit_textview);
        String textViewStringValue = getResources().getString(R.string.moyens_en_transit);
        String valueOfTextView = textViewStringValue + (" (" + sizeOfRefusedMeans + ")");
        refusedTextView.setText(valueOfTextView);

        transitMeansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Sélectionnez une action sur le moyen")
                        .setItems(R.array.transitMeansOptions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, String> param = new HashMap<String, String>();
                                param.put("id", idIntervention);
                                //Vehicle v = null;
                                String restService = "";
                                String toastValue = null;
                                switch (which) {
                                    // Validation de l'arrivée du moyen.
                                    case 0: {
                                        restService = RestAPI.POST_VALIDER_ARRIVEE_MOYEN;
                                        toastValue = "ARRIVEE DU MOYEN";
                                        break;
                                    }
                                    case 1: {
                                        restService = RestAPI.POST_VALIDER_LIBERATION_MOYEN;
                                        toastValue = "LIBERATION DU MOYEN";
                                        break;
                                    }
                                }
                                Log.i(TAG, "REST VALUE\t" + restService);
                                final String finalToastValue = toastValue;
                                RestServiceImpl.getInstance().post(restService, param, transitList.get(position), Mean.class, new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Mean m = (Mean) response;
                                        Toast.makeText(getActivity(), finalToastValue + "\n" + m.getName(), Toast.LENGTH_SHORT).show();

                                        // Mise à jour de la liste des moyens disponibles
                                        movingMapMeanStrategy(m);

                                        // Mise à jour de la liste des moyens en transit.
                                        transitMeanStrategy(m);
                                    }
                                }, getCallbackError());

                            }
                        }).show();
            }
        });
    }

    /**
     * Création de la liste des moyens refusés.
     *
     * @param meanRefused : liste des moyens refusés
     */
    private void createRefusedMeansView(List<Mean> meanRefused) {

        moyensRefusedTitle.clear();
        moyensRefusedDrawable.clear();

        if (meanRefused.size() > 0) {
            for (Mean m : meanRefused) {
                String vehicule = m.getVehicle().toString();
                String vehiculeName = Symbol.getImage(vehicule);
                String title;

                Symbol symbol = new Symbol(m.getId(),
                        valueOf(vehiculeName), vehicule, m.getName(), Symbol.getMeanColor(m.getVehicle()));


                if ("".compareTo(m.getName()) == 0) {
                    title = vehicule;
                }
                else {
                    title = vehicule + " * " + m.getName();
                }

                moyensRefusedTitle.add(title);
                moyensRefusedDrawable.add(SVGAdapter.convertSymbolToDrawable(getActivity().getApplicationContext(), symbol));
            }
        }

        //Listes pour générer tableaux pour adapterMeans
        Spinner refusedMeansSpinner = (Spinner) view.findViewById(R.id.refused_means_spinner);

        int sizeOfRefusedMeans = moyensRefusedDrawable.size();
        if (sizeOfRefusedMeans > 0) {

            //ListView moyensListView = getListView();

            // Set drawable to adapterMeans
            Drawable[] imagesArray = moyensRefusedDrawable.toArray(new Drawable[sizeOfRefusedMeans]);

            // Set image title to adapterMeans
            String[] titlesArray = moyensRefusedTitle.toArray(new String[sizeOfRefusedMeans]);


            ArrayAdapter adapterXtraRefused = new ItemsAdapter(getActivity(), R.layout.custom, titlesArray, imagesArray);

            refusedMeansSpinner.setAdapter(adapterXtraRefused);
        } else {
            refusedMeansSpinner.setVisibility(View.GONE);
        }
        TextView refusedTextView = (TextView) view.findViewById(R.id.moyens_refuses_textview);
        String textViewStringValue = getResources().getString(R.string.moyens_refuses);
        String valueOfTextView = textViewStringValue + (" (" + sizeOfRefusedMeans + ")");
        refusedTextView.setText(valueOfTextView);
    }

    /**
     * méthode qui crée la liste des moyens disponibles.
     *
     * @param meanList : liste des moyens disponibles
     */
    private void createAvailableMeansView(final List<Mean> meanList) {

        moyensDisponiblesDrawable.clear();
        moyensDisponiblesTitle.clear();

        int pos = 0;

        int meanSize = meanList.size(); // taille de la liste des moyens non placés sur la map
        means = new Symbol[meanSize];

        if (meanList.size() > 0) {
            for (Mean m : meanList) {
                String vehicule = m.getVehicle().toString();
                String vehiculeName = Symbol.getImage(vehicule);
                String title;

                Symbol symbol = new Symbol(m.getId(),
                        valueOf(vehiculeName), vehicule, m.getName(), Symbol.getMeanColor(m.getVehicle()));

                means[pos++] = symbol;

                if ("".compareTo(m.getName()) == 0) {
                    title = vehicule;
                }
                else {
                    title = vehicule + " * " + m.getName();
                }

                moyensDisponiblesTitle.add(title);
                moyensDisponiblesDrawable.add(SVGAdapter.convertSymbolToDrawable(getActivity().getApplicationContext(), symbol));
            }
        }

        // Set drawable to adapterMeans
        Drawable[] imagesArray = moyensDisponiblesDrawable.toArray(new Drawable[moyensDisponiblesDrawable.size()]);//drawables.toArray(new Drawable[drawables.size()]);

        // Set image title to adapterMeans
        String[] titlesArray = moyensDisponiblesTitle.toArray(new String[moyensDisponiblesTitle.size()]); //titles.toArray(new String[titles.size()]);

        Context activity = this.getActivity();
        ArrayAdapter adapterMeans = new ItemsAdapter(activity, R.layout.custom, titlesArray, imagesArray);


        ListView moyensListView = getListView();
        moyensListView.setAdapter(adapterMeans);

        TextView dispoTextView = (TextView) view.findViewById(R.id.moyens_dispo_textview);
        String textViewStringValue = getResources().getString(R.string.moyens_a_placer);
        String valueOfTextView = textViewStringValue + (" (" + moyensDisponiblesDrawable.size() + ")");
        dispoTextView.setText(valueOfTextView);

        moyensListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Sélectionnez une action sur le moyen")
                        .setItems(R.array.dispoMeansOptions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, String> param = new HashMap<String, String>();
                                param.put("id", idIntervention);
                                Vehicle v = null;
                                String restService = "";
                                String toastValue = null;
                                switch (which) {
                                    case 0: {
                                        restService = RestAPI.POST_VALIDER_LIBERATION_MOYEN;
                                        toastValue = "LIBERATION DU MOYEN";
                                        break;
                                    }
                                }
                                Log.i(TAG, "REST VALUE\t" + restService);
                                final String finalToastValue = toastValue;
                                RestServiceImpl.getInstance().post(restService, param, meanList.get(position), Mean.class, new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Mean m = (Mean) response;
                                        Toast.makeText(getActivity(), finalToastValue + "\n" + m.getName(), Toast.LENGTH_SHORT).show();

                                        // Mise à jour de la liste des moyens disponibles
                                        movingMapMeanStrategy(m);

                                        // Mise à jour de la liste des moyens en transit.
                                        transitMeanStrategy(m);
                                    }
                                }, getCallbackError());

                            }
                        }).show();
            }
        });

    }

    /**
     * Méthode appelé par la strategie pour la demande de moyens supplémentaires
     *
     * @param object
     */
    public void demandMeanStrategy(final Mean object) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // GET_MOYENS_DISPO
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", idIntervention);
                    RestServiceImpl.getInstance()
                            .get(RestAPI.GET_MOYENS_DISPO, map, Mean[].class, new Command() {
                                @Override
                                public void execute(Object response) {
                                    Mean[] means = (Mean[]) response;
                                    //Log.i(TAG, "Size de mon tableau DEMAND " + means.length);
                                    createRequestedMeansView(means); // Appel de la méthode qui crée la view des moyens demandés.
                                }
                            }, getCallbackError());
                }
            });
        }
    }

    /**
     * Méthode appelé par la strategie pour lors de la validation des moyens par le CODIS
     *
     * @param object
     */
    public void refusedMeanStrategy(final Mean object) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // GET_MOYENS_DISPO
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", idIntervention);
                    RestServiceImpl.getInstance()
                            .get(RestAPI.GET_MOYENS_DISPO, map, Mean[].class, new Command() {
                                @Override
                                public void execute(Object response) {
                                    Mean[] means = (Mean[]) response;
                                    List<Mean> refusedMeans = new ArrayList<Mean>();
                                    if (means.length > 0) {
                                        for (Mean m : means) {
                                            if (m.refusedMeans()) {
                                                refusedMeans.add(m);
                                            }
                                        }
                                    }
                                    //Log.i(TAG, "Size de mon tableau DEMAND " + means.length);
                                    createRefusedMeansView(refusedMeans); // Appel de la méthode qui crée la view des moyens demandés.
                                }
                            }, getCallbackError());
                }
            });
        }
    }

    /**
     * Méthode appelé par la strategie lorsqu'un moyen est déplacé sur la carte
     *
     * @param object
     */
    public void movingMapMeanStrategy(final Mean object) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // GET_MOYENS_DISPO
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", idIntervention);
                    RestServiceImpl.getInstance()
                            .get(RestAPI.GET_MOYENS_DISPO, map, Mean[].class, new Command() {
                                @Override
                                public void execute(Object response) {
                                    Mean[] means = (Mean[]) response;
                                    List<Mean> meanList = new ArrayList<Mean>();
                                    if (means.length > 0) {
                                        for (Mean m : means) {
                                            if (m.arrivedMean()) {
                                                meanList.add(m);
                                            }
                                        }
                                    }
                                    // Appel de la méthode qui crée la view des moyens demandés.
                                    createAvailableMeansView(meanList);
                                }
                            }, getCallbackError());
                }
            });
        }
    }

    /**
     * Méthode appelé par la strategie lorsqu'un moyen est déplacé vers les moyens arrivés.
     *
     * @param object
     */
    public void transitMeanStrategy(final Mean object) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // GET_MOYENS_DISPO
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", idIntervention);
                RestServiceImpl.getInstance()
                        .get(RestAPI.GET_MOYENS_DISPO, map, Mean[].class, new Command() {
                            @Override
                            public void execute(Object response) {
                                Mean[] means = (Mean[]) response;
                                List<Mean> transitList = new ArrayList<Mean>();
                                if (means.length > 0) {
                                    for (Mean m : means) {
                                        if (m.onTransitMean()) {
                                            transitList.add(m);
                                        }
                                    }
                                }
                                // Appel de la méthode qui crée la view des moyens demandés.
                                createTransitMeansView(transitList);
                            }
                        }, getCallbackError());
            }
        });
    }

    /**
     * Méthode appelé par la strategie lorsqu'un moyen est arrivé au CRM
     *
     * @param object
     */
    public void arrivedMeanStrategy(final Mean object) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    // GET_MOYENS_DISPO
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", idIntervention);
                    RestServiceImpl.getInstance()
                            .get(RestAPI.GET_MOYENS_DISPO, map, Mean[].class, new Command() {
                                @Override
                                public void execute(Object response) {
                                    Mean[] means = (Mean[]) response;
                                    List<Mean> meanList = new ArrayList<Mean>();
                                    if (means.length > 0) {
                                        for (Mean m : means) {
                                            if (m.arrivedMean()) {
                                                meanList.add(m);
                                            }
                                        }
                                    }
                                    // Appel de la méthode qui crée la view des moyens demandés.
                                    createAvailableMeansView(meanList);
                                }
                            }, getCallbackError());

                }
            });
        }
    }

    /**
     * Methode qui appelle le service REST pour l'ensemble des moyens
     */
    private void callMeansREST() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", idIntervention);
        RestServiceImpl.getInstance()
                .get(RestAPI.GET_MOYENS_DISPO, map, Mean[].class, new Command() {
                    @Override
                    public void execute(Object response) {
                        Mean[] means = (Mean[]) response;

                        if (means.length > 0) {
                            for (Mean m : means) {
                                if (m.arrivedMean()) {
                                    availableMeanList.add(m);
                                }
                                if (m.refusedMeans()) {
                                    refusedMeanList.add(m);
                                }
                                if (m.onTransitMean()) {
                                    transitMeanList.add(m);
                                }
                                if (m.arrivedMean()) {
                                    availableMeanList.add(m);
                                }
                            }
                        }
                    }
                }, getCallbackError());
    }

    /**
     * méthode qui crée la liste des moyens demandés.
     *
     * @param means : Tableau des moyens demandés
     */
    private void createRequestedMeansView(Mean[] means) {
        meansRequestedTitle.clear();
        meansRequestedDrawable.clear();
        for (int i = 0; i < means.length; i++) {
            Mean m = means[i];
            if (m.requestedMean()) {
                String vehicule = m.getVehicle().toString();
                String vehiculeName = Symbol.getImage(vehicule);

                String title;


                Symbol symbol = new Symbol(m.getId(),
                        valueOf(vehiculeName), vehicule, m.getName(), Symbol.getMeanColor(m.getVehicle()));

                if ("".compareTo(m.getName()) == 0) {
                    title = vehicule;
                }
                else {
                    title = vehicule + " * " + m.getName();
                }
                meansRequestedTitle.add(title);
                meansRequestedDrawable.add(SVGAdapter.convertSymbolToDrawable(getActivity().getApplicationContext(), symbol));
            }
        }

        // Set drawable to adapterMeans
        Drawable[] imagesArray = meansRequestedDrawable.toArray(new Drawable[meansRequestedDrawable.size()]);

        // Set image title to adapterMeans
        String[] titlesArray = meansRequestedTitle.toArray(new String[meansRequestedTitle.size()]);

        TextView notValidatedTextView = (TextView) view.findViewById(R.id.moyens_supp_textview);
        String textViewStringValue = getResources().getString(R.string.moyens_a_valider);
        String valueOfTextView = textViewStringValue + (" (" + titlesArray.length + ")");
        notValidatedTextView.setText(valueOfTextView);

        // to validated
        ListView notValidatedView = (ListView) view.findViewById(R.id.list_not_validated);
        ArrayAdapter adapterXtraNotValidate = new ItemsAdapter(getActivity(), R.layout.custom, titlesArray, imagesArray);
        notValidatedView.setAdapter(adapterXtraNotValidate);
    }
}