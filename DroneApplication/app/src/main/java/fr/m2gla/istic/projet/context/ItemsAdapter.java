package fr.m2gla.istic.projet.context;

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
    private Intervention intervention;
    private ArrayList<String> myList = null;
    private ArrayList<ViewHolder> vhList = new ArrayList<ViewHolder>();
    private String idIntervention = null;
    private List<Mean> meanList = null;
    private Mean xtraMean;
    private ListAdapterCommand adapterCommand = null;

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

    public ItemsAdapter(Context context, int textViewResourceId,
                        String[] objects, Drawable[] images, ListAdapterCommand cmd) {
        this(context, textViewResourceId, objects, images);
        this.adapterCommand = cmd;
    }

    public ItemsAdapter(Context context, int textViewResourceId, ArrayList<String> objects,
                        Drawable[] images, String idInter, List<Mean> xtMeanList) {
        super(context, textViewResourceId, objects);
        this.activity = (Activity) context;
        this.myList = objects;
        this.idIntervention = idInter;
        this.meanList = xtMeanList;
        this.titles = objects.toArray(new String[objects.size()]);
        this.images = images;
        customLayout = textViewResourceId;
    }

    public ItemsAdapter(Context context, int textViewResourceId, ArrayList<String> objects,
                        Drawable[] images, String idInter, List<Mean> xtMeanList, ListAdapterCommand cmd) {
        this(context, textViewResourceId, objects, images, idInter, xtMeanList);
        this.adapterCommand = cmd;
    }


    public void remove(int position) {
        String[] newTitles;
        Drawable[] newImage;
        int i, sz;


        if (position >= this.titles.length) {
            return;
        }

        if (this.adapterCommand != null) {
            this.adapterCommand.refreshList();
            return;
        }

        // NOTE : LE CODE SUIVANT DOIT ETRE SUPPRIME !
        Toast.makeText(getContext(), "Remove : " + position, Toast.LENGTH_LONG).show();
        Log.i("itemsAdapter", "Remove : " + position);
        Log.i("itemsAdapter", "Remove (List) : " + this.myList.get(position));
        Log.i("itemsAdapter", "Remove (Mean): " + this.getMeanInList(position).getVehicle());
        Log.i("itemsAdapter", "Remove (Titl): " + this.titles[position]);
        Log.i("itemsAdapter", "Remove (VHol): " + this.vhList.get(position).itemLabelTxtView.getText().toString() + " (" + this.vhList.get(position).position + ")");

        sz = this.titles.length - 1;
        newTitles = new String[sz];


        for (i = 0; i < position; i++) {
            newTitles[i] = this.titles[i];
        }

        for (; i < newTitles.length; i++) {
            newTitles[i] = this.titles[i + 1];
        }

        this.titles = newTitles;

        newImage = new Drawable[this.images.length - 1];

        for (i = 0; i < position; i++) {
            newImage[i] = this.images[i];
        }

        for (; i < newImage.length; i++) {
            newImage[i] = this.images[i + 1];
        }

        this.images = newImage;

        // Changement veritable
        if (this.myList != null) {
            this.myList.remove(position);
        }
        if (this.meanList != null) {
            int realp = getMeanPositionInList(position);
            if (realp >= 0) {
                this.meanList.remove(realp);
            }
        }

        // Changement pour l'affichage
        for (i = position; i < this.vhList.size() - 1; i++) {
            ViewHolder vha = this.vhList.get(i);
            ViewHolder vhb = this.vhList.get(i + 1);


            vha.itemLabelTxtView = vhb.itemLabelTxtView;
            vha.imgImageView = vhb.imgImageView;

            vha.annullerImageButton = vhb.annullerImageButton;
            vha.validerImageButton = vhb.validerImageButton;

            // Setting the text using the array
            vha.itemLabelTxtView.setText(titles[i]);
            // Setting the color of the text
            vha.itemLabelTxtView.setTextColor(Color.rgb(75, 180, 225));
            // Setting the size of the text
            vha.itemLabelTxtView.setTextSize(20f);

            Drawable drawable = images[i];
            Bitmap src = SVGAdapter.convertDrawableToBitmap(drawable, 64, 64);
            Bitmap image = Bitmap.createScaledBitmap(src, 50, 50, true);
            vha.imgImageView.setImageBitmap(image);

            vha.position = i;
        }
        this.vhList.remove(this.vhList.size() - 1);

        this.notifyDataSetChanged();
    }

    private Mean getMeanInList(int position) {
        if (this.meanList == null) {
            return (null);
        }

        int i = 0;
        for (Mean m : this.meanList) {

            if (!m.getIsDeclined()) {
                if (i == position) {
                    return (m);
                } else if (i > position) {
                    return (null);
                }
                i++;
            }
        }

        return (null);
    }

    private int getMeanPositionInList(int position) {
        if (this.meanList == null) {
            return (-1);
        }

        int i = 0, j = 0;
        for (Mean m : this.meanList) {

            if (!m.getIsDeclined()) {
                if (i == position) {
                    return (j);
                } else if (i > position) {
                    return (-1);
                }
                i++;
            }
            j++;
        }

        return (-1);
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

            holder.itemLabelTxtView = (TextView) convertView.findViewById(R.id.tvLanguage);
            // get the phoneIcon and emailIcon as well from convertView
            holder.imgImageView = (ImageView) convertView.findViewById(R.id.imgLanguage);

            holder.annullerImageButton = (ImageButton) convertView.findViewById(R.id.annuler);
            holder.validerImageButton = (ImageButton) convertView.findViewById(R.id.valid);

            vhList.add(position, holder);


            if ((holder.annullerImageButton != null) && (this.meanList != null) && (this.idIntervention != null)) {

                holder.annullerImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ItemsAdapter.this.xtraMean = ItemsAdapter.this.getMeanInList(position);
                        if (ItemsAdapter.this.xtraMean == null) return;

                        map.put("idintervention", idIntervention);
                        //Toast.makeText(getContext(), "button annuler " + position, Toast.LENGTH_LONG).show();

                        RestServiceImpl.getInstance()
                                .post(RestAPI.POST_ANNULLER_MOYEN, map, xtraMean, String.class,
                                        new Command() {
                                            @Override
                                            public void execute(Object response) {
                                                //Toast.makeText(getContext(), "Moyen annulé\nID mean: " + xtraMean.getId(), Toast.LENGTH_LONG).show();
                                                Log.i("itemsAdapter", "Moyen annulé : " + position);
                                                ItemsAdapter.this.remove(position);
                                            }
                                        }, new Command() {
                                            @Override
                                            public void execute(Object response) {
                                                Toast.makeText(getContext(), "Moyen n'a pas été annulé\nID mean: " + xtraMean.getId(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                    }
                });

                holder.validerImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ItemsAdapter.this.xtraMean = ItemsAdapter.this.getMeanInList(position);
                        if (ItemsAdapter.this.xtraMean == null) return;

                        map.put("idintervention", idIntervention);
                        RestServiceImpl.getInstance()
                                .post(RestAPI.POST_VALIDER_MOYEN, map, xtraMean, String.class,
                                        new Command() {
                                            @Override
                                            public void execute(Object response) {
                                                //Toast.makeText(getContext(), "Moyen validé\nID mean: " + xtraMean.getId(), Toast.LENGTH_LONG).show();
                                                Log.i("itemsAdapter", "Moyen validé : " + position);
                                                ItemsAdapter.this.remove(position);
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

            // Log.i(TAG, "Position not 0");

            // Setting the text using the array
            holder.itemLabelTxtView.setText(titles[position]);
            // Setting the color of the text
            holder.itemLabelTxtView.setTextColor(Color.rgb(75, 180, 225));
            // Setting the size of the text
            holder.itemLabelTxtView.setTextSize(20f);

            Drawable drawable = images[position];
            Bitmap src = SVGAdapter.convertDrawableToBitmap(drawable, 64, 64);
            Bitmap image = Bitmap.createScaledBitmap(src, 50, 50, true);
            holder.imgImageView.setImageBitmap(image);

            holder.position = position;
            convertView.setTag(holder);
        } else {
            /* get the View from the existing Tag */
            holder = (ViewHolder) convertView.getTag();
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
        }

        return convertView;

    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // Permettre de preciser la commande apres creation
    public void setAdapterCommand(ListAdapterCommand adapterCommand) {
        this.adapterCommand = adapterCommand;
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