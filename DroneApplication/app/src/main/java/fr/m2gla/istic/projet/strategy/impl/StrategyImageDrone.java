package fr.m2gla.istic.projet.strategy.impl;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.model.GeoImage;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by baptiste on 26/05/15.
 */
public class StrategyImageDrone implements Strategy {
    private static StrategyImageDrone INSTANCE;
    private MapActivity activity;

    protected StrategyImageDrone() {}

    public void setActivity(MapActivity activity) {
        this.activity = activity;
    }

    public static StrategyImageDrone getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyImageDrone();
            // On s'abonne à la strategy
            StrategyRegistery.getInstance().addStrategy(INSTANCE);
        }
        return INSTANCE;
    }

    @Override
    public String getScopeName() {
        return "imageDrone";
    }

    @Override
    public Class<?> getType() {
        return GeoImage.class;
    }

    /**
     * Met a jour l'image du trajet
     * @param object GeoImage Image a afficher
     */
    @Override
    public void call(Object object) {
        if (activity != null) {
            activity.imageDrone((GeoImage)object);
        }
    }
}
