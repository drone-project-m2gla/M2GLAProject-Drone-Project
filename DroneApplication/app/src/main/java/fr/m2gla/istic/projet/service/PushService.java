package fr.m2gla.istic.projet.service;

import android.content.Context;

/**
 * Created by baptiste on 08/04/15.
 */
public interface PushService {
    /**
     * Set context of application
     * @param context Context of application
     */
    public void setContext(Context context);
    /**
     * Resgister on Google Cloud Messages
     */
    public void register();
    /**
     * Unesgister on Google Cloud Messages
     */
    public void unregister();
}
