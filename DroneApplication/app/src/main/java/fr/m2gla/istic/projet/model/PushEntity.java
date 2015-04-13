package fr.m2gla.istic.projet.model;

import fr.m2gla.istic.projet.context.UserQualification;

/**
 * Created by baptiste on 11/04/15.
 */
public class PushEntity extends Entity {
    private UserQualification typeClient;

    public UserQualification getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(UserQualification typeClient) {
        this.typeClient = typeClient;
    }
}
