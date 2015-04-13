package fr.m2gla.istic.projet.fragments;


import android.app.ListFragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.constantes.Constant;

public class MoyensInitFragment extends ListFragment {
    String[] titles = new String[]{"Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread"};
    String[] Version = new String[]{"1.5", "1.6", "2.0-2.1", "2.2", "2.3", "3.0-3.2", "4.0", "4.1-4.3", "4.4"};

    // Declaring the String Array with the Text Data for the Spinners
    String[] Languages = {"Select a Language", "C# Language", "HTML Language",
            "XML Language", "PHP Language"};

    // Declaring the Integer Array with resourse Id's of Images for the Spinners
    String [] images = {Constant.SVG_COLONNE_INCENDIE_ACTIVE,Constant.SVG_GROUPE_INCENDIE_ACTIF,Constant.SVG_MOYEN_INTERVENTION_AERIEN};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.moyens_init_fragment, container, false);

        //setListAdapter(new ItemsAdapter(getActivity(), R.layout.custom, titles, images));
        //FIXME: remplacer plus tard
        setListAdapter(new ItemsAdapter(getActivity(), R.layout.custom, titles, new int[]{}));
//        Log.e("sow", this.getListAdapter().getItem(0).toString());

        return view;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        MoyensSuppFragment txt = (MoyensSuppFragment) getFragmentManager().findFragmentById(R.id.fragment_moyens_supp);
        txt.change(titles[position], "Version : " + Version[position]);
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

                // Create a new ClipData.Item from the ImageView object's tag
                ClipData.Item item = new ClipData.Item((String)v.getTag());

                // Create a new ClipData using the tag as a label, the plain text MIME type, and
                // the already-created item. This will create a new ClipDescription object within the
                // ClipData, and set its MIME type entry to "text/plain"
                ClipData dragData = new ClipData((String)v.getTag(),
                        new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item);

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

        getListView().setOnDragListener(new AdapterView.OnDragListener(){
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
        });
    }
}