package fr.m2gla.istic.projet.model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by fernando on 4/14/15.
 */
public class SymbolMarkerClusterItem implements ClusterItem {
    private final LatLng mPosition;
    private BitmapDescriptor icon;
    private Symbol symbol;

    public SymbolMarkerClusterItem(double lat, double lng, BitmapDescriptor icon, Symbol symbol) {
        mPosition = new LatLng(lat, lng);
        this.icon = icon;
        this.symbol = symbol;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public Symbol getSymbol() {
        return symbol;
    }
}