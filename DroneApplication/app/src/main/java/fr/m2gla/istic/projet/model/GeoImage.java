package fr.m2gla.istic.projet.model;

import java.util.Date;

/**
 * Entité de geolocalisation d'une image
 */
public class GeoImage extends Entity {
    private Position position;
    private int width;
    private int height;
    private String image;
    private Date date;
    private String interventionId;

    /**
     * Récupération de l'image
     * @return : image sous forme de chaine de caractères
     */
    public String getImage() {
        return image;
    }

    /**
     * Renseignement de l'image courante
     * @param image : image sous forme de chaine de caractères
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Récupération de la position de l'image
     * @return : position de l'image
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Renseignement de la position de l'image
     * @param position : Position de l'image
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Récupération de la largeur de l'image
     * @return : Largeur de l'image
     */
    public int getWidth() {
        return width;
    }


    /**
     * Renseignement de la largeur de l'image
     * @param width : Largeur de l'image
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Récupération de la hauteur de l'image
     * @return : hauteur de l'image
     */
    public int getHeight() {
        return height;
    }

    /**
     * Renseignement de la hauteur de l'image
     * @param height : hauteur de l'image
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Date de la prise de l'image
     * @return date de l'image
     */
    public Date getDate() {
        return date;
    }

    /**
     * Date de la prise de l'image
     * @param date date de l'image
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Id de l'intervention
     * @return id de l'intervention
     */
    public String getInterventionId() {
        return interventionId;
    }

    /**
     * Id de l'intervention
     * @param interventionId id de l'intervention
     */
    public void setInterventionId(String interventionId) {
        this.interventionId = interventionId;
    }
}
