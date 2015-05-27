package fr.m2gla.istic.projet.activity.mapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by baptiste on 22/05/15.
 */
public class ImageDroneRenderer extends DefaultClusterRenderer<ImageMarkerClusterItem> {
    public ImageDroneRenderer(Context context, GoogleMap map, ClusterManager<ImageMarkerClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(ImageMarkerClusterItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        if (item.getImage() == null) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        } else {
            byte[] byteImage = Base64.decode(item.getImage(), Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            image.setWidth(50);
            image.setHeight(50);

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(image));
        }
    }
}
