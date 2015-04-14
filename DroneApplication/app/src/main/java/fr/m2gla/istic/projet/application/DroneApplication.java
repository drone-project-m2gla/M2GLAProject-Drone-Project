package fr.m2gla.istic.projet.application;

import android.app.Application;
import android.content.Context;

import fr.m2gla.istic.projet.service.impl.PushServiceImpl;

/**
 * Created by baptiste on 13/04/15.
 */
public class DroneApplication  extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();

        DroneApplication.context = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        PushServiceImpl.getInstance().unregister();

        super.onTerminate();
    }

    public static Context getAppContext() {
        return DroneApplication.context;
    }
}