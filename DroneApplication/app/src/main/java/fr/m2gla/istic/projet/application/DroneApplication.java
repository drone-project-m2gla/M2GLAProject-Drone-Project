package fr.m2gla.istic.projet.application;

import android.app.Application;
import android.content.Context;

import fr.m2gla.istic.projet.service.impl.PushServiceImpl;

/**
 * Created by baptiste on 13/04/15.
 */
public class DroneApplication  extends Application {
    private static Context context;


    /**
     * Methode Principale de l'application
     *
     */
    public void onCreate(){
        super.onCreate();

        DroneApplication.context = getApplicationContext();
    }


    /**
     * Methode de fin de l'application
     *
     */
    @Override
    public void onTerminate() {
        PushServiceImpl.getInstance().unregister();

        super.onTerminate();
    }


    /**
     * Methode retournant le contexte de l'application
     *
     * @return : Contexte de l'application
     */
    public static Context getAppContext() {
        return DroneApplication.context;
    }
}