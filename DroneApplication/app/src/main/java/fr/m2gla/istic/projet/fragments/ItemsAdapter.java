package fr.m2gla.istic.projet.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import fr.m2gla.istic.projet.activity.R;

// Creating an Adapter Class
public class ItemsAdapter extends ArrayAdapter {

    private final Activity activity;
    private final String[] images;

    private String[] titles;

    public ItemsAdapter(Context context, int textViewResourceId,
                        String[] objects, String[] images) {
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
        TextView itemLabelTxtView = (TextView) layout
                .findViewById(R.id.tvLanguage);

        // Setting the text using the array
        itemLabelTxtView.setText(titles[position]);

        // Setting the color of the text
        itemLabelTxtView.setTextColor(Color.rgb(75, 180, 225));

        // Declaring and Typecasting the imageView in the inflated layout
        ImageView imgImageView = (ImageView) layout.findViewById(R.id.imgLanguage);

        SVG svg = null;
        try {
            int imageResource = activity.getResources().getIdentifier(images[position],
                    "raw", activity.getPackageName());
            Log.i("sow", "ressource value\t " + images[position] + "\t++++++++++++++  " + imageResource);

            if (imageResource == 0) {
                // Get default image.
                imageResource = R.raw.prise_eau_perenne;
            }
            svg = SVG.getFromResource(activity, imageResource);
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            Bitmap image = Bitmap.createScaledBitmap(convertToBitmap(drawable, 64, 64), 50, 50, true);
            imgImageView.setImageBitmap(image);
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
        // Setting Special atrributes for 1st element
        if (position == 0 && titles[position].equals("")) {
            // Removing the image view
            imgImageView.setVisibility(View.GONE);
            // Setting the size of the text
            itemLabelTxtView.setTextSize(20f);
            // Setting the text Color
            itemLabelTxtView.setTextColor(Color.WHITE);
            // Setting the value
            itemLabelTxtView.setText("Sélectionner un moyen supp.");

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

    /**
     * Converts a drawable object into a bitmap
     *
     * @param drawable     drawable object to convert
     * @param widthPixels  output image width
     * @param heightPixels output image height
     * @return converted bitmap
     */
    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }
}