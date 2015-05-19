package fr.m2gla.istic.projet.strategy.impl;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by baptiste on 18/05/15.
 */
public class StrategyMeanMove implements Strategy {
    private static StrategyMeanMove INSTANCE;
    private MapActivity activity;

    public StrategyMeanMove() {}

    public static StrategyMeanMove getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyMeanMove();
            // On s'abonne à la strategy
            StrategyRegistery.getInstance().addStrategy(INSTANCE);
        }
        return INSTANCE;
    }

    public void setActivity(MapActivity activity) {
        this.activity = activity;
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
        if (activity != null) {
            activity.posMean((Mean) object);
        }
    }
}
