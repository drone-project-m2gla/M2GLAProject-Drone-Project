package fr.m2gla.istic.projet.strategy.impl;

import android.util.Log;

import fr.m2gla.istic.projet.fragments.MoyensInitFragment;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by mds on 19/05/15.
 */
public class StrategyMeanSupplAdd implements Strategy {
    private static final String TAG = "StrategyMeanSupplAdd";
    private static StrategyMeanSupplAdd INSTANCE;

    public void setFragment(MoyensInitFragment mapActivity) {
        this.map = mapActivity;
        Log.i(TAG, "setter");
    }

    private MoyensInitFragment map;

    public StrategyMeanSupplAdd() {
    }

    @Override
    public String getScopeName() {
        return "xtra";
    }

    @Override
    public Class<?> getType() {
        return Mean.class;
    }

    @Override
    public void call(Object object) {
        if (map != null) {
            map.demandMeanStrategy((Mean) object);
            Log.i(TAG, "Call");
        }
    }

    public static StrategyMeanSupplAdd getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyMeanSupplAdd();
            // On s'abonne à la strategy
            StrategyRegistery.getInstance().addStrategy(INSTANCE);
        }
        return INSTANCE;
    }
}
