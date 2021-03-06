package entity;

/**
 * @author jerem
 * @see GeoIcon with all information to place on map. Using for Topographie Rest
 */
public class GeoIcon extends AbstractEntity {

    /**
     * filename without extension
     */
    private String filename;

    private String entitled;

    private Position position;

    private String firstContent;

    private String secondContent;

    private String color;

    private Boolean tiret;

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