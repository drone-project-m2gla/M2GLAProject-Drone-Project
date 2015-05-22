package fr.m2gla.istic.projet.context;

import fr.m2gla.istic.projet.model.Mean;

/**
 * Created by david on 20/05/15.
 */
public interface ListAdapterCommand {
    public boolean refreshList();
    public boolean onValidateClick(Mean xtraMean, int position);
    public boolean onCancelClick(Mean xtraMean, int position);
}
