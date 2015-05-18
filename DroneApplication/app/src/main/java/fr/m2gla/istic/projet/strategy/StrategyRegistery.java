package fr.m2gla.istic.projet.strategy;

import android.util.Log;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import java.util.ArrayList;
import java.util.List;

import fr.m2gla.istic.projet.strategy.impl.StrategyIntervention;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanMove;
import fr.m2gla.istic.projet.strategy.impl.StrategyMoveDrone;

/**
 * Created by baptiste on 09/04/15.
 */
public class StrategyRegistery {
    private static final String TAG = "StratRegister";
    private static StrategyRegistery INSTANCE;
    private List<Strategy> strategies;

    protected StrategyRegistery() {
        strategies = new ArrayList<>();
        // Add strategy impl
        strategies.add(StrategyIntervention.getINSTANCE());
        strategies.add(StrategyMoveDrone.getINSTANCE());
        strategies.add(StrategyMeanMove.getINSTANCE());
    }

    public static StrategyRegistery getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyRegistery();
        }
        return INSTANCE;
    }

    public List<Strategy> getStrategies() {
        return strategies;
    }
}
