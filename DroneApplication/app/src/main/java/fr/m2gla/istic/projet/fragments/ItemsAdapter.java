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

    //    // declare it final so that it could be accessed from the inner class
//    final ViewHolder holder;
//
//    if (convertView == null)
//    {
//        LayoutInflater mInflater = (LayoutInflater) SimpleAddressBookAdapter.context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        convertView = mInflater.inflate(SimpleAddressBookAdapter.resource, parent, false);
//
//        holder = new ViewHolder();
//        holder.textView = new TextView[fromList.length];
//
//        // get the textview's from the convertView
//        for (int i = 0; i < fromList.length; i++) {
//            holder.textView[i] = (TextView) convertView.findViewById(resourceList[i]);
//        }
//
//        // get the phoneIcon and emailIcon as well from convertView
//        holder.phoneIcon = (ImageView) convertView.findViewById(R.id.address_book_item_call);
//        holder.emailIcon = (ImageView) convertView.findViewById(R.id.address_book_item_email);
//
//        // add a listener for phone call
//        holder.phoneIcon.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                String phone = SimpleAddressBookAdapter.listMap.get(holder.position).get("phone");
//                ActivityHelper.startActivity(ActivityHelper.PHONE_CALL, phone);
//            }
//
//        });
//
//        // add listener for email
//        holder.emailIcon.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                String email = SimpleAddressBookAdapter.listMap.get(holder.position).get("email");
//                ActivityHelper.startActivity(ActivityHelper.EMAIL, email);
//            }
//
//        });
//
//        // store it in a Tag as its the first time this view is generated
//        convertView.setTag(holder);
//
//    }
//    else {
//			/* get the View from the existing Tag */
//        holder = (ViewHolder) convertView.getTag();
//    }
//
//		/* update the textView's text for this list view item/element */
//    for (int i = 0; i < fromList.length; i++) {
//        holder.textView[i].setText(listMap.get(position).get(fromList[i]));
//    }
//
//    // store the position/index for this list view element/item
//    holder.position = position;
    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

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
                Drawable drawable = images[position];
                Log.i(TAG, "drawable  " + position + "   is   " + (drawable == null));
                Log.i(TAG, "image  " + position + "   is   " + (images[position] == null));
                Bitmap src = SVGAdapter.convertDrawableToBitmap(drawable, 64, 64);
                Log.i(TAG, "src  " + position + "   is   " + (src == null));
                Bitmap image = Bitmap.createScaledBitmap(src, 50, 50, true);
                holder.imgImageView.setImageBitmap(image);
            }
        }

//        // Inflating the layout for the custom Spinner
//        LayoutInflater inflater = activity.getLayoutInflater();
//        View layout = inflater.inflate(this.customLayout, parent, false);
//
//        // Declaring and Typecasting the textview in the inflated layout
//        TextView itemLabelTxtView = (TextView) layout
//                .findViewById(R.id.tvLanguage);
//
//        // Setting the text using the array
//        itemLabelTxtView.setText(titles[position]);
//
//        // Setting the color of the text
//        itemLabelTxtView.setTextColor(Color.rgb(75, 180, 225));
//
//        // Declaring and Typecasting the imageView in the inflated layout
//        ImageView imgImageView = (ImageView) layout.findViewById(R.id.imgLanguage);
//
//
//        // Setting Special attributes for 1st element
//        if (position == 0 && titles[position].equals("")) {
//            // Removing the image view
//            imgImageView.setVisibility(View.GONE);
//            // Setting the size of the text
//            itemLabelTxtView.setTextSize(20f);
//            // Setting the text Color
//            itemLabelTxtView.setTextColor(Color.WHITE);
//            // Setting the value
//            itemLabelTxtView.setText("Sélectionner un moyen supp.");
//
//        } else {
//            Drawable drawable = images[position];
//            Log.i(TAG, "drawable  " + position + "   is   " + (drawable == null));
//            Log.i(TAG, "image  " + position + "   is   " + (images[position] == null));
//            Bitmap src = SVGAdapter.convertDrawableToBitmap(drawable, 64, 64);
//            Log.i(TAG, "src  " + position + "   is   " + (src == null));
//            Bitmap image = Bitmap.createScaledBitmap(src, 50, 50, true);
//            imgImageView.setImageBitmap(image);
//        }
//        return layout;
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