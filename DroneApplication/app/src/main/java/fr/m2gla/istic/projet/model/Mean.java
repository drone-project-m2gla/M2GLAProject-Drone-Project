package fr.m2gla.istic.projet.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * Created by baptiste on 10/04/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mean extends Entity {
    private Vehicle vehicle;
    private Position coordinates;
    private boolean inPosition;
    //    private boolean meanIsDeclined;
    private MeanState meanState;
    private Date dateRefused;
    private Date dateRequested;
    private Date dateActivated;
    private Date dateArrived;
    private Date dateEngaged;
    private Date dateReleased;

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

    public void setInPosition(boolean inPosition) {
        this.inPosition = inPosition;
    }

    public boolean isInPosition() {
        return inPosition;
    }

    public MeanState getMeanState() {
        return meanState;
    }

    public void setMeanState(MeanState meanState) {
        this.meanState = meanState;
    }

    public Date getDateRefused() {
        return dateRefused;
    }

    public void setDateRefused(Date dateRefused) {
        this.dateRefused = dateRefused;
    }

    public Date getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(Date dateRequested) {
        this.dateRequested = dateRequested;
    }

    public Date getDateActivated() {
        return dateActivated;
    }

    public void setDateActivated(Date dateActivated) {
        this.dateActivated = dateActivated;
    }

    public Date getDateArrived() {
        return dateArrived;
    }

    public void setDateArrived(Date dateArrived) {
        this.dateArrived = dateArrived;
    }

    public Date getDateEngaged() {
        return dateEngaged;
    }

    public void setDateEngaged(Date dateEngaged) {
        this.dateEngaged = dateEngaged;
    }

    public Date getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(Date dateReleased) {
        this.dateReleased = dateReleased;
    }

    public boolean meanIsDeclined() {
        return this.meanState == MeanState.REFUSED;
    }


//    public boolean meanIsDeclined() {
//        return meanIsDeclined;
//    }
//
//    public void setIsDeclined(boolean meanIsDeclined) {
//        this.meanIsDeclined = meanIsDeclined;
//    }
}
