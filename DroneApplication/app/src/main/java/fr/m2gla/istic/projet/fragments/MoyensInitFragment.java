package fr.m2gla.istic.projet.fragments;


import android.app.ListFragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

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

public class MoyensInitFragment extends ListFragment {
    private static final String TAG = "MoyensInitFragment";

    private String idIntervention = "";
    private View view;
    private Symbol[] means;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.moyens_init_fragment, container, false);
        Log.i(TAG, "I'm here!!!!");
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
                ClipData.Item item5 = new ClipData.Item(means[position].getDescription());
                dragData.addItem(item5);

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
     * Formattage des moyens extra pour l'adapter
     *
     * @param intervention
     * @param position
     * @param listMean
     */
    private void initImagesTitles(Intervention intervention, int position, List<Mean> listMean, List<Mean> listXtra) {
        int meanSize = listMean.size(); // taille de la liste des moyens
        int xtraSize = listXtra.size(); // taille des moyens supplémentaires
        means = new Symbol[meanSize + xtraSize];

        if (meanSize > 0) {
            for (Mean m : listMean) {
                String meanClass = m.getVehicle().toString();
                String meanType = Constant.getImage(meanClass);
                means[position] = new Symbol(m.getId(),
                       Symbol.SymbolType.valueOf(meanType), meanClass, "RNS", "ff0000", meanClass);
                position++;
            }
            if (xtraSize > 0) {
                for (Mean m : listXtra) {
                    String meanClass = m.getVehicle().toString();
                    String meanType = Constant.getImage(meanClass);
                    means[position] = new Symbol(m.getId(),
                            Symbol.SymbolType.valueOf(meanType), meanClass, "RNS", "ff0000", meanClass);
                    position++;
                }
            }
            Toast.makeText(getActivity(), "Nombre de demandes supplémentaires " + xtraSize, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "intervention " + intervention.getId() + "\n n'a pas de demandes de moyens extra ", Toast.LENGTH_LONG).show();
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
                Intervention intervention = (Intervention) response;
                Toast.makeText(getActivity(), "  test intervention return " + intervention.getId(), Toast.LENGTH_LONG).show();
                int i = 0;

                List<Mean> meanList = intervention.getMeansList();
                List<Mean> xtraList = intervention.getMeansXtra();

                // Initialisation des titres et images.
                initImagesTitles(intervention, i, meanList, xtraList);

                //Listes pour générer tableaux pour adapter
                List<Drawable> drawables = new ArrayList<Drawable>();
                List<String> titles = new ArrayList<String>();

                for (Symbol mean : means) {
                    drawables.add(SVGAdapter.convertSymbolToDrawable(getActivity().getApplicationContext(), mean));
                    titles.add(mean.getFirstText());
                }

                ListView moyensListView = getListView();
                Drawable[] imagesArray = drawables.toArray(new Drawable[drawables.size()]);
                String [] titlesArray = titles.toArray(new String[titles.size()]);
                Context activity = MoyensInitFragment.this.getActivity();
                ListAdapter adapter = new ItemsAdapter(activity, R.layout.custom, titlesArray, imagesArray);
                Log.i(TAG, "adapter  " + (adapter == null) +
                        " \nImage array  " + imagesArray.length +
                        " \ntitles " + (titlesArray == null) +
                        " \nactivity  " + (activity == null));

                Log.i(TAG, "List\t" + (moyensListView == null));

                moyensListView.setAdapter(adapter);
            }
        };
    }
}