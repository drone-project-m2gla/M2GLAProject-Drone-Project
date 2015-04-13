package entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import util.Constant;

/**
 * Created by arno on 09/03/15.
 */
public class GeoInterventionZone extends AbstractEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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


