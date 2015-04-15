package fr.m2gla.istic.projet.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.model.SVGAdapter;

// Creating an Adapter Class
public class ItemsAdapter extends ArrayAdapter {

    private final Activity activity;
    private final Drawable[] images;

    private final static String TAG = "ItemsAdapter";

    private String[] titles;
    private int customLayout;

    public ItemsAdapter(Context context, int textViewResourceId,
                        String[] objects, Drawable[] images) {
        super(context, textViewResourceId, objects);
        activity = (Activity) context;
        this.titles = objects;
        this.images = images;
        customLayout = textViewResourceId;
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        // Inflating the layout for the custom Spinner
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(this.customLayout, parent, false);

        // Declaring and Typecasting the textview in the inflated layout
        TextView itemLabelTxtView = (TextView) layout
                .findViewById(R.id.tvLanguage);

        // Setting the text using the array
        itemLabelTxtView.setText(titles[position]);

        // Setting the color of the text
        itemLabelTxtView.setTextColor(Color.rgb(75, 180, 225));

        // Declaring and Typecasting the imageView in the inflated layout
        ImageView imgImageView = (ImageView) layout.findViewById(R.id.imgLanguage);
        // Setting Special attributes for 1st element
        if (position == 0 && titles[position].equals("")) {
            // Removing the image view
            imgImageView.setVisibility(View.GONE);
            // Setting the size of the text
            itemLabelTxtView.setTextSize(20f);
            // Setting the text Color
            itemLabelTxtView.setTextColor(Color.WHITE);
            // Setting the value
            itemLabelTxtView.setText("Sélectionner un moyen supp.");

        } else {
            Bitmap image = Bitmap.createScaledBitmap(SVGAdapter.convertDrawableToBitmap(images[position], 64, 64), 50, 50, true);
            imgImageView.setImageBitmap(image);
        }

        Button valid = (Button)layout.findViewById(R.id.valid);
        if (valid != null){
            valid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext()," button a la position  " + position,Toast.LENGTH_LONG).show();

                }
            });
        }
        //Button annuler = (Button)layout.findViewById(R.id.annuler);

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