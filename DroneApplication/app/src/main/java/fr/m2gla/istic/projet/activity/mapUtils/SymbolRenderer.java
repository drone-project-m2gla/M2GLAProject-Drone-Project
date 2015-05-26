package fr.m2gla.istic.projet.activity.mapUtils;

/**
 * Created by fernando on 22/05/15.
 */

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import fr.m2gla.istic.projet.context.SVGAdapter;

/**
* Cluster rendered used to draw firemen symbols
* All the symbols are integrated into the map that links markers and symbols
* Non-topographic symbols are draggable
* Description is shown in marker's info window
*/
public class SymbolRenderer extends DefaultClusterRenderer<SymbolMarkerClusterItem> {
    private Context context;
    private MapListeners mapListeners;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setMapListeners(MapListeners mapListeners) {
        this.mapListeners = mapListeners;
    }

    public SymbolRenderer(Context context, GoogleMap map, ClusterManager<SymbolMarkerClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(SymbolMarkerClusterItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        markerOptions.icon(SVGAdapter.convertSymbolToIcon(context, item.getSymbol()));

        if (!item.getSymbol().isTopographic()) {
            markerOptions.draggable(true);
            markerOptions.title("Action sur le moyen");
        } else {
            markerOptions.title(item.getSymbol().getFirstText());
        }
    }

    @Override
    protected void onClusterItemRendered(SymbolMarkerClusterItem clusterItem, Marker marker) {
        mapListeners.markerSymbolLinkMap.put(marker.getId(), clusterItem);
    }
}

