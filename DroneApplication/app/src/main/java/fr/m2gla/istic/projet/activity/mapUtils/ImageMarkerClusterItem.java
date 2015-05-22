package fr.m2gla.istic.projet.activity.mapUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by baptiste on 22/05/15.
 */
public class ImageMarkerClusterItem implements ClusterItem {
    private LatLng position;
    private String img;

    public ImageMarkerClusterItem(LatLng position, String img) {
        this.position = position;
        this.img = img;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public String getImage() {
        return img;
    }
}
