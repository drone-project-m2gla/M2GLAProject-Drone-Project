package fr.m2gla.istic.projet.strategy.impl;

import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by baptiste on 09/04/15.
 */
public class StrategyIntervention implements Strategy {
    private static final Strategy INSTANCE = new StrategyIntervention();

    protected StrategyIntervention() {
        StrategyRegistery.getInstance().addStrategy(INSTANCE);
    }

    @Override
    public String getScopeName() {
        return "intervention";
    }

    @Override
    public void call(String massage) {
        //TODO execute code
    }
}
