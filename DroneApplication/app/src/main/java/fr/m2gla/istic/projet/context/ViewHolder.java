package fr.m2gla.istic.projet.context;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mds on 20/05/15.
 */
    /* A Static class for holding the elements of each List View Item
         * This is created as per Google UI Guideline for faster performance */
public class ViewHolder {
    TextView itemLabelTxtView;
    ImageView imgImageView;
    ImageButton validerImageButton;
    ImageButton annullerImageButton;

    int position;
}
