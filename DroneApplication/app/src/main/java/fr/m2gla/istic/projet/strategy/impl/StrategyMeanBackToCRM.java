package fr.m2gla.istic.projet.strategy.impl;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.fragments.MoyensInitFragment;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by fernando on 26/05/15.
 */
public class StrategyMeanBackToCRM implements Strategy {

    //Instance de la stratégie pour singleton
    private static StrategyMeanBackToCRM INSTANCE;

    //Fragment qui contient les listes à mettre à jour
    private MoyensInitFragment meansListsFragment;

    /**
     * Patron singleton : constructeur, enregistrement et retour de l'instance
     *
     * @return instance de la stratégie
     */
    public static StrategyMeanBackToCRM getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyMeanBackToCRM();
            // On s'abonne à la strategy
            StrategyRegistery.getInstance().addStrategy(INSTANCE);
        }
        return INSTANCE;
    }

    /**
     * Définit la référence du fragment qui contient les listes à mettre à jour
     * @param meansListsFragment fragment avec les listes
     */
    public void setFragment(MoyensInitFragment meansListsFragment) {
        this.meansListsFragment = meansListsFragment;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getScopeName() {
        return "moyenAuCRM";
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
        if (meansListsFragment != null) {
            //mettre à jour les moyens sur la carte
            ((MapActivity) meansListsFragment.getActivity()).updateMeans();
            //mettre à jour la liste des moyens disponibles
            meansListsFragment.arrivedMeanStrategy((Mean) object);
        }
    }
}
