package fr.m2gla.istic.projet.strategy.impl;

import android.util.Log;

import fr.m2gla.istic.projet.fragments.MoyensInitFragment;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by mds on 19/05/15.
 * Stratégie de notification de la validation des moyens par le Codis
 */
public class StrategyCodisValidateMean implements Strategy {
    private static final String TAG = "StrategyCodisValidMean";
    private static StrategyCodisValidateMean INSTANCE;

    /**
     *
     * @param moyensInitFragment
     */
    public void setFragment(MoyensInitFragment moyensInitFragment) {
        this.moyensInitFragment = moyensInitFragment;
        Log.i(TAG, "Setter");
    }

    private MoyensInitFragment moyensInitFragment;

    /**
     * Constructeur
     */
    public StrategyCodisValidateMean() {
    }

    @Override
    public String getScopeName() {
        return "ok";
    }

    @Override
    public Class<?> getType() {
        return Mean.class;
    }

    @Override
    public void call(Object object) {
        if (moyensInitFragment != null) {
            moyensInitFragment.updateMeanRequestView((Mean) object);
        }
    }

    public static StrategyCodisValidateMean getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyCodisValidateMean();
            // On s'abonne à la strategy
            StrategyRegistery.getInstance().addStrategy(INSTANCE);
        }
        return INSTANCE;
    }
}
