package fr.m2gla.istic.projet.strategy.impl;

import fr.m2gla.istic.projet.fragments.MoyensInitFragment;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by mds on 19/05/15.
 * <p/>
 * Classe qui met en place la stratégie lorsqu'un moyen est arrivée sur le CRM
 */
public class StrategyMeanArrived implements Strategy {
    private static StrategyMeanArrived INSTANCE;

    public void setFragment(MoyensInitFragment mapActivity) {
        this.map = mapActivity;
    }

    private MoyensInitFragment map;

    public StrategyMeanArrived() {
    }

    @Override
    public String getScopeName() {
        return "moyenArrive";
    }

    @Override
    public Class<?> getType() {
        return Mean.class;
    }

    @Override
    public void call(Object object) {
        if (map != null) {
            map.transitMeanStrategy((Mean) object);
        }
    }

    public static StrategyMeanArrived getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyMeanArrived();
            // On s'abonne à la strategy
            StrategyRegistery.getInstance().addStrategy(INSTANCE);
        }
        return INSTANCE;
    }
}
