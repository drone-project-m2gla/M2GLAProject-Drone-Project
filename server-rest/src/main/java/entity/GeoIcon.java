package entity;

import util.Constant;

/**
 * Created by jerem on 08/04/15.
 *
 *  Retourne une coordonnée satellite pour la carte avec son signalement (icone "statique)
 */
public class GeoIcon extends AbstractEntity {

    //nom du fichier sans l'extension
    private String filename;

    private String entitled;

    private Position position;

    private String firstContent;

    private String secondContent;

    private String color;

    private Boolean tiret;

    public GeoIcon() {
        super();
        this.datatype = Constant.DATATYPE_GEOICON;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoIcon that = (GeoIcon) o;

        if (color != null ? !color.equals(that.color) : that.color != null) return false;
        if (firstContent != null ? !firstContent.equals(that.firstContent) : that.firstContent != null)
            return false;
        if (secondContent != null ? !secondContent.equals(that.secondContent) : that.secondContent != null)
            return false;
        if (entitled != null ? !entitled.equals(that.entitled) : that.entitled != null)
            return false;
        if (filename != null ? !filename.equals(that.filename) : that.filename != null)
            return false;
        if (position != null ? !position.equals(that.position) : that.position != null)
            return false;
        if (tiret != null ? !tiret.equals(that.tiret) : that.tiret != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filename != null ? filename.hashCode() : 0;
        result = 31 * result + (entitled != null ? entitled.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (firstContent != null ? firstContent.hashCode() : 0);
        result = 31 * result + (secondContent != null ? secondContent.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (tiret != null ? tiret.hashCode() : 0);
        return result;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSecondContent() {
        return secondContent;
    }

    public void setSecondContent(String secondContent) {
        this.secondContent = secondContent;
    }

    public String getFirstContent() {
        return firstContent;
    }

    public void setFirstContent(String firstContent) {
        this.firstContent = firstContent;
    }

    public Boolean getTiret() {
        return tiret;
    }

    public void setTiret(Boolean tiret) {
        this.tiret = tiret;
    }
}