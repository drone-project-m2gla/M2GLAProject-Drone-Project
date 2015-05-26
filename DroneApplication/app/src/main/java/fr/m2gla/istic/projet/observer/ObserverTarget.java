package fr.m2gla.istic.projet.observer;

/**
 * Created by baptiste on 16/04/15.
 * Interface pour les classes observant d'autres classes
 */
public interface ObserverTarget {
    /**
     * Notification d'emission
     */
    public void notifySend();

    /**
     * Notification d'annulation
     */
    public void notifyClear();

    /**
     * Notation de fermeture
     */
    public void notifyClose();
}
