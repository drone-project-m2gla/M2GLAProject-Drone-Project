package fr.m2gla.istic.projet.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by baptiste on 13/04/15.
 */
public class DroneApplication  extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        DroneApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return DroneApplication.context;
    }
}