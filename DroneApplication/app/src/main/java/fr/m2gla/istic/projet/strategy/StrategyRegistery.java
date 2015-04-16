package fr.m2gla.istic.projet.strategy;

import java.util.ArrayList;
import java.util.List;

import fr.m2gla.istic.projet.strategy.impl.StrategyIntervention;
import fr.m2gla.istic.projet.strategy.impl.StrategyMoveDrone;

/**
 * Created by baptiste on 09/04/15.
 */
public class StrategyRegistery {
    private static final String TAG = "StratRegister";
    private static final StrategyRegistery INSTANCE = new StrategyRegistery();
    private List<Strategy> strategies;

    protected StrategyRegistery() {
        strategies = new ArrayList<>();
        // Add strategy impl
        strategies.add(new StrategyIntervention());
    }

    public static StrategyRegistery getInstance() {
        return INSTANCE;
    }

    public List<Strategy> getStrategies() {
        return strategies;
    }
}
