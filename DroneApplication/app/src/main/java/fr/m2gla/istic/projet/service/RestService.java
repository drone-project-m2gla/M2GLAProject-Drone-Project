package fr.m2gla.istic.projet.service;

import java.util.Map;

import fr.m2gla.istic.projet.command.Command;


/**
 * Interface les services REST
 */
public interface RestService {
    /**
     * Send get http method
     * @param service Service REST call (available in RestAPI)
     * @param param (Optional) Param of query, or null
     * @param type Type of result
     * @param callbackSuccess Callback after response success
     * @param callbackError Callback after response error
     */
    public <T> void get(String service, Map<String, String> param, Class<T> type, Command callbackSuccess, Command callbackError);
    /**
     * Send post http method
     * @param service Service REST call (available in RestAPI)
     * @param param (Optional) Param of query, or null
     * @param type Type of result
     * @param content Content of request
     * @param callbackSuccess Callback after response success
     * @param callbackError Callback after response error
     */
    public <T> void post(String service, Map<String, String> param, Object content, Class<T> type, Command callbackSuccess, Command callbackError);
    /**
     * Send put http method
     * @param service Service REST call (available in RestAPI)
     * @param param (Optional) Param of query, or null
     * @param type Type of result
     * @param content Content of request
     * @param callbackSuccess Callback after response success
     * @param callbackError Callback after response error
     */
    public <T> void put(String service, Map<String, String> param, Object content, Class<T> type, Command callbackSuccess, Command callbackError);
    /**
     * Send delete http method
     * @param service Service REST call (available in RestAPI)
     * @param param (Optional) Param of query, or null
     * @param callbackSuccess Callback after response success
     * @param callbackError Callback after response error
     */
    public <T> void delete(String service, Map<String, String> param, Command callbackSuccess, Command callbackError);
}
