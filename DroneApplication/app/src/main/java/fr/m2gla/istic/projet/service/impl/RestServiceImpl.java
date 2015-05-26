package fr.m2gla.istic.projet.service.impl;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.service.RestService;

/**
 * Définition des services REST
 */
public class RestServiceImpl implements RestService {
    private static final String TAG = "RestServiceImpl";
    private static final RestService INSTANCE = new RestServiceImpl();

    private static final String URL = "http://projm2gla1backup.istic.univ-rennes1.fr:8080/sitserver/rest";
//    private static final String URL = "http://148.60.14.207:8088/sitserver/rest";
//    private static final String URL = "http://projm2gla1int.istic.univ-rennes1.fr:58089/sitserver/rest";


    /**
     * Constructeur
     */
    protected RestServiceImpl() {
    }

    /**
     * Builder
     * @return : Instance de la classe
     */
    public static RestService getInstance() {
        return INSTANCE;
    }

    /**
     * Service GET
     * @param service Service REST call (available in RestAPI)
     * @param param (Optional) Param of query, or null
     * @param type Type of result
     * @param callbackSuccess Callback after response success
     * @param callbackError Callback after response error
     * @param <T>
     */
    @Override
    public <T> void get(final String service, final Map<String, String> param, final Class<T> type, final Command callbackSuccess, final Command callbackError) {
        (new AsyncTask() {
            private boolean error = false;

            @Override
            protected Object doInBackground(Object[] params) {

                RestTemplate restTemplate = new RestTemplate();

                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                T result = null;
                try {
                    if (param == null) {
                        result = restTemplate.getForObject(URL + service, type);
                    } else {
                        result = restTemplate.getForObject(URL + service, type, param);
                    }
                } catch (HttpStatusCodeException e) {
                    Log.e(TAG, "Error http " + e.getMessage());
                    error = true;
                    return e;
                } catch (HttpMessageNotReadableException e) {
                    Log.e(TAG, "Error http " + e.getMessage());
                    error = true;
                    return e;
                } catch (Exception e) {
                    Log.e(TAG, "Error " + e.getMessage());
                    error = true;
                    return e;
                }

                return result;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (error) {
                    callbackError.execute(o);
                } else {
                    callbackSuccess.execute(o);
                }
            }
        }).execute();
    }

    /**
     * Service POST
     * @param service Service REST call (available in RestAPI)
     * @param param (Optional) Param of query, or null
     * @param content Content of request
     * @param type Type of result
     * @param callbackSuccess Callback after response success
     * @param callbackError Callback after response error
     * @param <T>
     */
    @Override
    public <T> void post(final String service, final Map<String, String> param, final Object content, final Class<T> type, final Command callbackSuccess, final Command callbackError) {
        (new AsyncTask() {
            private boolean error = false;

            @Override
            protected Object doInBackground(Object[] params) {
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                try {
                    T result = null;

                    if (param == null) {
                        result = restTemplate.postForObject(URL + service, content, type);
                    } else {
                        result = restTemplate.postForObject(URL + service, content, type, param);
                    }
                    Log.i(TAG, "Object \t" + result);

                    return result;
                } catch (HttpStatusCodeException e) {
                    Log.e(TAG, "Error http " + e.getMessage());
                    error = true;
                    return e;
                } catch (Exception e) {
                    Log.e(TAG, "Error " + e.getMessage());
                    error = true;
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Object o) {
                if (error) {
                    callbackError.execute(o);
                } else {
                    callbackSuccess.execute(o);
                }
            }
        }).execute();
    }

    /**
     * Service PUT
     * @param service Service REST call (available in RestAPI)
     * @param param (Optional) Param of query, or null
     * @param content Content of request
     * @param type Type of result
     * @param callbackSuccess Callback after response success
     * @param callbackError Callback after response error
     * @param <T>
     */
    @Override
    public <T> void put(final String service, final Map<String, String> param, final Object content, final Class<T> type, final Command callbackSuccess, final Command callbackError) {
        (new AsyncTask() {
            private boolean error = false;

            @Override
            protected Object doInBackground(Object[] params) {
                //TODO put
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (error) {
                    callbackError.execute(o);
                } else {
                    callbackSuccess.execute(o);
                }
            }
        }).execute();
    }

    /**
     * Service DELETE
     * @param service Service REST call (available in RestAPI)
     * @param param (Optional) Param of query, or null
     * @param callbackSuccess Callback after response success
     * @param callbackError Callback after response error
     * @param <T>
     */
    @Override
    public <T> void delete(final String service, final Map<String, String> param, final Command callbackSuccess, final Command callbackError) {
        (new AsyncTask() {
            private boolean error = false;

            @Override
            protected Object doInBackground(Object[] params) {
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                try {
                    if (param == null) {
                        restTemplate.delete(URL + service);
                    } else {
                        restTemplate.delete(URL + service, param);
                    }
                } catch (HttpStatusCodeException e) {
                    Log.e(TAG, "Error http " + e.getMessage());
                    error = true;
                    return e;
                } catch (Exception e) {
                    Log.e(TAG, "Error " + e.getMessage());
                    error = true;
                    return e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (error) {
                    callbackError.execute(o);
                } else {
                    callbackSuccess.execute(o);
                }
            }
        }).execute();
    }
}
