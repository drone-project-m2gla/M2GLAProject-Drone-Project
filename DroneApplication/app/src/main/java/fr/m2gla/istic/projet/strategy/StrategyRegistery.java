package fr.m2gla.istic.projet.strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baptiste on 09/04/15.
 */
public class StrategyRegistery {
    private static final String TAG = "StratRegister";
    private static StrategyRegistery INSTANCE;
    private List<Strategy> strategies;

    protected StrategyRegistery() {
        strategies = new ArrayList<>();
    }

    public static StrategyRegistery getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyRegistery();
        }
        return INSTANCE;
    }

    public void addStrategy(Strategy strategy) {
        strategies.add(strategy);
    }

    public List<Strategy> getStrategies() {
        return strategies;
    }
}
