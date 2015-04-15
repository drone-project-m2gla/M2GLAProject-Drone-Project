package fr.m2gla.istic.projet.context;

/**
 * Created by david on 08/04/15.
 */
public interface GeneralConstants {
    // Codes retours des services REST
    public static final int     HTTP_RESP_OK = 200;

    // References des éléments de la liste d'intervention
    public static final String  INTER_LIST_MEAN = "mean";
    public static final String  INTER_LIST_ID = "id";
    public static final String  INTER_LIST_CODE = "code";
    public static final String  INTER_LIST_DATA = "data";

    // References des éléments transitant entre les Activity
    public static final String  REF_ACT_ROLE = "role";

    // Id de l'intervention à transmettre à d'autres activités
    public static final String ID_INTERVENTION = "idIntervention";
}
