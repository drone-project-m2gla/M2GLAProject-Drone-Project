package entity;

import util.Constant;
import util.MeansByDisasterCode;

import java.util.List;

/**
 * Created by arno on 07/04/15.
 *
 * This class is used for handling intervention information.
 *
 */

public class Intervention extends AbstractEntity {

    private String address;
    private int postcode;
    private String city;
    private DisasterCode disasterCode;
    private List<Mean> meansList;
    private Position coordinates;

    public Intervention(String address, int postcode, String city, DisasterCode disasterCode) {
        this();
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.disasterCode = disasterCode;
        generateMeanList();
        System.out.println(this.meansList.size());
    }

    public Intervention() {
        super();
        this.datatype = Constant.DATATYPE_INTERVENTION;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public DisasterCode getDisasterCode() {
        return disasterCode;
    }

    public void setDisasterCode(DisasterCode disasterCode) {
        this.disasterCode = disasterCode;
    }

    public List<Mean> getMeansList() {
        return meansList;
    }

    public void setMeansList(List<Mean> meansList) {
        this.meansList = meansList;
    }

    public Position getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Position coordinates) {
        this.coordinates = coordinates;
    }

    public void generateMeanList(){
        if (this.disasterCode != null) {
            MeansByDisasterCode.meansByDisasterCode(disasterCode);
        }
    }
}

