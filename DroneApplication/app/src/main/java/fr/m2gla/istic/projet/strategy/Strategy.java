package fr.m2gla.istic.projet.strategy;

/**
 * Created by baptiste on 09/04/15.
 */
public interface Strategy {
    public String getScopeName();
    public void call(String massage);
}
