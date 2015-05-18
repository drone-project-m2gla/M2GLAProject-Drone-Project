package entity;

import util.Constant;

/**
 * Image with metadata
 * Created by mds on 10/03/15.
 * Coordonnées d'une image et son nom
 */
public class GeoImage extends AbstractEntity {
    private Position coordinates;

    @Override
    public String toString() {
        return "GeoImage{" +
                "coordinates=" + coordinates +
                ", imageIn64='" + imageIn64 + '\'' +
                '}';
    }

    private String imageIn64;

    /**
     * Image 64 code getter
     *
     * @return String
     */
    public String getImageIn64() {
        return imageIn64;
    }

    /**
     * Image setter
     *
     * @param imageIn64 String
     */
    public void setImageIn64(String imageIn64) {
        this.imageIn64 = imageIn64;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoImage geoImage = (GeoImage) o;

        if (coordinates != null ? !coordinates.equals(geoImage.coordinates) : geoImage.coordinates != null)
            return false;
        if (imageIn64 != null ? !imageIn64.equals(geoImage.imageIn64) : geoImage.imageIn64 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = coordinates != null ? coordinates.hashCode() : 0;
        result = 31 * result + (imageIn64 != null ? imageIn64.hashCode() : 0);
        return result;
    }

    /**

     * Image coordinates getter
     *
     * @return Position
     */
    public Position getCoordinates() {
        return coordinates;
    }

    /**
     * Image coordinatess setter
     *
     * @param coordinates Position
     */
    public void setCoordinates(Position coordinates) {
        this.coordinates = coordinates;
    }
}
