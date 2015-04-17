package fr.m2gla.istic.projet.strategy.impl;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.model.Position;
import fr.m2gla.istic.projet.strategy.Strategy;

/**
 * Created by baptiste on 16/04/15.
 */
public class StrategyMoveDrone implements Strategy {
    public static StrategyMoveDrone INSTANCE;

    private MapActivity activity;

    public StrategyMoveDrone() {
    }

    public static StrategyMoveDrone getINSTANCE() {
        return INSTANCE;
    }

    public void setActivity(MapActivity activity) {
        this.activity = activity;
    }

    @Override
    public String getScopeName() {
        return "droneMove";
    }

    @Override
    public Class<?> getType() {
        return Position.class;
    }

    @Override
    public void call(Object object) {
        if (activity != null) {
            activity.moveDrone((Position) object);
        }
    }
}
