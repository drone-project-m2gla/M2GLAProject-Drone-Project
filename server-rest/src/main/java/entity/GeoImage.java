package entity;

/**
 * @author mds
 * @see GeoImage with coordinates and name
 */
public class GeoImage extends AbstractEntity {
    private Position position;
    private int width;
    private int height;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

        return true;
    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GeoImage{" +
                "position=" + position +
                ", width=" + width +
                ", height=" + height +
                ", image='" + image + '\'' +
                '}';
    }
}
