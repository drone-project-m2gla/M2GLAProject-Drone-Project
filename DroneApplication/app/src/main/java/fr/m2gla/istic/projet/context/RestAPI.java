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

}
