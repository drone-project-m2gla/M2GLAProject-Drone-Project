package fr.m2gla.istic.projet.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.m2gla.istic.projet.activity.R;

// Creating an Adapter Class
public class ItemsAdapter extends ArrayAdapter {

    private final Activity activity;
    private final Integer[] images;

    private String[] titles;

    public ItemsAdapter(Context context, int textViewResourceId,
                        String[] objects, Integer[] images) {
        super(context, textViewResourceId, objects);
        activity = (Activity) context;
        this.titles = objects;
        this.images = images;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        // Inflating the layout for the custom Spinner
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom, parent, false);

        // Declaring and Typecasting the textview in the inflated layout
        TextView tvLanguage = (TextView) layout
                .findViewById(R.id.tvLanguage);

        // Setting the text using the array
        tvLanguage.setText(titles[position]);

        // Setting the color of the text
        tvLanguage.setTextColor(Color.rgb(75, 180, 225));

        // Declaring and Typecasting the imageView in the inflated layout
        ImageView img = (ImageView) layout.findViewById(R.id.imgLanguage);

        // Setting an image using the id's in the array
        img.setImageResource(images[position]);

//        // Setting Special atrributes for 1st element
//        if (position == 0) {
//            // Removing the image view
//            img.setVisibility(View.GONE);
//            // Setting the size of the text
//            tvLanguage.setTextSize(20f);
//            // Setting the text Color
//            tvLanguage.setTextColor(Color.BLACK);
//
//        }

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