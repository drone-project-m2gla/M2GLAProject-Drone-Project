package entity;

import java.util.Date;

/**
 * @author mds
 * @see GeoImage with coordinates and name
 */
public class GeoImage extends AbstractEntity {
    private Position position;
    private int width;
    private int height;
    private String image;
    private Date date;
    private long interventionId;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public long getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(long interventionID) {
        this.interventionId = interventionID;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoImage)) return false;

        GeoImage geoImage = (GeoImage) o;

        if (height != geoImage.height) return false;
        if (width != geoImage.width) return false;
        if (image != null ? !image.equals(geoImage.image) : geoImage.image != null) return false;
        if (position != null ? !position.equals(geoImage.position) : geoImage.position != null) return false;
        if (interventionId != geoImage.interventionId) return false;
        if (geoImage.date != null && date != null && geoImage.date.getTime() != date.getTime()) return false;
        return true;
    }

    @Override
    public String toString() {
        return "GeoImage{" +
                "position=" + position +
                ", width=" + width +
                ", height=" + height +
                ", image='" + image + '\'' +
                ", interventionId=" + interventionId + '\'' +
                ", date=" + date + '\'' +
                '}';
    }
}
