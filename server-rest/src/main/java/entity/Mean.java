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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mean)) return false;

        Mean mean = (Mean) o;

        if (inPosition != mean.inPosition) return false;
        if (isDeclined != mean.isDeclined) return false;
        if (coordinates != null ? !coordinates.equals(mean.coordinates) : mean.coordinates != null) return false;
        if (vehicle != mean.vehicle) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = vehicle != null ? vehicle.hashCode() : 0;
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        result = 31 * result + (inPosition ? 1 : 0);
        result = 31 * result + (isDeclined ? 1 : 0);
        return result;
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
