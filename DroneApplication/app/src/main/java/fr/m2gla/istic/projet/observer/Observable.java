package fr.m2gla.istic.projet.observer;

/**
 * Created by baptiste on 16/04/15.
 * Interface pour les classes observable
 */
public interface Observable {
    /**
     * Ajoute un observateur
     * @param observer : Nouvel observateur
     */
    public void addObserver(ObserverTarget observer);

    /**
     * Supprime un observateur
     * @param observer : Observateur à supprimer
     */
    public void removeObserver(ObserverTarget observer);
}
