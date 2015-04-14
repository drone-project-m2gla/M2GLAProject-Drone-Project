package fr.m2gla.istic.projet.service.impl;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.service.RestService;

public class RestServiceImpl implements RestService {
    private static final String TAG = "RestServiceImpl";
    private static final RestService INSTANCE = new RestServiceImpl();
    private static final String URL = "http://projm2gla1.istic.univ-rennes1.fr:8080/sitserver/rest";

    protected RestServiceImpl() {
    }

    public static RestService getInstance() {
        return INSTANCE;
    }

    @Override
    public <T> void get(final String service, final Map<String, String> param, final Class<T> type, final Command callbackSuccess, final Command callbackError) {
        (new AsyncTask() {
            private boolean error = false;

            @Override
            protected Object doInBackground(Object[] params) {

                RestTemplate restTemplate = new RestTemplate();

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

    @Override
    public <T> void post(final String service, final Map<String, String> param, final Object content, final Class<T> type, final Command callbackSuccess, final Command callbackError) {
        (new AsyncTask() {
            private boolean error = false;

            @Override
            protected Object doInBackground(Object[] params) {

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.set("Connection", "Close");
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);


                RestTemplate restTemplate = new RestTemplate();
                HttpEntity<String> entity = new HttpEntity<String>(new Gson().toJson((T) content), requestHeaders);

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpEntity<T> result = null;
                try {
                    if (param == null) {
                        System.out.println(URL.toString()+" - " + service.toString());
                        System.out.println(new Gson().toJson((T) content));
                        System.out.println(type.toString());
                        result = restTemplate.exchange(URL + service, HttpMethod.POST, entity, type);

                    } else {
                        result = restTemplate.exchange(URL + service, HttpMethod.POST, entity, type, param);
                    }
                    // Log.i(TAG, "Object \t" + result);

                    return result;
                } catch (HttpStatusCodeException e) {
                    Log.e(TAG, "Error http " + e.getMessage());
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

    @Override
    public <T> void delete(final String service, final Map<String, String> param, final Command callbackSuccess, final Command callbackError) {
        (new AsyncTask() {
            private boolean error = false;

            @Override
            protected Object doInBackground(Object[] params) {
                RestTemplate restTemplate = new RestTemplate();

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
