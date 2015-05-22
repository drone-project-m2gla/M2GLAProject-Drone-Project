package entity;

import util.Datetime;
import java.util.Date;

/**
 *@author arno
 * @see Mean is used for handling mean information which are available for an intervention.
 *
 */
public class Mean extends AbstractEntity{
    private String name;
    private Vehicle vehicle;
    private Position coordinates;
    private boolean inPosition;
    private MeanState meanState;
    private Date dateRefused;
    private Date dateRequested;
    private Date dateActivated;
    private Date dateArrived;
    private Date dateEngaged;
    private Date dateReleased;

    public Mean(Vehicle vehicle, Boolean isInitialMean) {
        this();
        this.vehicle = vehicle;

        if (isInitialMean) {
            this.dateActivated = this.dateRequested;
            this.meanState = MeanState.ACTIVATED;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mean)) return false;

        Mean mean = (Mean) o;

        if (name != null && !name.equals(mean.getName())) return false;
        if (inPosition != mean.inPosition) return false;
        if (coordinates != null ? !coordinates.equals(mean.coordinates) : mean.coordinates != null) return false;
        if (vehicle != mean.vehicle) return false;
        if (dateRequested != null ? !dateRequested.equals(mean.dateRequested) : mean.dateRequested != null) return false;
        if (dateActivated != null ? !dateActivated.equals(mean.dateActivated) : mean.dateActivated != null) return false;
        if (dateArrived != null ? !dateArrived.equals(mean.dateArrived) : mean.dateArrived != null) return false;
        if (dateEngaged != null ? !dateEngaged.equals(mean.dateEngaged) : mean.dateEngaged != null) return false;
        if (dateReleased != null ? !dateReleased.equals(mean.dateReleased) : mean.dateReleased != null) return false;
        if (dateRefused != null ? !dateRefused.equals(mean.dateRefused) : mean.dateRefused != null) return false;
        if (meanState != mean.meanState) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Mean{" +
                "vehicle=" + vehicle +
                ", coordinates=" + coordinates +
                ", inPosition=" + inPosition +
                ", meanState=" + meanState +
                ", dateRefused=" + dateRefused +
                ", dateRequested=" + dateRequested +
                ", dateActivated=" + dateActivated +
                ", dateArrived=" + dateArrived +
                ", dateEngaged=" + dateEngaged +
                ", dateReleased=" + dateReleased +
                '}';
    }

    @Override
    public int hashCode() {
        int result = vehicle != null ? vehicle.hashCode() : 0;
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        result = 31 * result + (inPosition ? 1 : 0);

        return result;
    }

    public Mean() {
        super();
        this.name = "";
        this.inPosition = false;
        this.coordinates = new Position();
        this.coordinates.setAltitude(Double.NaN);
        this.coordinates.setLatitude(Double.NaN);
        this.coordinates.setLongitude(Double.NaN);
        this.dateRequested = Datetime.getCurrentDate();
        this.meanState = MeanState.REQUESTED;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public MeanState getMeanState() {
        return meanState;
    }

    public void setMeanState(MeanState meanState) {
        this.meanState = meanState;
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

    public Date getDateRefused() {
        return dateRefused;
    }

    public void setDateRefused(Date dateRefused) {
        this.dateRefused = dateRefused;
    }
}
