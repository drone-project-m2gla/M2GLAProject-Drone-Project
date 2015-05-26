package fr.m2gla.istic.projet.context;

/**
 * Interface to list rest service API for application
 */
public interface RestAPI {
    // Push register block
    public static final String POST_PUSH_REGISTER = "/gcm/register";
    public static final String DELETE_PUSH_REGISTER = "/gcm/unregister/{id}";

    // Login block
    public static final String POST_PUSH_LOGIN = "/user/login";

    // Intervention block
    public static final String GET_ALL_INTERVENTION = "/intervention";
    public static final String POST_INTERVENTION = "/intervention";
    public static final String POST_POSITION_CONFIRMATION = "/intervention/{id}/moyen/emplace";
    public static final String POST_POSITION_MOVE = "/intervention/{id}/moyen/positionner";
    public static final String POST_RELEASE = "/intervention/{id}/moyen/libere";
    public static final String POST_RETOURCRM = "/intervention/{id}/moyen/retourcrm";

    // topographie block
    public static final String GET_ALL_TOPOGRAPHIE = "/topographie/1/1/1";

    // Demande d'un moyen supplémentaire /intervention/idIntervention/xtra
    public static final String POST_SEND_MEAN_REQUEST = "/intervention/{id}/moyenextra";

    //Demande d'une intervention avec id
    public static final String GET_INTERVENTION = "/intervention/{id}";

    // ajouter moyen supplementaire
    public static final String POST_VALIDER_MOYEN = "/moyen/{idintervention}/ok";

    // annuller moyen supplementaire
    public static final String POST_ANNULLER_MOYEN = "/moyen/{idintervention}/nok";

    // drone block
    public static final String POST_POSITION_DRONE = "/drone/move";
    public static final String POST_PARCOURS_DRONE = "/drone/target";

    // get des moyens extra d'une intervention
    public static final String GET_MOYENS_DISPO = "/intervention/{id}/moyen";

    // Valider l'arrivée d'un moyen au CRM
    public static final String POST_VALIDER_ARRIVEE_MOYEN = "/intervention/{id}/moyen/arrive";

    // Valider la libération du moyen
    public static final String POST_VALIDER_LIBERATION_MOYEN = "/intervention/{id}/moyen/libere";

//    // get des moyens dispo d'une intervention
//    public static final String GET_MOYENS_DISPO1 = "/intervention/{id}/moyen";

    // get images
    public static final String GET_IMAGES = "/images";
}
