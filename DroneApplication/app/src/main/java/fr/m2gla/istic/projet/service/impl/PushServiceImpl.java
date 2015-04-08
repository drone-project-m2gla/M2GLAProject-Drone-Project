package fr.m2gla.istic.projet.service.impl;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.service.PushService;

public class PushServiceImpl implements PushService {
    private static final PushService INSTANCE = new PushServiceImpl();
    private static final String TAG = "PushService";
    private static final String CLIENT_KEY = "AIzaSyBO1geWvgWqYwLQyOzNTdFPMHGCDHrGfPc";

    private GoogleCloudMessaging gcm;
    private Context context;

    protected PushServiceImpl() {}

    public static PushService getInstance() {
        return INSTANCE;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void register() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);

            (new AsyncTask() {
                private String registeredId;

                @Override
                protected Object doInBackground(Object[] params) {
                    try {
                        registeredId = gcm.register(CLIENT_KEY);
                    } catch (IOException e) {
                        Log.e(TAG + ".AsyncTask", "Error register");
                        return false;
                    }

                    return true;
                }

                @Override
                protected void onPostExecute(Object o) {
                    final Boolean result = (Boolean)o;
                    if (result) {
                        List<NameValuePair> content = new ArrayList<NameValuePair>();
                        content.add(new BasicNameValuePair("id", registeredId));

                        RestServiceImpl.getInstance()
                            .post(RestAPI.POST_PUSH_REGISTER, content, new Command() {
                                    @Override
                                    public void execute(HttpResponse response) {
                                        if (response.getStatusLine().getStatusCode() != 204) {
                                            Log.e(TAG, "Erreur register");
                                        }
                                    }
                                }, null);
                    }
                }
            }).execute();
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        if (context == null) {
            throw new IllegalArgumentException("Context not set");
        }
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }
}
