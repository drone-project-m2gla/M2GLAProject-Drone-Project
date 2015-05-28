package fr.m2gla.istic.projet.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baptiste on 16/04/15.
 * Chemin du drone
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Target extends Entity {
    private String interventionId;
    private List<Position> positions;
    private boolean isClose;

    /**
     * Constructeur
     */
    public Target() {
        positions = new ArrayList<>();
    }

    /**
     * Récupèration de la liste des positions liées à un trajet
     * @return : liste des positions liées à un trajet
     */
    public List<Position> getPositions() {
        return positions;
    }

    /**
     * Renseignement de la liste des positions liées à un trajet
     * @param positions : liste des positions liées à un trajet
     */
    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    /**
     * Ajout d'une position dans le trajet
     * @param position : Nouvelle position
     */
    public void addPosition(Position position) {
        this.positions.add(position);
    }

    /**
     * Indique si le trajet est fermé (boucle)
     * @return : true si le trajet est fermé, false sinon
     */
    public boolean isClose() {
        return isClose;
    }

    /**
     * Position la fermeture du trajet
     * @param isClose : true si le trajet est fermé, false sinon
     */
    public void setClose(boolean isClose) {
        this.isClose = isClose;
    }

    /**
     * Id de l'intervention courante
     * @return Id de l'intervention
     */
    public String getInterventionId() {
        return interventionId;
    }

    /**
     * Id de l'intervention courante
     * @param interventionId de l'intervention
     */
    public void setInterventionId(String interventionId) {
        this.interventionId = interventionId;
    }
}
