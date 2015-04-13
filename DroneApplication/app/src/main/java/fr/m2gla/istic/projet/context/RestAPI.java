package fr.m2gla.istic.projet.context;

/**
 * Interface to list rest service API for application
 */
public interface RestAPI {
    // Push register block
    public static final String POST_PUSH_REGISTER = "/register";
    public static final String DELETE_PUSH_REGISTER = "/register/{id}";

    // Login block
    public static final String POST_PUSH_LOGIN = "/user/login";

    // Intervention block
    public static final String GET_ALL_INTERVENTION = "/intervention";
    public static final String POST_INTERVENTION = "/intervention";

    // topographie block
    public static final String GET_ALL_TOPOGRAPHIE = "/topographie/1/1/1";
}
