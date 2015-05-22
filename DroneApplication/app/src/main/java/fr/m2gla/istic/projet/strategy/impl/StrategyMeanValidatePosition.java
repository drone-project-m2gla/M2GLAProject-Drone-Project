package fr.m2gla.istic.projet.strategy.impl;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by fernando on 19/05/15.
 */
public class StrategyMeanValidatePosition implements Strategy {
    private static StrategyMeanValidatePosition INSTANCE;
    private MapActivity activity;

    public StrategyMeanValidatePosition() {}

    public static StrategyMeanValidatePosition getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyMeanValidatePosition();
            // On s'abonne à la strategy registery
            StrategyRegistery.getInstance().addStrategy(INSTANCE);
        }
        return INSTANCE;
    }

    public void setActivity(MapActivity activity) {
        this.activity = activity;
    }

    @Override
    public String getScopeName() {
        return "moyenValide";
    }

    @Override
    public Class<?> getType() {
        return Mean.class;
    }

    @Override
    public void call(Object object) {
        if (activity != null) {
            activity.updateMeans();
        }
    }
}
