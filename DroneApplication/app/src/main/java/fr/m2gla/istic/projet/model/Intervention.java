package fr.m2gla.istic.projet.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by baptiste on 10/04/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Intervention extends Entity {
    private static final String TAG = "Intervention";
    private String label;
    private String dateCreate;
    private String address;
    private String postcode;
    private String city;
    private DisasterCode disasterCode;
    private List<Mean> meansList;
    private Position coordinates;
    // private List<Mean> meansXtra;

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

    /**
     * Moyens demandés
     *
     * @return
     */
    public List<Mean> meansRequested() {
        List<Mean> means = new ArrayList();
        Iterator<Mean> it = this.getMeansList().iterator();
        while (it.hasNext()) {
            Mean m = it.next();
            Log.i(TAG, "Mean " + m.getMeanState());
            if (m.getMeanState() == MeanState.REQUESTED) {
                means.add(m);
            }
        }
        return means;
    }

    /**
     * Moyens disponibles
     *
     * @return
     */
    public List<Mean> meansArrived() {
        List<Mean> means = new ArrayList();
        Iterator<Mean> it = this.getMeansList().iterator();
        while (it.hasNext()) {
            Mean m = it.next();
            if (m.getMeanState() == MeanState.ARRIVED) {
                means.add(m);
            }
        }
        return means;
    }

    /**
     * Moyens en transit
     *
     * @return
     */
    public List<Mean> meansTransit() {
        List<Mean> means = new ArrayList();
        Iterator<Mean> it = this.getMeansList().iterator();
        while (it.hasNext()) {
            Mean m = it.next();
            if (m.getMeanState() == MeanState.ACTIVATED) {
                means.add(m);
            }
        }
        return means;
    }
    /**
     * Moyens en transit
     *
     * @return
     */
    public List<Mean> meansRefused() {
        List<Mean> means = new ArrayList();
        Iterator<Mean> it = this.getMeansList().iterator();
        while (it.hasNext()) {
            Mean m = it.next();
            Log.i(TAG, "Mean " + m.getMeanState());
            if (m.getMeanState() == MeanState.REFUSED) {
                means.add(m);
            }
        }
        return means;
    }

//    public List<Mean> meansRequested() {
//        return meansXtra;
//    }
//
//    public void setMeansXtra(List<Mean> meansXtra) {
//        this.meansXtra = meansXtra;
//    }

}
