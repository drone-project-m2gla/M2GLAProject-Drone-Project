package fr.m2gla.istic.projet.observer;

/**
 * Created by baptiste on 16/04/15.
 */
public interface ObserverTarget {
    public void notifySend();
    public void notifyClear();
    public void notifyClose();
}
