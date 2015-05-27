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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.ListAdapterCommand;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;


/**
 * Personnalisation de la classe ArrayAdapter
 */
public class ItemsAdapter extends ArrayAdapter {

    private final Activity activity;
    private Drawable[] images;

    private final static String TAG = "ItemsAdapter";

    private String[] titles;
    private int customLayout;
    private Intervention intervention;
    private ArrayList<String> myList = null;
    private String idIntervention = null;
    private List<Mean> meanList = null;
    private Mean xtraMean;
    private ListAdapterCommand adapterCommand = null;


    /**
     * Constructeur de la classe
     * @param context :
     * @param textViewResourceId
     * @param objects
     * @param images
     */
    public ItemsAdapter(Context context, int textViewResourceId, String[] objects, Drawable[] images) {
        super(context, textViewResourceId, objects);
        activity = (Activity) context;
        this.titles = objects;
        Log.i(TAG, "MeanXtra Title\t" + this.titles.length);
        this.images = images;
        Log.i(TAG, "MeanXtra Image\t" + this.images.length);
        customLayout = textViewResourceId;
    }

    /**
     * Variante du constructeur de la classe
     * @param context
     * @param textViewResourceId
     * @param objects
     * @param images
     * @param cmd
     */
    public ItemsAdapter(Context context, int textViewResourceId,
                        String[] objects, Drawable[] images, ListAdapterCommand cmd) {
        this(context, textViewResourceId, objects, images);
        this.adapterCommand = cmd;
    }

    /**
     * Variante du constructeur de la classe
     * @param context
     * @param textViewResourceId
     * @param objects
     * @param images
     * @param idInter
     * @param xtMeanList
     */
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

    /**
     * Variante du constructeur de la classe
     * @param context
     * @param textViewResourceId
     * @param objects
     * @param images
     * @param idInter
     * @param xtMeanList
     * @param cmd
     */
    public ItemsAdapter(Context context, int textViewResourceId, ArrayList<String> objects,
                        Drawable[] images, String idInter, List<Mean> xtMeanList, ListAdapterCommand cmd) {
        this(context, textViewResourceId, objects, images, idInter, xtMeanList);
        this.adapterCommand = cmd;
    }


    /**
     * Methode de rafraichissement de la liste
     * @deprecated
     * @param position
     */
    public void remove(int position) {
        String[] newTitles;
        Drawable[] newImage;
        int i, sz;


        if (position >= this.titles.length) {
            return;
        }

        if (this.adapterCommand != null) {
            this.adapterCommand.refreshList();
        }

        // Pas de fonction de suppression par defaut
    }

    /**
     * Recupère le moyen correspondant à la position fournie dans la liste affichée
     * @param position : position dans le liste
     * @return : Moyen trouvé
     */
    private Mean getMeanInList(int position) {
        if (this.meanList == null) {
            return (null);
        }

        int i = 0;
        for (Mean m : this.meanList) {

            if (!m.refusedMeans()) {
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

    /**
     * Récupère l'index du moyen dans liste des moyens en fonction de la position dans liste
     * affichée
     * @param position
     * @return
     */
    private int getMeanPositionInList(int position) {
        if (this.meanList == null) {
            return (-1);
        }

        int i = 0, j = 0;
        for (Mean m : this.meanList) {

            if (!m.refusedMeans()) {
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

    /**
     * Réécriture de la méthode getView afin de personnaliser l'affichage des lignes de la liste
     * @param position : Position dans la liste
     * @param convertView : vue affectée à la ligne de la liste
     * @param parent : groupe de vues parents
     * @return : Vue courante
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    /**
     * Réécriture effective de la methode getView()
     * @param position : Position dans la liste
     * @param convertView : vue affectée à la ligne de la liste
     * @param parent : groupe de vues parents
     * @return : Vue courante
     */
    public View getCustomView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
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


            if ((holder.annullerImageButton != null) && (this.meanList != null) && (this.idIntervention != null)) {

                holder.annullerImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ItemsAdapter.this.xtraMean = ItemsAdapter.this.getMeanInList(position);
                        if (ItemsAdapter.this.xtraMean == null) return;

                        if (adapterCommand != null) {
                            adapterCommand.onCancelClick(ItemsAdapter.this.xtraMean, position);
                        }
                    }
                });

                holder.validerImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ItemsAdapter.this.xtraMean = ItemsAdapter.this.getMeanInList(position);
                        if (ItemsAdapter.this.xtraMean == null) return;

                        if (adapterCommand != null) {
                            adapterCommand.onValidateClick(ItemsAdapter.this.xtraMean, position);
                        }
                    }
                });
            }

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

    /**
     * Réécriture de la methode
     * @param position : Position dans la liste
     * @param convertView : vue affectée à la ligne de la liste
     * @param parent : groupe de vues parents
     * @return : Vue courante
     */
    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    /**
     * Methode permettant de specifier la commande apres creation
     * @param adapterCommand : Classe permettant de specifier les actions a effectuer en
     *                       fonction des boutons sélectionnes dans la liste
     */
    public void setAdapterCommand(ListAdapterCommand adapterCommand) {
        this.adapterCommand = adapterCommand;
    }
}