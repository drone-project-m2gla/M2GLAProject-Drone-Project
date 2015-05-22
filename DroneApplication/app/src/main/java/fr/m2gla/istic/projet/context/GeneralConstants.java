package fr.m2gla.istic.projet.context;

import fr.m2gla.istic.projet.activity.R;

/**
 * Created by david on 08/04/15.
 */
public interface GeneralConstants {
    // Codes retours des services REST
    public static final int     HTTP_RESP_OK = 200;

    // References des éléments de la liste d'intervention
    public static final String  INTER_LIST_MEAN = "mean";
    public static final String  INTER_LIST_LABEL = "label";
    public static final String  INTER_LIST_DATE = "date";
    public static final String  INTER_LIST_ID = "id";
    public static final String  INTER_LIST_CODE = "code";
    public static final String  INTER_LIST_DATA = "data";
    public static final String  INTER_LIST_SELECT = "check";

    // Designation de la sélection/désélection sous forme de chaine
    public static final String  UNSELECT_DESC_STR = "O";
    public static final String  SELECT_DESC_STR = "X";

    // References des éléments transitant entre les Activity
    public static final String  REF_ACT_ROLE = "role";
    public static final String  REF_ACT_IDINTER = "idIntervention";

    // Designation des colonnes de la table des moyens
    public static final String  MEAN_TABLE_1 = "VEHICULES";
    public static final String  MEAN_TABLE_2 = "DEMANDE";
    public static final String  MEAN_TABLE_3 = "DECLENCHE";
    public static final String  MEAN_TABLE_4 = "ARRIVE";
    public static final String  MEAN_TABLE_5 = "ENGAGE";
    public static final String  MEAN_TABLE_6 = "LIBERE";
    public static final String  MEAN_REFUSED = "REFUSE";

    // Nom des fichiers svg
    public static final String SVG_COLONNE_INCENDIE = "colonne_incendie";
    public static final String SVG_GROUPE_INCENDIE = "groupe_incendie";
    public static final String SVG_SECOURS_A_PERSONNE = "secours_a_personnes";
    public static final String SVG_VEHICULE_A_INCENDIE_SEUL = "vehicule_incendie_seul";
    public static final String SVG_VEHICULE_POST_COMMAND = "poste_commandement_prevu";

    // Nom des moyens correspondants aux fichiers svg
    public static final String VALUE_COLONNE_INCENDIE = "Colonne incendie";
    public static final String VALUE_GROUPE_INCENDIE = "Groupe incendie";
    public static final String VALUE_MOYEN_INTERVENTION_AERIEN = "Moyen aérien";
    public static final String VALUE_SECOURS_A_PERSONNE = "Secours à personne";
    public static final String VALUE_VEHICULE_A_INCENDIE = "Véhicule incendie";

    public static final String VALUE_VEHICULE_VSAV = "VSAV";
    public static final String VALUE_VEHICULE_VSR = "VSR";
    public static final String VALUE_VEHICULE_VLCG = "VLCG";
    public static final String VALUE_VEHICULE_EPA = "EPA";
    public static final String VALUE_VEHICULE_FPT = "FPT";


    // Text
    public static final int DRAWABLE_VEHICULE_VSAV = R.mipmap.ic_vehicule_vsav;
    public static final int DRAWABLE_VEHICULE_VSR = R.mipmap.ic_vehicule_vsr;
    public static final int DRAWABLE_VEHICULE_VLCG = R.mipmap.ic_vehicule_vlcg;
    public static final int DRAWABLE_VEHICULE_EPA = R.mipmap.ic_vehicule_epa;
    public static final int DRAWABLE_VEHICULE_FPT = R.mipmap.ic_vehicule_fpt;

    // Image
    public static final int DRAWABLE_IMG_VEHICULE_VSAV = R.mipmap.ic_vehicule_img_vsav;
    public static final int DRAWABLE_IMG_VEHICULE_VSR = R.mipmap.ic_vehicule_img_vsr;
    public static final int DRAWABLE_IMG_VEHICULE_VLCG = R.mipmap.ic_vehicule_img_vlcg;
    public static final int DRAWABLE_IMG_VEHICULE_EPA = R.mipmap.ic_vehicule_img_epa;
    public static final int DRAWABLE_IMG_VEHICULE_FPT = R.mipmap.ic_vehicule_img_fpt;

    // image type svg variable
    public static final String TYPE_IMG = "type";
}
