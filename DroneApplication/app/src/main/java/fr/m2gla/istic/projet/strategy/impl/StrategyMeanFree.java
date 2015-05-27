package fr.m2gla.istic.projet.strategy.impl;

import fr.m2gla.istic.projet.fragments.MoyensInitFragment;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.strategy.Strategy;
import fr.m2gla.istic.projet.strategy.StrategyRegistery;

/**
 * Created by fernando on 26/05/15.
 * Strategy pour synchroniser la libération d'un moyen
 */
public class StrategyMeanFree implements Strategy {

    //Instance de la stratégie pour singleton
    private static StrategyMeanFree INSTANCE;

    //Fragment qui contient les listes à mettre à jour
    private MoyensInitFragment meansListsFragment;

    /**
     * Patron singleton : constructeur, enregistrement et retour de l'instance
     *
     * @return instance de la stratégie
     */
    public static StrategyMeanFree getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyMeanFree();
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
     * @return
     */
    @Override
    public String getScopeName() {
        return "moyenLibere";
    }


    /**
     * @inheritDoc
     * @return
     */
    @Override
    public Class<?> getType() {
        return Mean.class;
    }

    /**
     * @inheritDoc
     * Dans ce cas particulier mise à jour des listes des moyens disponibles et en transit
     * @param object objet transmis
     */
    @Override
    public void call(Object object) {
        if (meansListsFragment != null) {
            meansListsFragment.movingMapMeanStrategy((Mean) object);
            meansListsFragment.transitMeanStrategy((Mean) object);
        }
    }
}
