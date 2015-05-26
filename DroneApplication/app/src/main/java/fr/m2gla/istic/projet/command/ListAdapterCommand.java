package fr.m2gla.istic.projet.command;

import fr.m2gla.istic.projet.model.Mean;

/**
 * Created by david on 20/05/15.
 */
public interface ListAdapterCommand {
    /**
     * Methode de raffraichissement
     * @deprecated
     * @return true si pas de problème, false sinon
     */
    public boolean refreshList();


    /**
     * Methode d'action après clic sur le bouton validation dans une liste
     * @return true si pas de problème, false sinon
     */
    public boolean onValidateClick(Mean xtraMean, int position);


    /**
     * Methode d'action après clic sur le bouton d'annulation dans une liste
     * @return true si pas de problème, false sinon
     */
    public boolean onCancelClick(Mean xtraMean, int position);
}
