package fr.m2gla.istic.projet.model;

/**
 * Created by baptiste on 10/04/15.
 */
public class Mean {
    private String id;
    private Vehicle vehicle;
    private Position coordinates;
    private boolean inPosition;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean getIsInPosition() {
        return inPosition;
    }

    public void setInPosition(boolean inPosition) {
        this.inPosition = inPosition;
    }
}
