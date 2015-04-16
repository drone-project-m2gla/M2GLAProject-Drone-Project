package fr.m2gla.istic.projet.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.model.SVGAdapter;

// Creating an Adapter Class
public class ItemsAdapter extends ArrayAdapter {

    private final Activity activity;
    private Drawable[] images;

    private final static String TAG = "ItemsAdapter";

    private String[] titles;
    private int customLayout;

    public ItemsAdapter(Context context, int textViewResourceId,
                        String[] objects, Drawable[] images) {
        super(context, textViewResourceId, objects);
        activity = (Activity) context;
        this.titles = objects;
        Log.i(TAG, "MeanXtra Title\t" + this.titles.length);
        this.images = images;
        Log.i(TAG, "MeanXtra Image\t" + this.images.length);
        customLayout = textViewResourceId;
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater mInflater = activity.getLayoutInflater();

            convertView = mInflater.inflate(this.customLayout, parent, false);

            holder = new ViewHolder();

            holder.itemLabelTxtView = (TextView) convertView
                    .findViewById(R.id.tvLanguage);
            // get the phoneIcon and emailIcon as well from convertView
            holder.imgImageView = (ImageView) convertView.findViewById(R.id.imgLanguage);

            // Setting Special attributes for 1st element

            Log.i(TAG, "Position not 0");

            // Setting the text using the array
            holder.itemLabelTxtView.setText(titles[position]);//
            // Setting the color of the text
            holder.itemLabelTxtView.setTextColor(Color.rgb(75, 180, 225));
            // Setting the size of the text
            holder.itemLabelTxtView.setTextSize(20f);

            Drawable drawable = images[position];
            Bitmap src = SVGAdapter.convertDrawableToBitmap(drawable, 64, 64);
            Bitmap image = Bitmap.createScaledBitmap(src, 50, 50, true);
            holder.imgImageView.setImageBitmap(image);

            holder.position = position;
        } else {
            /* get the View from the existing Tag */
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;

    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    /* A Static class for holding the elements of each List View Item
     * This is created as per Google UI Guideline for faster performance */
    private static class ViewHolder {
        TextView itemLabelTxtView;
        ImageView imgImageView;

        int position;
    }
}