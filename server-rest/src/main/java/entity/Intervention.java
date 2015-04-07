package entity;

import util.MeansByDisasterCode;

import java.util.List;

/**
 * Created by arno on 07/04/15.
 *
 * This class is used for handling intervention information.
 *
 */

public class Intervention {

    private long id;
    private String address;
    private int postcode;
    private String city;
    private DisasterCode disasterCode;
    private List<Mean> meansList;

    public Intervention(String address, int postcode, String city, DisasterCode disasterCode) {
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.disasterCode = disasterCode;
        this.meansList = MeansByDisasterCode.meansByDisasterCode(disasterCode);
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
}

