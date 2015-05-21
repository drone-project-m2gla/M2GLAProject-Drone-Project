package fr.m2gla.istic.projet.strategy.impl;

import android.util.Log;

import fr.m2gla.istic.projet.fragments.MoyensInitFragment;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by mds on 19/05/15.
 */
public class StrategyMeanMovingMap implements Strategy {
    private static final String TAG = "Strategy moy. dispo";
    private static StrategyMeanMovingMap INSTANCE;

    public void setFragment(MoyensInitFragment mapActivity) {
        this.map = mapActivity;
        Log.i(TAG, "Setter");
    }

    private MoyensInitFragment map;

    public StrategyMeanMovingMap() {
    }

    @Override
    public String getScopeName() {
        return "moyenMove";
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

    public static StrategyMeanMovingMap getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyMeanMovingMap();
            // On s'abonne à la strategy
            StrategyRegistery.getInstance().addStrategy(INSTANCE);
        }
        return INSTANCE;
    }
}
