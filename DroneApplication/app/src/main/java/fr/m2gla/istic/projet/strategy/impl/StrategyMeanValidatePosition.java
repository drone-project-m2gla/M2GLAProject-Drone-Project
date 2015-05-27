package fr.m2gla.istic.projet.strategy.impl;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.fragments.MoyensInitFragment;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by fernando on 19/05/15.
 */
public class StrategyMeanValidatePosition implements Strategy {

    //Instance de la stratégie pour singleton
    private static StrategyMeanValidatePosition INSTANCE;

    //Activité android qui contient la carte avec les moyens à mettre à jour
    private MapActivity activity;

    /**
     * Patron singleton : constructeur, enregistrement et retour de l'instance
     *
     * @return instance de la stratégie
     */
    public static StrategyMeanValidatePosition getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyMeanValidatePosition();
            // On s'abonne à la strategy registery
            StrategyRegistery.getInstance().addStrategy(INSTANCE);
        }
        return INSTANCE;
    }

    /**
     * Définit la référence de l'activité qui contient la carte
     * @param activity activité avec la carte
     */
    public void setActivity(MapActivity activity) {
        this.activity = activity;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getScopeName() {
        return "moyenValide";
    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<?> getType() {
        return Mean.class;
    }

    /**
     * @inheritDoc
     * Dans ce cas particulier mise à jour des moyens sur la carte et sur la liste des moyens disponibles
     * @param object objet transmis
     */
    @Override
    public void call(Object object) {
        if (activity != null) {
            //mettre à jour les moyens sur la carte
            activity.updateMeans();

            //mettre à jour la liste des moyens disponibles
            ((MoyensInitFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_moyens_init)).movingMapMeanStrategy((Mean) object);
        }
    }
}
