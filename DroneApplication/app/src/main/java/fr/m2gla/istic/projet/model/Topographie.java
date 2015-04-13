package fr.m2gla.istic.projet.model;

/**
 * Created by chaabi on 10/04/15.
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Topographie(){

    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isTiret() {
        return tiret;
    }

    public void setTiret(boolean tiret) {
        this.tiret = tiret;
    }
}
