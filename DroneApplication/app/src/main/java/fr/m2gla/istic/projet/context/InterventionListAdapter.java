package fr.m2gla.istic.projet.context;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import fr.m2gla.istic.projet.activity.R;

/**
 * Created by david on 15/04/15.
 * Personnalisation de la classe SimpleAdapter
 */
public class InterventionListAdapter extends SimpleAdapter {
    private static final String TAG = "InterListAdapter";

    //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private LayoutInflater                  localInflater;

    // Une liste d'interventions
    private List<? extends Map<String, ?>>  localListIntervention;


    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public InterventionListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        // Recuperer les donnees indispensables
        this.localInflater = LayoutInflater.from(context);
        this.localListIntervention = data;
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
        View            curentView;
        LinearLayout    layoutItem;


        //(1) : Réutilisation des layouts
        if (convertView == null) {
            //Initialisation de notre item à partir du  layout XML "personne_layout.xml"
            convertView = localInflater.inflate(R.layout.disp_intervention, parent, false);
        }
        layoutItem = (LinearLayout) convertView;


        //(2) : Récupération des TextView de notre layout
        TextView tvMean = (TextView)layoutItem.findViewById(R.id.interventionNewMean);
        TextView tvLabel = (TextView)layoutItem.findViewById(R.id.interventionLabel);

        //(3) Changement de la couleur de nos item
        String  nbMean = (String) this.localListIntervention.get(position).get(GeneralConstants.INTER_LIST_MEAN);
        String  selLine = (String) this.localListIntervention.get(position).get(GeneralConstants.INTER_LIST_SELECT);

        // Changement de couleur pour le nombre de moyens en attente
        if (nbMean.compareTo("[0]") != 0) {
            tvMean.setBackgroundColor(Color.RED);
            tvMean.setTextColor(Color.YELLOW);
        }
        else {
            tvMean.setBackgroundColor(Color.TRANSPARENT);
            tvMean.setTextColor(Color.WHITE);
        }

        // Changement de couleur pour le label lorsqu'il est selectionne
        if (selLine.compareTo(GeneralConstants.SELECT_DESC_STR) == 0) {
            tvLabel.setTextColor(Color.YELLOW);
        }
        else {
            tvLabel.setTextColor(Color.LTGRAY);
        }

        //(4) : Appel a la methode d'origine
        curentView = super.getView(position, convertView, parent);

        return (curentView);
    }
}
