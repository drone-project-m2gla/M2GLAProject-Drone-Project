package fr.m2gla.istic.projet.strategy.impl;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Collection;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.activity.mapUtils.ImageMarkerClusterItem;
import fr.m2gla.istic.projet.model.GeoImage;
import fr.m2gla.istic.projet.model.Position;
import fr.m2gla.istic.projet.strategy.Strategy;

/**
 * Created by baptiste on 26/05/15.
 */
public class StrategyImageDrone implements Strategy {
    private static final double GPS_DELTA = 0.0005;
    private static StrategyImageDrone INSTANCE;
    private MapActivity activity;

    protected StrategyImageDrone() {}

    public void setActivity(MapActivity activity) {
        this.activity = activity;
    }

    public static StrategyImageDrone getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StrategyImageDrone();
        }
        return INSTANCE;
    }

    @Override
    public String getScopeName() {
        return "imageDrone";
    }

    @Override
    public Class<?> getType() {
        return GeoImage.class;
    }

    @Override
    public void call(Object object) {
        if (activity != null) {
            GeoImage image = (GeoImage)object;
            Collection<Marker> imageMarker = activity.getDroneClusterManager().getMarkerCollection().getMarkers();
            for (Marker m : imageMarker) {
                if (positionEqual(m.getPosition(), image.getPosition())) {
                    // Remove marker
                    LatLng latLng = m.getPosition();
                    m.remove();
                    // Replace marker to image marker
                    ImageMarkerClusterItem marker = new ImageMarkerClusterItem(latLng, image.getImage());
                    activity.getDroneClusterManager().addItem(marker);
                    activity.getDroneClusterManager().cluster();
                }
            }
        }
    }

    private boolean positionEqual(LatLng pos1, Position pos2) {
        boolean latitude1 = Double.compare(pos1.latitude + GPS_DELTA, pos2.getLatitude()) < 1;
        boolean latitude2 = Double.compare(pos1.latitude - GPS_DELTA, pos2.getLatitude()) < 1;
        boolean longitude1 = Double.compare(pos1.longitude + GPS_DELTA, pos2.getLongitude()) < 1;
        boolean longitude2 = Double.compare(pos1.longitude - GPS_DELTA, pos2.getLongitude()) < 1;

        return latitude1 || latitude2 || longitude1 || longitude2;
    }
}
