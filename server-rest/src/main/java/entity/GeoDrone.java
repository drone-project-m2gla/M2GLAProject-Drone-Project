package entity;

import util.Constant;

/**
 * Created by alban on 17/03/15.
 */
public class GeoDrone extends AbstractEntity {

    private Position coordinates;

    /**
     * Basic contruct, assign type
     */
    public GeoDrone()
    {
        super();
        this.datatype = Constant.DATATYPE_GEODRONE;
    }

    /**
     * Drone coordinates getter
     *
     * @return Position
     */
    public Position getCoordinates() {
        return coordinates;
    }

    /**
     * Drone coordinates setter
     *
     * @param coordinates Position
     */
    public void setCoordinates(Position coordinates) {
        this.coordinates = coordinates;
    }

}
