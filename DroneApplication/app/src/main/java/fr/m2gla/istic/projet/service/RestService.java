package fr.m2gla.istic.projet.service;

import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.command.Command;


public interface RestService {
    /**
     * Send get http method
     * @param service Service REST call
     * @param callback Callback after response
     * @param param (Optional) Param of query
     */
    public void get(final String service, final Command callback, final Map<String, String> param);
    /**
     * Send post http method
     * @param service Service REST call
     * @param content Content of request
     * @param callback Callback after response
     * @param param (Optional) Param of query
     */
    public void post(final String service, final List<NameValuePair> content, final Command callback, final Map<String, String> param);
    /**
     * Send put http method
     * @param service Service REST call
     * @param content Content of request
     * @param callback Callback after response
     * @param param (Optional) Param of query
     */
    public void put(final String service, final List<NameValuePair> content, final Command callback, final Map<String, String> param);
    /**
     * Send delete http method
     * @param service Service REST call
     * @param callback Callback after response
     * @param param (Optional) Param of query
     */
    public void delete(final String service, final Command callback, final Map<String, String> param);
}
