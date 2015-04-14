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
    private boolean isDeclined;

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

    public boolean getIsDeclined() {
        return isDeclined;
    }

    public void setIsDeclined(boolean isDeclined) {
        this.isDeclined = isDeclined;
    }
}
