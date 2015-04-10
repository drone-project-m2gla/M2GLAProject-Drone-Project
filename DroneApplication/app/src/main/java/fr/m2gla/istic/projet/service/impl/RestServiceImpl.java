package fr.m2gla.istic.projet.service.impl;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.service.RestService;

public class RestServiceImpl implements RestService {
    private static final String TAG = "RestServiceImpl";
    private static final RestService INSTANCE = new RestServiceImpl();
    private static final String URL = "http://projm2gla1.istic.univ-rennes1.fr:8080/sitserver/rest";

    protected RestServiceImpl() {}

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
                } catch (HttpClientErrorException e) {
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
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                try {
                    T result = null;

                    if (param == null) {
                        restTemplate.postForObject(URL + service, content, type);
                    } else {
                        restTemplate.postForObject(URL + service, content, type, param);
                    }

                    return result;
                } catch (HttpClientErrorException e) {
                    Log.e(TAG, "Error http " + e.getMessage());
                    error = true;
                    return e;
                }
                catch (HttpServerErrorException e) {
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
    public <T> void delete(final String service, final Map<String, String> param, final Class<T> type, final Command callbackSuccess, final Command callbackError) {
        (new AsyncTask() {
            private boolean error = false;

            @Override
            protected Object doInBackground(Object[] params) {
                //TODO delete
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
