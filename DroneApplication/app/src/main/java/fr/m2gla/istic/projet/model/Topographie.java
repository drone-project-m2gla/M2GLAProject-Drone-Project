package fr.m2gla.istic.projet.model;

/**
 * Created by chaabi on 10/04/15.
 * Définition d'un élément topographique
 */
public class Topographie {

    private String id;
    private String filename;
    private String entitled;
    private String firstContent;
    private String secondContent;
    private String color;
    private boolean tiret;
    private Position position;


    /**
     * Constructeur
     */
    public Topographie() {

    }

    /**
     * Constructeur
     * @param id : Identifiant de l'élément topographique
     * @param filename : Nom du fichier associé
     * @param entitled
     * @param firstContent
     * @param secondContent
     * @param color : Couleur associée
     * @param tiret
     * @param position : Position de l'élément topographique
     */
    public Topographie(String id, String filename, String entitled, String firstContent, String secondContent, String color, boolean tiret, Position position) {
        this.id = id;
        this.filename = filename;
        this.entitled = entitled;
        this.firstContent = firstContent;
        this.secondContent = secondContent;
        this.color = color;
        this.tiret = tiret;
        this.position = position;
    }

    /**
     * Récupération de l'identifiant de l'élément topographique
     * @return : identifiant de l'élément topographique
     */
    public String getId() {
        return id;
    }

    /**
     * Renseignement de l'identifiant de l'élément topographique
     * @param id : identifiant de l'élément topographique
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Récupération du nom du fichier associé
     * @return : nom du fichier associé
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Renseignement du nom du fichier associé
     * @param filename : nom du fichier associé
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getEntitled() {
        return entitled;
    }

    public void setEntitled(String entitled) {
        this.entitled = entitled;
    }

    public String getFirstContent() {
        return firstContent;
    }

    public void setFirstContent(String firstContent) {
        this.firstContent = firstContent;
    }

    public String getSecondContent() {
        return secondContent;
    }

    public void setSecondContent(String secondContent) {
        this.secondContent = secondContent;
    }

    /**
     * Récupération de la couleur associée
     * @return : couleur associée
     */
    public String getColor() {
        return color;
    }

    /**
     * Renseignement de la couleur associée
     * @param color : couleur associée
     */
    public void setColor(String color) {
        this.color = color;
    }

    public boolean isTiret() {
        return tiret;
    }

    public void setTiret(boolean tiret) {
        this.tiret = tiret;
    }

    /**
     * Récupération de la position de l'élément
     * @return : position de l'élément
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Renseignement de la position de l'élément
     * @param position : position de l'élément
     */
    public void setPosition(Position position) {
        this.position = position;
    }
}
