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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.application.DroneApplication;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.context.UserQualification;
import fr.m2gla.istic.projet.model.Entity;
import fr.m2gla.istic.projet.model.PushEntity;
import fr.m2gla.istic.projet.service.PushService;

/**
 * Définition du service de notification
 */
public class PushServiceImpl implements PushService {
    private static final PushService INSTANCE = new PushServiceImpl();
    private static final String TAG = "PushService";
    private static final String SENDER_ID = "836789679656";

    private Context context;
    private String registeredId;

    /**
     * Constructeur
     */
    protected PushServiceImpl() {
        context = DroneApplication.getAppContext();
    }

    /**
     * Builder
     * @return : instance de la classe
     */
    public static PushService getInstance() {
        return INSTANCE;
    }

    /**
     * Abonnement au service de notification
     * @param typeClient : Nom et Type du client
     */
    @Override
    public void register(final UserQualification typeClient) {
        final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);

        if (gcm != null && checkPlayServices()) {
            (new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    try {
                        Log.i(TAG + ".AsyncTask", "Registration start");
                        registeredId = gcm.register(SENDER_ID);
                        Log.i(TAG + ".AsyncTask", "Registration id " + registeredId);
                    } catch (IOException e) {
                        Log.e(TAG + ".AsyncTask", "Error register", e);
                        return false;
                    }

                    return true;
                }

                @Override
                protected void onPostExecute(Object o) {
                    final Boolean result = (Boolean)o;
                    if (result) {
                        PushEntity entity = new PushEntity();
                        entity.setId(registeredId);
                        entity.setTypeClient(typeClient);

                        RestServiceImpl.getInstance().post(RestAPI.POST_PUSH_REGISTER, null, entity, Void.class,
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Log.i(TAG, "Register success");
                                    }
                                },
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Log.e(TAG, "Error unregister code " + ((Exception)response).getMessage());
                                    }
                                });
                    }
                }
            }).execute();
        }
    }

    /**
     * Désabonnement au service de notification
     */
    @Override
    public void unregister() {
        final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);

        if (gcm != null && checkPlayServices()) {
            (new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    try {
                        Log.i(TAG + ".AsyncTask", "Unregistration start");
                        gcm.unregister();
                        Log.i(TAG + ".AsyncTask", "Unregistration end");
                    } catch (IOException e) {
                        Log.e(TAG + ".AsyncTask", "Error register", e);
                        return false;
                    }

                    return true;
                }

                @Override
                protected void onPostExecute(Object o) {
                    final Boolean result = (Boolean)o;
                    if (result) {
                        Map<String, String> param = new HashMap<String, String>();
                        param.put("id", registeredId);

                        RestServiceImpl.getInstance().delete(RestAPI.DELETE_PUSH_REGISTER, param,
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Log.i(TAG, "Unregister success");
                                    }
                                },
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Log.e(TAG, "Error unregister code " + ((Exception)response).getMessage());
                                    }
                                });
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

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.e(TAG, "Google not available");
            } else {
                Log.wtf(TAG, "This device is not supported.");
            }
        }
        return resultCode == ConnectionResult.SUCCESS;
    }
}
