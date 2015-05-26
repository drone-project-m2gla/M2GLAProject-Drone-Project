package fr.m2gla.istic.projet.strategy.impl;

import android.util.Log;

import fr.m2gla.istic.projet.fragments.MoyensInitFragment;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by mds on 19/05/15.
 */
public class StrategyMeanFree implements Strategy {
    private static StrategyMeanFree INSTANCE;

    public void setFragment(MoyensInitFragment mapActivity) {
        this.map = mapActivity;
    }

    private MoyensInitFragment map;

    public StrategyMeanFree() {
    }

    @Override
    public String getScopeName() {
        return "moyenLibere";
    }

    @Override
    public Class<?> getType() {
        return Mean.class;
    }

    @Override
    public void call(Object object) {
        if (map != null) {
            map.movingMapMeanStrategy((Mean) object);
        }
    }

    public static StrategyMeanFree getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyMeanFree();
            // On s'abonne à la strategy
            StrategyRegistery.getInstance().addStrategy(INSTANCE);
        }
        return INSTANCE;
    }
}