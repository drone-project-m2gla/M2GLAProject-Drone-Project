package fr.m2gla.istic.projet.model;

/**
 * Created by baptiste on 10/04/15.
 * Classe permettant de sauvegader une position
 */
public class Position {
    private double longitude;
    private double latitude;
    private double altitude;

    /**
     * Récupération de la longitude
     * @return : longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Renseignement de la longitude
     * @param longitude : longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Récupération de la latitude
     * @return : latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Renseignement de la latitude
     * @param latitude ; latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Récupération de l'altitude
     * @return : altitude
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * Renseignement de l'altitude
     * @param altitude : altitude
     */
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
