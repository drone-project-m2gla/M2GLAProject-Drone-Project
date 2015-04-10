package entity;

import util.Constant;

/**
 * Created by arno on 07/04/15.
 *
 * This class is used for handling mean information which are available for an intervention.
 *
 *
 *
 */

public class Mean extends AbstractEntity{

    private Vehicle vehicle;
    private Position coordinates;
    private boolean inPosition;

    public Mean(Vehicle vehicle) {
        this();
        this.vehicle = vehicle;
        this.inPosition = false;
        this.coordinates = new Position();
        this.coordinates.setAltitude(-1);
        this.coordinates.setLatitude(-1);
        this.coordinates.setLongitude(-1);
    }
    public Mean() {
        super();
        this.datatype = Constant.DATATYPE_MEAN;
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


}
