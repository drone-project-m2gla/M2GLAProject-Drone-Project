package fr.m2gla.istic.projet.model;

import java.util.List;

/**
 * Created by baptiste on 10/04/15.
 */
public class Intervention extends Entity {
    private String address;
    private int postcode;
    private String city;
    private DisasterCode disasterCode;
    private List<Mean> meansList;
    private Position position;

    public Intervention() {
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

    public void setMeansList(List<Mean> means) {
        this.meansList = means;
    }

    public Position getCoordinates() {
        return position;
    }

    public void setCoordinates(Position position) {
        this.position = position;
    }
}
