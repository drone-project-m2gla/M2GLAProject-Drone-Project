package fr.m2gla.istic.projet.fragments;


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
import fr.m2gla.istic.projet.model.SVGAdapter;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

import static fr.m2gla.istic.projet.model.Symbol.SymbolType.valueOf;

public class MoyensInitFragment extends ListFragment {
    private static final String TAG = "MoyensInitFragment";

    private String idIntervention = "";
    private View view;
    private Symbol[] means;
    ArrayAdapter adapterMeans;
    List<String> titles;
    private int positionElement;
    private List<Boolean> isDeclineList = new ArrayList();
    private List<Boolean> draggable = new ArrayList();
    private Intervention intervention;
    private List<Mean> meanNotInPosition = new ArrayList<>();
    private List<Mean> meanNotValidated = new ArrayList<>();
    private List<Mean> meanRefused = new ArrayList();
    private Symbol[] meansXNotValidate;
    private Symbol[] meansXRefused;
    private ArrayAdapter adapterXtraRefused;
    private ArrayAdapter adapterXtraNotValidate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.moyens_init_fragment, container, false);
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
                positionElement = position;

                if (draggable.get(positionElement)) {
                    // Create a new ClipData.
                    // This is done in two steps to provide clarity. The convenience method
                    // ClipData.newPlainText() can create a plain text ClipData in one step.

                    // Create a new ClipData.Item from the ImageView Symbol Name
                    ClipData.Item item0 = new ClipData.Item(means[position].getId());

                    // Create a new ClipData using the tag as a label, the plain text MIME type, and
                    // the already-created item. This will create a new ClipDescription object within the
                    // ClipData, and set its MIME type entry to "text/plain"
                    ClipData dragData = new ClipData((String) v.getTag(),
                            new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                            item0);

                    ClipData.Item item1 = new ClipData.Item(means[position].getSymbolType().name());
                    dragData.addItem(item1);
                    ClipData.Item item2 = new ClipData.Item(means[position].getFirstText());
                    dragData.addItem(item2);
                    ClipData.Item item3 = new ClipData.Item(means[position].getSecondText());
                    dragData.addItem(item3);
                    ClipData.Item item4 = new ClipData.Item(means[position].getColor());
                    dragData.addItem(item4);

                    // Instantiates the drag shadow builder.
                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);

                    // Starts the drag
                    v.startDrag(dragData,  // the data to be dragged
                            myShadow,  // the drag shadow builder
                            null,      // no need to use local data
                            0          // flags (not currently used, set to 0)
                    );

                } else {
                    String title = "";
                    String message = "";
                    if (isDeclineList.get(positionElement)) {
                        title = "ATTENTION!!!!!!!!!!!!!!!!!!";

                        message = "Moyen " + titles.get(positionElement) + ".\nValidation refusée.".toUpperCase();
                    } else {
                        title = "INFO";

                        message = "Moyen " + titles.get(positionElement) + ".\nValidation en attente.".toUpperCase();
                    }
                    new AlertDialog.Builder(getActivity())
                            .setTitle(title)
                            .setMessage(message)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
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


        int meanWithoutCoordinates = 0;
        int pos = 0;

        for (Mean m : listMean) {
            Log.i(TAG, pos++ + "  Not is NaN******\t" + m.getCoordinates().getLatitude());
            if ("NaN".equals(m.getCoordinates().getLatitude())) {
                meanNotInPosition.add(m);
            }
        }

        int meanSize = meanNotInPosition.size(); // taille de la liste des moyens non placés sur la map
        means = new Symbol[meanSize];

        if (meanSize > 0) {
            for (Mean m : meanNotInPosition) {
                String meanClass = m.getVehicle().toString();
                String meanType = Constant.getImage(meanClass);
                means[position] = new Symbol(m.getId(),
                        valueOf(meanType), meanClass, "RNS", "ff0000");
                draggable.add(true);
                isDeclineList.add(m.getIsDeclined());

                position++;
            }
        }

        // Init list des moyens refusés et non validés
        for (Mean m : listXtra) {
            if (!m.getIsDeclined()) {
                meanNotValidated.add(m);
            } else {
                meanRefused.add(m);
            }
        }

        int xtraNotValidateSize = meanNotValidated.size(); // taille des moyens supplémentaires non validés
        meansXNotValidate = new Symbol[xtraNotValidateSize];
        if (xtraNotValidateSize > 0) {
            position = 0;
            for (Mean m : meanNotValidated) {
                String meanClass = m.getVehicle().toString();
                String meanType = Constant.getImage(meanClass);
                meansXNotValidate[position] = new Symbol(m.getId(),
                        valueOf(meanType), meanClass, "RNS", "ff0000");
                draggable.add(false);
                isDeclineList.add(m.getIsDeclined());

                position++;
            }
        }

        int xtraRefusedSize = meanRefused.size(); // taille des moyens supplémentaires non validés
        meansXRefused = new Symbol[xtraRefusedSize];
        if (xtraRefusedSize > 0) {
            position = 0;
            for (Mean m : meanRefused) {
                String meanClass = m.getVehicle().toString();
                String meanType = Constant.getImage(meanClass);
                meansXRefused[position] = new Symbol(m.getId(),
                        valueOf(meanType), meanClass, "RNS", "ff0000");
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
                Toast.makeText(getActivity(), "  test intervention return " + intervention.getId(), Toast.LENGTH_LONG).show();
                int i = 0;

                List<Mean> meanList = intervention.getMeansList();
                List<Mean> xtraList = intervention.getMeansXtra();

                Log.i(TAG, "mean size\t" + meanList.size());
                Log.i(TAG, "xtra size\t" + xtraList.size());

                // Initialisation des titres et images.
                initImagesTitles(intervention, i, meanList, xtraList);

                //Listes pour générer tableaux pour adapterMeans
                List<Drawable> drawables = new ArrayList<Drawable>();
                titles = new ArrayList<String>();

                if (means.length > 0) {
                    for (Symbol mean : means) {
                        drawables.add(SVGAdapter.convertSymbolToDrawable(getActivity().getApplicationContext(), mean));
                        titles.add(mean.getFirstText() + " * " + mean.getId());
                    }

                    ListView moyensListView = getListView();

                    // Set drawable to adapterMeans
                    Drawable[] imagesArray = drawables.toArray(new Drawable[drawables.size()]);

                    // Set image title to adapterMeans
                    String[] titlesArray = titles.toArray(new String[titles.size()]);

                    Context activity = MoyensInitFragment.this.getActivity();
                    adapterMeans = new ItemsAdapter(activity, R.layout.custom, titlesArray, imagesArray);
                    moyensListView.setAdapter(adapterMeans);

                }

                if (meansXRefused.length > 0) {
                    for (Symbol mean : meansXRefused) {
                        drawables.add(SVGAdapter.convertSymbolToDrawable(getActivity().getApplicationContext(), mean));
                        titles.add(mean.getFirstText() + " * " + mean.getId());
                    }

                    ListView moyensListView = getListView();

                    // Set drawable to adapterMeans
                    Drawable[] imagesArray = drawables.toArray(new Drawable[drawables.size()]);

                    // Set image title to adapterMeans
                    String[] titlesArray = titles.toArray(new String[titles.size()]);

                    // Refused
                    ListView refusedView = (ListView) view.findViewById(R.id.list_refused);
                    adapterXtraRefused = new ItemsAdapter(getActivity(), R.layout.custom, titlesArray, imagesArray);
                    refusedView.setAdapter(adapterXtraRefused);
                }

                if (meansXNotValidate.length > 0) {
                    for (Symbol mean : meansXNotValidate) {
                        drawables.add(SVGAdapter.convertSymbolToDrawable(getActivity().getApplicationContext(), mean));
                        titles.add(mean.getFirstText() + " * " + mean.getId());
                    }

                    ListView moyensListView = getListView();

                    // Set drawable to adapterMeans
                    Drawable[] imagesArray = drawables.toArray(new Drawable[drawables.size()]);

                    // Set image title to adapterMeans
                    String[] titlesArray = titles.toArray(new String[titles.size()]);

                    // to validated
                    ListView notValidatedView = (ListView) view.findViewById(R.id.list_not_validated);
                    adapterXtraNotValidate = new ItemsAdapter(getActivity(), R.layout.custom, titlesArray, imagesArray);
                    notValidatedView.setAdapter(adapterXtraNotValidate);

                }
            }
        };
    }
}