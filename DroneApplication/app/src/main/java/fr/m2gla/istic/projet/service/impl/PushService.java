package fr.m2gla.istic.projet.service.impl;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;

import java.io.IOException;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.RestAPI;

public class PushService {
    private static final PushService INSTANCE = new PushService();
    private static final String TAG = "PushService";
    private static final String CLIENT_KEY = "AIzaSyBO1geWvgWqYwLQyOzNTdFPMHGCDHrGfPc";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private GoogleCloudMessaging gcm;
    private Context context;

    protected PushService() {}

    public PushService getInstance() {
        return INSTANCE;
    }

    public void setContext(Context context) {
        this.context = context;
    }

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
                    if (o instanceof Boolean) {
                        Boolean result = (Boolean)o;
                        if (result) {
                            RestServiceImpl.getInstance()
                                    .post(RestAPI.POST_PUSH_REGISTER, null, new Command() {
                                        @Override
                                        public void execute(HttpResponse response) {

                                        }
                                    }, null);
                        }
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
