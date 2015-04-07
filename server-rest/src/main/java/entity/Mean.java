package entity;

/**
 * Created by arno on 07/04/15.
 *
 * This class is used for handling mean information which are available for an intervention.
 *
 *
 *
 */

public class Mean {

    private Vehicle vehicule;
    private Position positionRequested;
    private boolean isInPosition;

    public Mean(Vehicle vehicule) {
        this.vehicule = vehicule;
        this.isInPosition = false;
    }

    public Vehicle getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicle vehicule) {
        this.vehicule = vehicule;
    }

    public Position getPositionRequested() {
        return positionRequested;
    }

    public void setPositionRequested(Position positionRequested) {
        this.positionRequested = positionRequested;
    }

    public boolean isInPosition() {
        return isInPosition;
    }

    public void setInPosition(boolean isInPosition) {
        this.isInPosition = isInPosition;
    }
}
