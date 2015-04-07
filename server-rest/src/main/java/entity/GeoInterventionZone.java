package entity;


import util.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arno on 09/03/15.
 */
public class GeoInterventionZone extends AbstractEntity implements Serializable {

    List<Zone> coordinates = new ArrayList<Zone>();
    public GeoInterventionZone() {
        super();
        this.datatype = Constant.DATATYPE_GEOINTERVENTIONZONE;
    }

    public List<Zone> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Zone> coordinates) {
        this.coordinates = coordinates;
    }

    public void addZone(Zone zone) {
        this.coordinates.add(zone);
    }
}


