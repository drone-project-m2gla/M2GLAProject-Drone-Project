package fr.m2gla.istic.projet.service;

import android.content.Context;

import fr.m2gla.istic.projet.context.UserQualification;

/**
 * Created by baptiste on 08/04/15.
 * Interface pour le service de notification
 */
public interface PushService {
    /**
     * Resgister on Google Cloud Messages
     */
    public void register(UserQualification typeClient);
    /**
     * Unesgister on Google Cloud Messages
     */
    public void unregister();
}
