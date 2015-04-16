package fr.m2gla.istic.projet.observer;

/**
 * Created by baptiste on 16/04/15.
 */
public interface Observable {
    public void addObserver(ObserverTarget observer);
    public void removeObserver(ObserverTarget observer);
}
