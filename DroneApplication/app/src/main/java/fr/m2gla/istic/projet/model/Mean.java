package fr.m2gla.istic.projet.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by baptiste on 10/04/15.
 * Entité des moyens
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mean extends Entity {
    private Vehicle vehicle;
    private Position coordinates;
    private boolean inPosition;
    //    private boolean refusedMeans;
    private MeanState meanState;
    private String name;
    private String dateRefused;
    private String dateRequested;
    private String dateActivated;
    private String dateArrived;
    private String dateEngaged;
    private String dateReleased;


    /**
     * Récupération du type de véhicule
     *
     * @return : type de véhicule
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Renseignement du type de véhicule
     *
     * @param vehicle : type de véhicule
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Récupération des coordonnées du véhicule
     *
     * @return : coordonnées du véhicule
     */
    public Position getCoordinates() {
        return coordinates;
    }

    /**
     * Renseignement des coordonnées du véhicule
     *
     * @param coordinates : coordonnées du véhicule
     */
    public void setCoordinates(Position coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Indique si la position du moyen est validée
     *
     * @return : true si la position est validée, false sinon
     */
    public boolean getInPosition() {
        return inPosition;
    }

    /**
     * Indique si la position du moyen est validée
     *
     * @return : true si la position est validée, false sinon
     */
    public boolean isInPosition() {
        return inPosition;
    }

    /**
     * Permet de spécifier si la position du moyen est validée
     *
     * @param inPosition : true si la position est validée, false sinon
     */
    public void setInPosition(boolean inPosition) {
        this.inPosition = inPosition;
    }

    /**
     * Récupération de l'état courant du moyen
     *
     * @return : état courant du moyen
     */
    public MeanState getMeanState() {
        return meanState;
    }

    /**
     * Renseignement de l'état courant du moyen
     *
     * @param meanState : état courant du moyen
     */
    public void setMeanState(MeanState meanState) {
        this.meanState = meanState;
    }

    /**
     * Récupération du nom / de la désignation unique du moyen
     *
     * @return : nom / désignation unique du moyen
     */
    public String getName() {
        if (name == null || name.isEmpty())
            name = getId();
        return name;
    }

    /**
     * Renseignement du nom / de la désignation unique du moyen
     *
     * @param name : nom / désignation unique du moyen
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Récupération de la date de refus du moyen
     *
     * @return : date de refus du moyen
     */
    public String getDateRefused() {
        return dateRefused;
    }

    /**
     * Renseignement de la date de refus du moyen
     *
     * @param dateRefused : date de refus du moyen
     */
    public void setDateRefused(String dateRefused) {
        this.dateRefused = dateRefused;
    }

    /**
     * Récupération de la date de demande de moyen supplémentaire
     *
     * @return : date de demande de moyen supplémentaire
     */
    public String getDateRequested() {
        return dateRequested;
    }

    /**
     * Renseignement de la date de demande de moyen supplémentaire
     *
     * @param dateRequested : date de demande de moyen supplémentaire
     */
    public void setDateRequested(String dateRequested) {
        this.dateRequested = dateRequested;
    }

    /**
     * Récupération de la date de validation du moyen
     *
     * @return : date validation du moyen
     */
    public String getDateActivated() {
        return dateActivated;
    }

    /**
     * Renseignement de la date validation du moyen
     *
     * @param dateActivated : date validation du moyen
     */
    public void setDateActivated(String dateActivated) {
        this.dateActivated = dateActivated;
    }

    /**
     * Récupération de la date d'arrivée du moyen sur le lieu de l'intervention
     *
     * @return : date d'arrivée du moyen sur le lieu de l'intervention
     */
    public String getDateArrived() {
        return dateArrived;
    }

    /**
     * Renseignement de la date d'arrivée du moyen sur le lieu de l'intervention
     *
     * @param dateArrived : date d'arrivée du moyen sur le lieu de l'intervention
     */
    public void setDateArrived(String dateArrived) {
        this.dateArrived = dateArrived;
    }

    /**
     * Récupération de la date d'engagement du moyen
     *
     * @return : date d'engagement du moyen
     */
    public String getDateEngaged() {
        return dateEngaged;
    }

    /**
     * Renseignement de la date d'engagement du moyen
     *
     * @param dateEngaged : date d'engagement du moyen
     */
    public void setDateEngaged(String dateEngaged) {
        this.dateEngaged = dateEngaged;
    }

    /**
     * Récupération de la date de libération du moyen
     *
     * @return : date de libération du moyen
     */
    public String getDateReleased() {
        return dateReleased;
    }

    /**
     * Renseignement de la date de libération du moyen
     *
     * @param dateReleased : date de libération du moyen
     */
    public void setDateReleased(String dateReleased) {
        this.dateReleased = dateReleased;
    }

    /**
     * Vérification de l'état Refusé du moyen
     *
     * @return : true si le moyen a été refusé, false sinon
     */
    public boolean refusedMeans() {
        return this.meanState == MeanState.REFUSED;
    }

    /**
     * Vérification de l'état demandé du moyen
     *
     * @return : true si le moyen est en cours de demande
     */
    public boolean requestedMean() {
        return this.meanState == MeanState.REQUESTED;
    }

    /**
     * Vérification de l'état arrivée du moyen
     *
     * @return : true si le moyen est arrivée sur le lieu de l'intervention, false sinon
     */
    public boolean arrivedMean() {
        return this.meanState == MeanState.ARRIVED;
    }

    /**
     * Vérification de l'état en transit du moyen
     *
     * @return : true si le moyen est en cours de déplacement vers sa position d'intervention, false
     * sinon
     */
    public boolean onTransitMean() {
        return this.meanState == MeanState.ACTIVATED;
    }
}
