package fr.m2gla.istic.projet.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by baptiste on 10/04/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Intervention extends Entity {
    private String label;
    private String dateCreate;
    private String address;
    private String postcode;
    private String city;
    private DisasterCode disasterCode;
    private List<Mean> meansList;
    private Position coordinates;
    private List<Mean> meansXtra;

    public Intervention() {

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
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
        return coordinates;
    }

    public void setCoordinates(Position position) {
        this.coordinates = position;
    }

//    public List<Mean> getMeansXtra() {
//        List<Mean> means = new ArrayList();
//        Iterator<Mean> it = this.getMeansList().iterator();
//        while (it.hasNext()) {
//            Mean m = it.next();
//            if (String.valueOf(m.getMeanState()).equals(MeanState.REQUESTED)) {
//                means.add(m);
//            }
//        }
//        return means;
//    }

    public List<Mean> getMeansXtra() {
        return meansXtra;
    }

    public void setMeansXtra(List<Mean> meansXtra) {
        this.meansXtra = meansXtra;
    }

}
