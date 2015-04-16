package fr.m2gla.istic.projet.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.SVGAdapter;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

// Creating an Adapter Class
public class ItemsAdapter extends ArrayAdapter {

    private final Activity activity;
    private Drawable[] images;

    private final static String TAG = "ItemsAdapter";

    private String[] titles;
    private int customLayout;
    Intervention intervention;

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

    public View getCustomView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        Mean mean;
        final Map<String, String> map = new HashMap<>();

        if (convertView == null) {
            LayoutInflater mInflater = activity.getLayoutInflater();

            convertView = mInflater.inflate(this.customLayout, parent, false);

            holder = new ViewHolder();

            holder.itemLabelTxtView = (TextView) convertView
                    .findViewById(R.id.tvLanguage);
            // get the phoneIcon and emailIcon as well from convertView
            holder.imgImageView = (ImageView) convertView.findViewById(R.id.imgLanguage);

            holder.annullerImageButton = (ImageButton) convertView.findViewById(R.id.annuler);
            holder.validerImageButton = (ImageButton) convertView.findViewById(R.id.valid);


            if (holder.annullerImageButton != null) {
                InterventionDetailFragment interventionFragment = (InterventionDetailFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_intervention_detail);

                final String idIntervention = interventionFragment.getIdIntervention();
                final Mean xtraMean = interventionFragment.getMeanXtra(position);

                holder.annullerImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        map.put("idintervention", idIntervention);
                        Toast.makeText(getContext(), "button annuller " + position, Toast.LENGTH_LONG).show();

                        RestServiceImpl.getInstance()
                                .post(RestAPI.POST_ANNULLER_MOYEN, map, xtraMean, Intervention.class,
                                        new Command() {
                                            @Override
                                            public void execute(Object response) {
                                                Toast.makeText(getContext(), "Moyen annuller\nID mean: " + xtraMean.getId(), Toast.LENGTH_LONG).show();
                                            }
                                        }, new Command() {
                                            @Override
                                            public void execute(Object response) {
                                                Toast.makeText(getContext(), "Moyen pas été annuller\nID mean: " + xtraMean.getId(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                    }
                });

                holder.validerImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        map.put("idintervention", idIntervention);
                        RestServiceImpl.getInstance()
                                .post(RestAPI.POST_VALIDER_MOYEN, map, xtraMean, Intervention.class,
                                        new Command() {
                                            @Override
                                            public void execute(Object response) {
                                                Toast.makeText(getContext(), "Moyen validé\nID mean: " + xtraMean.getId(), Toast.LENGTH_LONG).show();
                                            }
                                        }, new Command() {
                                            @Override
                                            public void execute(Object response) {
                                                Toast.makeText(getContext(), "Moyen n'a pas été validé\nID mean: " + xtraMean.getId(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                    }
                });
            }
            // Setting Special attributes for 1st element
            if (position == 0 && titles[position].equals("")) {
                // Removing the image view
                holder.imgImageView.setVisibility(View.GONE);
                // Setting the size of the text
                holder.itemLabelTxtView.setTextSize(20f);
                // Setting the text Color
                holder.itemLabelTxtView.setTextColor(Color.WHITE);
                // Setting the value
                holder.itemLabelTxtView.setText("Sélectionner un moyen supp.");

            } else {
                // Setting the text using the array
                holder.itemLabelTxtView.setText(titles[position]);
                Drawable drawable = images[position];
                Log.i(TAG, "drawable  " + position + "   is   " + (drawable == null));
                Log.i(TAG, "image  " + position + "   is   " + (images[position] == null));
                Bitmap src = SVGAdapter.convertDrawableToBitmap(drawable, 64, 64);
                Log.i(TAG, "src  " + position + "   is   " + (src == null));
                Bitmap image = Bitmap.createScaledBitmap(src, 50, 50, true);
                holder.imgImageView.setImageBitmap(image);
            }
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
        ImageButton validerImageButton;
        ImageButton annullerImageButton;

        int position;
    }

}