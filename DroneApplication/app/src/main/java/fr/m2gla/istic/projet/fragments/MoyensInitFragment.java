package fr.m2gla.istic.projet.fragments;


import android.app.ListFragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.ArrayList;
import java.util.List;
import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.constantes.Constant;

public class MoyensInitFragment extends ListFragment {
    String[] titles = new String[]{Constant.VALUE_COLONNE_INCENDIE_ACTIVE,
            Constant.VALUE_GROUPE_INCENDIE_ACTIF,
            Constant.VALUE_MOYEN_INTERVENTION_AERIEN,
            Constant.VALUE_SECOUR_A_PERSONNE_PREVU,
            Constant.VALUE_VEHICULE_A_INCENDIE_SEUL};

    String [] images = {Constant.SVG_COLONNE_INCENDIE_ACTIVE,
            Constant.SVG_GROUPE_INCENDIE_ACTIF,
            Constant.SVG_MOYEN_INTERVENTION_AERIEN,
            Constant.SVG_SECOUR_A_PERSONNE_PREVU,
            Constant.SVG_VEHICULE_A_INCENDIE_SEUL};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.moyens_init_fragment, container, false);

        List<Drawable> drawables = new ArrayList<Drawable>();
        for (String imageId : images) {
            try {
                SVG svg = SVG.getFromResource(this.getActivity(), getResources().getIdentifier(imageId, "raw", this.getActivity().getPackageName()));
                drawables.add(new PictureDrawable(svg.renderToPicture()));
            } catch (SVGParseException e) {
            }
        }
        setListAdapter(new ItemsAdapter(getActivity(), R.layout.custom, titles, drawables.toArray(new Drawable[drawables.size()])));
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //MoyensSuppFragment txt = (MoyensSuppFragment) getFragmentManager().findFragmentById(R.id.fragment_moyens_supp);
        //txt.change(titles[position], "Version : " + Version[position]);
        getListView().setSelector(android.R.color.holo_blue_dark);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           int position, long id) {
                // Create a new ClipData.
                // This is done in two steps to provide clarity. The convenience method
                // ClipData.newPlainText() can create a plain text ClipData in one step.

                // Create a new ClipData.Item from the ImageView Symbol Name
                ClipData.Item itemSymbolName = new ClipData.Item(images[position]);

                // Create a new ClipData using the tag as a label, the plain text MIME type, and
                // the already-created item. This will create a new ClipDescription object within the
                // ClipData, and set its MIME type entry to "text/plain"
                ClipData dragData = new ClipData((String)v.getTag(),
                        new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
                        itemSymbolName);

                //ClipData.Item item = new ClipData.Item(images[position]);
                //dragData.addItem(item);

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

        /*getListView().setOnDragListener(new AdapterView.OnDragListener(){
            @Override
            public boolean onDrag(View v, DragEvent event) {

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Toast.makeText(getActivity(), "Drag", Toast.LENGTH_SHORT).show();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        Toast.makeText(getActivity(), "Drag Ended", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;
            }
        });*/
    }
}