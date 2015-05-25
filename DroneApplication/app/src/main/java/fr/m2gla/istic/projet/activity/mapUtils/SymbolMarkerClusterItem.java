package fr.m2gla.istic.projet.activity.mapUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import fr.m2gla.istic.projet.model.Symbol;

/**
 * Created by fernando on 4/14/15.
 */
public class SymbolMarkerClusterItem implements ClusterItem {
    private LatLng mPosition;
    private Symbol symbol;

    public SymbolMarkerClusterItem(double lat, double lng, Symbol symbol) {
        mPosition = new LatLng(lat, lng);
        this.symbol = symbol;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public void setPosition(LatLng position) {
        this.mPosition = position;
    }

    public Symbol getSymbol() {
        return symbol;
    }
}