package entity;

import util.Constant;

/**
 * Created by arno on 07/04/15.
 *
 * This class is used for handling mean information which are available for an intervention.
 *
 */

public class Mean extends AbstractEntity{

    private Vehicle vehicle;
    private Position coordinates;
    private boolean inPosition;
    private boolean isDeclined;

    public Mean(Vehicle vehicle) {
        this();
        this.vehicle = vehicle;
    }
    public Mean() {
        super();
        this.inPosition = false;
        this.isDeclined = false;
        this.coordinates = new Position();
        this.coordinates.setAltitude(Double.NaN);
        this.coordinates.setLatitude(Double.NaN);
        this.coordinates.setLongitude(Double.NaN);
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

    public boolean getInPosition() {
        return inPosition;
    }

    public void setInPosition(boolean inPosition) {
        this.inPosition = inPosition;
    }

    public boolean getisDeclined() {
        return isDeclined;
    }

    public void setisDeclined(boolean isDeclined) {
        this.isDeclined = isDeclined;
    }


}
