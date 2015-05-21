package fr.m2gla.istic.projet.fragments;


import android.app.ListFragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.ItemsAdapter;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.context.ViewHolder;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.SVGAdapter;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanMovingMap;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanSupplAdd;

import static fr.m2gla.istic.projet.model.Symbol.SymbolType.valueOf;

public class MoyensInitFragment extends ListFragment {
    private static final String TAG = "MoyensInitFragment";

    private String idIntervention = "";
    private View view;
    private Symbol[] means;
    List<String> titles;
    private List<Boolean> isDeclineList = new ArrayList();
    private List<Boolean> draggable = new ArrayList();
    private Intervention intervention;
    private List<Mean> meanRefused = new ArrayList();
    private Symbol[] meansXRefused;
    private List<Drawable> drawables;
    // Moyens disponibles
    private List<String> moyensDisponiblesTitle = new ArrayList<>();
    private List<Drawable> moyensDisponiblesDrawable = new ArrayList<>();

    // Moyens en transit
    private List<String> moyensTransitTitle = new ArrayList<>();
    private List<Drawable> moyensTransitDrawable = new ArrayList<>();


    // Moyens non validés
    List<String> meansNotValidateTitle = new ArrayList<>();
    List<Drawable> meansNotValidateDrawable = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.moyens_init_fragment, container, false);

        StrategyMeanSupplAdd.getINSTANCE().setFragment(this);
        StrategyMeanMovingMap.getINSTANCE().setFragment(this);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        getListView().setSelector(android.R.color.holo_blue_dark);
    }

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
                MapActivity.setDraggingMode();
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

                /*ClipData.Item item1 = new ClipData.Item(symbol.getSymbolType().name());
                dragData.addItem(item1);
                ClipData.Item item2 = new ClipData.Item(symbol.getFirstText());
                dragData.addItem(item2);
                ClipData.Item item3 = new ClipData.Item(symbol.getSecondText());
                dragData.addItem(item3);
                ClipData.Item item4 = new ClipData.Item(symbol.getColor());
                dragData.addItem(item4);*/

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
     * Formattage des moyens extra pour l'adapterMeans
     *
     * @param intervention
     * @param position
     * @param listMean
     */
    private void initImagesTitles(Intervention intervention, int position, List<Mean> listMean, List<Mean> listXtra) {


        int xtraRefusedSize = meanRefused.size(); // taille des moyens supplémentaires non validés
        meansXRefused = new Symbol[xtraRefusedSize];
        if (xtraRefusedSize > 0) {
            position = 0;
            for (Mean m : meanRefused) {
                String vehicule = m.getVehicle().toString();
                String vehiculeName = Symbol.getImage(vehicule);
                meansXRefused[position] = new Symbol(m.getId(),
                        valueOf(vehiculeName), vehicule, Symbol.getCityTrigram(), Symbol.getMeanColor(m.getVehicle()));
                draggable.add(false);
                isDeclineList.add(m.getIsDeclined());

                position++;
            }
        }
    }

    /**
     * Set intervention id from another fragment or activity
     *
     * @param idIntervention
     */
    public void setInterventionID(String idIntervention) {
        this.idIntervention = idIntervention;

        if (!idIntervention.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.put("id", idIntervention);
            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION, map, Intervention.class, getCallbackSuccess(), getCallbackError());
            // Appel du drag and drop.
            listenerDragAndDrop();
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
                int i = 0;

                List<Mean> meanList = intervention.getMeansList();
                List<Mean> xtraList = intervention.getMeansXtra();

                List<Mean> meanNotValidateList = new ArrayList<>();
                // Init list des moyens refusés et non validés
                for (Mean m : xtraList) {
                    if (!m.getIsDeclined()) {
                        meanNotValidateList.add(m);
                    } else {
                        meanRefused.add(m);
                    }
                }
                createNotValidateMeansView(meanNotValidateList.toArray(new Mean[meanNotValidateList.size()])); // Appel de la méthode qui cré la view des moyens demandés.
                // Done moyens refusés.

                // Initialisation des titres et images.
                initImagesTitles(intervention, i, meanList, xtraList);


                // Appel de la méthode qui cré la view des moyens disponibles.
                createAvailableMeansView(meanList);

                String textViewStringValue;
                String valueOfTextView;

                //Listes pour générer tableaux pour adapterMeans
                drawables = new ArrayList<Drawable>();
                titles = new ArrayList<String>();
                Spinner refusedMeansSpinner = (Spinner) view.findViewById(R.id.refused_means_spinner);

                if (meansXRefused.length > 0) {
                    titles.clear();
                    drawables.clear();
                    for (Symbol symbol : meansXRefused) {
                        drawables.add(SVGAdapter.convertSymbolToDrawable(getActivity().getApplicationContext(), symbol));
                        titles.add(symbol.getFirstText() + " * " + symbol.getId());
                    }

                    ListView moyensListView = getListView();

                    // Set drawable to adapterMeans
                    Drawable[] imagesArray = drawables.toArray(new Drawable[drawables.size()]);

                    // Set image title to adapterMeans
                    String[] titlesArray = titles.toArray(new String[titles.size()]);

                    // Refused
//                    ListView refusedView = (ListView) view.findViewById(R.id.list_refused);
                    ArrayAdapter adapterXtraRefused = new ItemsAdapter(getActivity(), R.layout.custom, titlesArray, imagesArray);
//                    refusedView.setAdapter(adapterXtraRefused);

                    refusedMeansSpinner.setAdapter(adapterXtraRefused);
                } else {
                    refusedMeansSpinner.setVisibility(View.GONE);
                }
                TextView refusedTextView = (TextView) view.findViewById(R.id.moyens_refuses_textview);
                textViewStringValue = getResources().getString(R.string.moyens_refuses);
                valueOfTextView = textViewStringValue + (" (" + meansXRefused.length + ")");
                refusedTextView.setText(valueOfTextView);
            }
        };
    }

    /**
     * méthode qui cré la view des moyens disponibles.
     *
     * @param meanList
     */
    private void createAvailableMeansView(List<Mean> meanList) {

        moyensDisponiblesDrawable.clear();
        moyensDisponiblesTitle.clear();

        int pos = 0;

        List<Mean> meanNotInPosition = new ArrayList<>();
        for (Mean m : meanList) {
            boolean isNaN = Double.isNaN(m.getCoordinates().getLatitude());//"NaN".equals();
            if (isNaN) {
                meanNotInPosition.add(m);
            }
        }

        int meanSize = meanNotInPosition.size(); // taille de la liste des moyens non placés sur la map
        means = new Symbol[meanSize];

        if (meanNotInPosition.size() > 0) {
            for (Mean m : meanNotInPosition) {
                String vehicule = m.getVehicle().toString();
                String vehiculeName = Symbol.getImage(vehicule);
                Symbol symbol = new Symbol(m.getId(),
                        valueOf(vehiculeName), vehicule, Symbol.getCityTrigram(), Symbol.getMeanColor(m.getVehicle()));

                means[pos++] = symbol;

                String title = vehicule + " * " + m.getId();

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
    }

    // Méthode appelé par la strategie.
    public void addMeanStrategy(final Mean object) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // GET_MOYENS_EXTRAS
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", idIntervention);
                RestServiceImpl.getInstance()
                        .get(RestAPI.GET_MOYENS_EXTRAS, map, Mean[].class, new Command() {
                            @Override
                            public void execute(Object response) {
                                Mean[] means = (Mean[]) response;

                                createNotValidateMeansView(means); // Appel de la méthode qui cré la view des moyens demandés.
                            }
                        }, getCallbackError());
            }
        });
    }

    // Méthode appelé par la strategie.
    public void movingMapMeanStrategy(final Mean object) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // GET_MOYENS_EXTRAS
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", idIntervention);
                RestServiceImpl.getInstance()
                        .get(RestAPI.GET_MOYENS_DISPO, map, Mean[].class, new Command() {
                            @Override
                            public void execute(Object response) {
                                Mean[] means = (Mean[]) response;
                                createAvailableMeansView(Arrays.asList(means)); // Appel de la méthode qui cré la view des moyens demandés.
                            }
                        }, getCallbackError());
            }
        });
    }

    /**
     * méthode qui cré la view des moyens demandés.
     *
     * @param means
     */
    private void createNotValidateMeansView(Mean[] means) {
        meansNotValidateTitle.clear();
        meansNotValidateDrawable.clear();
        for (int i = 0; i < means.length; i++) {
            Mean m = means[i];
            if (!m.getIsDeclined()) {
                String vehicule = m.getVehicle().toString();
                String vehiculeName = Symbol.getImage(vehicule);

                Symbol symbol = new Symbol(m.getId(),
                        valueOf(vehiculeName), vehicule, Symbol.getCityTrigram(), Symbol.getMeanColor(m.getVehicle()));

                String title = vehicule + " * " + m.getId();
                meansNotValidateTitle.add(title);
                meansNotValidateDrawable.add(SVGAdapter.convertSymbolToDrawable(getActivity().getApplicationContext(), symbol));
            }
        }

        // Set drawable to adapterMeans
        Drawable[] imagesArray = meansNotValidateDrawable.toArray(new Drawable[meansNotValidateDrawable.size()]);

        // Set image title to adapterMeans
        String[] titlesArray = meansNotValidateTitle.toArray(new String[meansNotValidateTitle.size()]);

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