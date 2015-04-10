package fr.m2gla.istic.projet.fragments;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import fr.m2gla.istic.projet.activity.R;

public class MoyensInitFragment extends ListFragment {
    String[] titles = new String[]{"Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread"};
    String[] Version = new String[]{"1.5", "1.6", "2.0-2.1", "2.2", "2.3", "3.0-3.2", "4.0", "4.1-4.3", "4.4"};

    // Declaring the String Array with the Text Data for the Spinners
    String[] Languages = {"Select a Language", "C# Language", "HTML Language",
            "XML Language", "PHP Language"};

    // Declaring the Integer Array with resourse Id's of Images for the Spinners
    Integer[] images = {R.drawable.common_ic_googleplayservices, R.drawable.common_full_open_on_phone, R.drawable.common_signin_btn_icon_dark,
            R.drawable.common_full_open_on_phone, R.drawable.common_signin_btn_icon_light};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.moyens_init_fragment, container, false);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, titles);

        setListAdapter(new ItemsAdapter(getActivity(), R.layout.custom, titles, images));

        return view;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        MoyensSuppFragment txt = (MoyensSuppFragment) getFragmentManager().findFragmentById(R.id.fragment_moyens_supp);
        txt.change(titles[position], "Version : " + Version[position]);
        getListView().setSelector(android.R.color.holo_blue_dark);
    }
}