package fr.m2gla.istic.projet.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import fr.m2gla.istic.projet.activity.R;


public class MoyensSuppFragment extends Fragment {
    TextView text, vers;

    public static ListView maListViewPerso;
    public static ArrayList<HashMap<String, String>> listItem;
    public static SimpleAdapter mListAdapter;
    private Spinner moyensSpinner;
    private static ArrayAdapter<CharSequence> adapter;

    // Declaring the String Array with the Text Data for the Spinners
    String[] Languages = {"Select a Language", "C# Language", "HTML Language",
            "XML Language", "PHP Language"};
    // Declaring the Integer Array with resourse Id's of Images for the Spinners
    Integer[] images = {0, R.drawable.common_full_open_on_phone, R.drawable.common_signin_btn_icon_dark,
            R.drawable.common_full_open_on_phone, R.drawable.common_signin_btn_icon_light};

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.moyens_supp_fragment, container, false);
        text = (TextView) view.findViewById(R.id.AndroidOs);
        vers = (TextView) view.findViewById(R.id.Version);

//        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.departements,
//                android.R.layout.simple_spinner_item);

        moyensSpinner = (Spinner) view
                .findViewById(R.id.moyensSpinner);

        // Setting a Custom Adapter to the Spinner
        moyensSpinner.setAdapter(new MyAdapter(getActivity(), R.layout.custom,
                Languages));

        // spinner
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        moyensSpinner.setAdapter(adapter);

        return view;

    }

    public void change(String txt, String txt1) {
        text.setText(txt);
        vers.setText(txt1);

    }

    // Creating an Adapter Class
    public class MyAdapter extends ArrayAdapter {

        public MyAdapter(Context context, int textViewResourceId,
                         String[] objects) {
            super(context, textViewResourceId, objects);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

// Inflating the layout for the custom Spinner
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom, parent, false);

// Declaring and Typecasting the textview in the inflated layout
            TextView tvLanguage = (TextView) layout
                    .findViewById(R.id.tvLanguage);

// Setting the text using the array
            tvLanguage.setText(Languages[position]);

// Setting the color of the text
            tvLanguage.setTextColor(Color.rgb(75, 180, 225));

// Declaring and Typecasting the imageView in the inflated layout
            ImageView img = (ImageView) layout.findViewById(R.id.imgLanguage);

// Setting an image using the id's in the array
            img.setImageResource(images[position]);

// Setting Special atrributes for 1st element
            if (position == 0) {
// Removing the image view
                img.setVisibility(View.GONE);
// Setting the size of the text
                tvLanguage.setTextSize(20f);
// Setting the text Color
                tvLanguage.setTextColor(Color.BLACK);

            }

            return layout;
        }

        // It gets a View that displays in the drop down popup the data at the specified position
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }
}