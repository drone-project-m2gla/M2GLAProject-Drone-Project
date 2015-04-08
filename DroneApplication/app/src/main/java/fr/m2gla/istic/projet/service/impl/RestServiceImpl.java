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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.service.RestService;

public class RestServiceImpl implements RestService {
    private static final String TAG = "fr.m2gla.istic.projet.service.impl.RestServiceImpl";
    private static final RestService INSTANCE = new RestServiceImpl();
    private static final String URL = "";

    protected RestServiceImpl() {}

    public static RestService getInstance() {
        return INSTANCE;
    }

    @Override
    public void get(final String service, final Command callback, final Map<String, String> param) {
        (new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                HttpGet httpGet = new HttpGet(addParam(URL + service, param));

                return  call(httpGet);
            }

            @Override
            protected void onPostExecute(Object o) {
                callback.execute((HttpResponse)o);
            }
        }).execute();
    }

    @Override
    public void post(final String service, final List<NameValuePair> content, final Command callback, final Map<String, String> param) {
        (new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                HttpPost httpPost = new HttpPost(addParam(URL + service, param));
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(content));
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                return  call(httpPost);
            }

            @Override
            protected void onPostExecute(Object o) {
                callback.execute((HttpResponse)o);
            }
        }).execute();
    }

    @Override
    public void put(final String service, final List<NameValuePair> content, final Command callback, final Map<String, String> param) {
        (new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                HttpPut httpPut = new HttpPut(addParam(URL + service, param));
                try {
                    httpPut.setEntity(new UrlEncodedFormEntity(content));
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                return  call(httpPut);
            }

            @Override
            protected void onPostExecute(Object o) {
                callback.execute((HttpResponse)o);
            }
        }).execute();
    }

    @Override
    public void delete(final String service, final Command callback, final Map<String, String> param) {
        (new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                HttpDelete httpGet = new HttpDelete(addParam(URL + service, param));

                return  call(httpGet);
            }

            @Override
            protected void onPostExecute(Object o) {
                callback.execute((HttpResponse)o);
            }
        }).execute();
    }

    /**
     * Add param of url
     * @param url Base url
     * @param param param of query
     * @return Url with param
     */
    private String addParam(String url, Map<String, String> param) {
        if (param == null || param.isEmpty()) {
            return url;
        }

        int i = 0;
        String result = url + '?';

        for (String key : param.keySet()) {
            result += key + '=' + param.get(key) + ((param.size() < i) ? '&' : "");
            i++;
        }
        return result;
    }

    /**
     * Call http method
     * @param method Method http
     * @return Response http
     */
    private HttpResponse call(HttpRequestBase method) {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(method);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return  httpResponse;
    }
}
