package fr.m2gla.istic.projet.model;

import fr.m2gla.istic.projet.context.UserQualification;

/**
 * Created by baptiste on 11/04/15.
 * Définit le rôle d'un utilisateur
 */
public class PushEntity extends Entity {
    private UserQualification typeClient;

    /**
     * Récupération du rôle de l'utilisateur
     * @return : rôle de l'utilisateur
     */
    public UserQualification getTypeClient() {
        return typeClient;
    }

    /**
     * Renseignement du rôle de l'utilisateur
     * @param typeClient : rôle de l'utilisateur
     */
    public void setTypeClient(UserQualification typeClient) {
        this.typeClient = typeClient;
    }
}
