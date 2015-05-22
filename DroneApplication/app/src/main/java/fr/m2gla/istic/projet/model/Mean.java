package fr.m2gla.istic.projet.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by baptiste on 10/04/15.
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

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Position getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Position coordinates) {
        this.coordinates = coordinates;
    }

    public boolean getInPosition() {
        return inPosition;
    }

    public boolean isInPosition() {
        return inPosition;
    }

    public void setInPosition(boolean inPosition) {
        this.inPosition = inPosition;
    }

    public MeanState getMeanState() {
        return meanState;
    }

    public void setMeanState(MeanState meanState) {
        this.meanState = meanState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateRefused() {
        return dateRefused;
    }

    public void setDateRefused(String dateRefused) {
        this.dateRefused = dateRefused;
    }

    public String getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(String dateRequested) {
        this.dateRequested = dateRequested;
    }

    public String getDateActivated() {
        return dateActivated;
    }

    public void setDateActivated(String dateActivated) {
        this.dateActivated = dateActivated;
    }

    public String getDateArrived() {
        return dateArrived;
    }

    public void setDateArrived(String dateArrived) {
        this.dateArrived = dateArrived;
    }

    public String getDateEngaged() {
        return dateEngaged;
    }

    public void setDateEngaged(String dateEngaged) {
        this.dateEngaged = dateEngaged;
    }

    public String getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(String dateReleased) {
        this.dateReleased = dateReleased;
    }

    private String dateReleased;

    public boolean refusedMeans() {
        return this.meanState == MeanState.REFUSED;
    }

    public boolean requestedMean() {
        return this.meanState == MeanState.REQUESTED;
    }
    public boolean arrivedMean() {
        return this.meanState == MeanState.ARRIVED;
    }
    public boolean onTransitMean() {
        return this.meanState == MeanState.ACTIVATED;
    }
}
