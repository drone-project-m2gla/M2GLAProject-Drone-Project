package fr.m2gla.istic.projet.context;

import fr.m2gla.istic.projet.activity.R;

/**
 * Created by david on 08/04/15.
 * Classe contenant la définition des constantes de l'application androïd
 */
public interface GeneralConstants {
    // Codes retours des services REST
    public static final int     HTTP_RESP_OK = 200;

    // Titre de l'icon
    public static final String TITLE_DRONE_MARKER = "Checkpoint drone";
    public static final String TITLE_MOYEN_MARKER = "Action sur le moyen";

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
    public static final String  REF_ACT_LAT_IMG = "latitude";
    public static final String  REF_ACT_LON_IMG = "longitude";

    // Affichage en cas de moyen refusé
    public static final String  MEAN_REFUSED = "REFUSÉ";

    // Pour la boite de saisie du nom du moyen
    public static final String  MEAN_DIALOG_TITLE = "Nom ou désignation du moyen";
    public static final String  MEAN_DIALOG_OK_BUTTON = "Valider";
    public static final String  MEAN_DIALOG_CANCEL_BUTTON = "Abandonner";

    // Nom des fichiers svg
    public static final String SVG_VEHICULE_A_INCENDIE_SEUL = "vehicule_incendie_seul";
}
