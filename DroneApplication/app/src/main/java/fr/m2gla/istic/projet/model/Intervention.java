package fr.m2gla.istic.projet.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by baptiste on 10/04/15.
 * Entité d'une intervention
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


    /**
     * Constructeur
     */
    public Intervention() {

    }

    /**
     * Récupération du nom de l'intervention
     * @return : Nom/Label de l'intervention
     */
    public String getLabel() {
        return label;
    }

    /**
     * Renseignement du nom de l'intervention
     * @param label : Nom/Label de l'intervention
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Récupération de la date de création de l'intervention
     * @return : Date de création
     */
    public String getDateCreate() {
        return dateCreate;
    }

    /**
     * Renseignement de la date de création de l'intervention
     * @param dateCreate : Date de Création
     */
    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    /**
     * Récupération de l'adresse de l'intervention
     * @return : Adresse de l'intervention
     */
    public String getAddress() {
        return address;
    }

    /**
     * Renseignement de l'adresse de l'intervention
     * @param address : Adresse de l'intervention
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Récupération du code postal du lieu de l'intervention
     * @return : Code Postal
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Renseignement du code postal du lieu de l'intervention
     * @param postcode : Code Postal
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * Récupération de la ville d'intervention
     * @return : Ville de l'intervention
     */
    public String getCity() {
        return city;
    }

    /**
     * Reseignement de la ville d'intervention
     * @param city : Ville de l'intervention
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Récupération du code désastre
     * @return : Code désastre
     */
    public DisasterCode getDisasterCode() {
        return disasterCode;
    }

    /**
     * Renseignement du code désastre
     * @param disasterCode : Code désastre
     */
    public void setDisasterCode(DisasterCode disasterCode) {
        this.disasterCode = disasterCode;
    }

    /**
     * Récupération de la liste des moyens de l'intervention
     * @return : Liste des moyens
     */
    public List<Mean> getMeansList() {
        return meansList;
    }

    /**
     * Renseignement de la liste des moyens de l'intervention
     * @param means : Liste des moyens
     */
    public void setMeansList(List<Mean> means) {
        this.meansList = means;
    }

    /**
     * Récupération des coordonnées de l'intervention
     * @return : coordonnées de l'intervention
     */
    public Position getCoordinates() {
        return coordinates;
    }

    /**
     * Renseignement des coordonnées de l'intervention
     * @param position : coordonnées de l'intervention
     */
    public void setCoordinates(Position position) {
        this.coordinates = position;
    }

    /**
     * Moyens demandés
     *
     * @return : Liste des moyens demandés
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
     * @return : Liste des moyens disponibles
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
     * @return : Liste des moyens en transit
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
     * Moyens refusés
     *
     * @return : Liste des moyens refusés
     */
    public List<Mean> meansRefused() {
        List<Mean> means = new ArrayList();
        Iterator<Mean> it = this.getMeansList().iterator();
        while (it.hasNext()) {
            Mean m = it.next();
            if (m.getMeanState() == MeanState.REFUSED) {
                means.add(m);
            }
        }
        return means;
    }

    /**
     * Moyens activés / validés.
     *
     * @return : Liste des moyens validés
     */
    public List<Mean> meansActivated() {
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

}
