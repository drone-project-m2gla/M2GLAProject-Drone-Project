package fr.m2gla.istic.projet.strategy;

import android.content.Context;

/**
 * Created by baptiste on 09/04/15.
 */
public interface Strategy {
    public String getScopeName();
    public Class<?> getType();
    public void call(Object object);
}
