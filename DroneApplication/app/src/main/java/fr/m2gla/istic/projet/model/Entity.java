package fr.m2gla.istic.projet.model;

/**
 * Created by baptiste on 10/04/15.
 * Classe de base des entités
 */
public class Entity {
    private String id;

    /**
     * Récupération de l'identifiant de l'entité courante
     * @return : Identifiant de l'entité
     */
    public String getId() {
        return id;
    }

    /**
     * Positionnement de l'identifiant de l'entité
     * @param id : Identifiant de l'entité
     */
    public void setId(String id) {
        this.id = id;
    }
}
