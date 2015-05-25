package fr.m2gla.istic.projet.activity.mapUtils;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.Map;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.fragments.DroneTargetActionFragment;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.Position;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

/**
 * Created by fernando on 22/05/15.
 */
public class MapListeners implements
        AdapterView.OnDragListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener {
    private static final String TAG = "MapListeners";

    // offsets used to place the icon when it is dropped
    private static final int OFFSET_X = -100;
    private static final int OFFSET_Y = 30;

    // Shift used by raise on drag for map markers
    private static final int SHIFT_RAISE_ON_DRAG = 90;

    private MapActivity mapActivity;

    // Keep track of all the symbols by marker id
    public Map<String, SymbolMarkerClusterItem> markerSymbolLink = new HashMap<String, SymbolMarkerClusterItem>();

    public void setMapActivity(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
    }

    /**
     * Map fragment drag listener used to get the dropped symbol
     * A new symbol is created and added to the map
     *
     * @param v     current view
     * @param event drag element
     * @return
     */
    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DROP) {

            final ClipData clipData = event.getClipData();
            try {
                final LatLng latlng = mapActivity.map.getProjection().fromScreenLocation(new Point((int) event.getX() + OFFSET_X, (int) event.getY() + OFFSET_Y));

                //Use REST to update position on confirmation
                Position position = new Position();
                position.setLatitude(latlng.latitude);
                position.setLongitude(latlng.longitude);
                Mean mean = new Mean();
                //Get mean id from ClipData saved onDrag
                mean.setId((String) clipData.getItemAt(0).getText());
                mean.setCoordinates(position);

                mapActivity.setDraggingMode(false);
                RestServiceImpl.getInstance()
                        .post(RestAPI.POST_POSITION_MOVE, mapActivity.restParams, mean, Mean.class,
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Log.e(TAG, "Post new position success");
                                        mapActivity.loadMeansInMap();
                                    }
                                },
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Log.e(TAG, "Post new position error");
                                        Toast.makeText(mapActivity, "Impossible de positionner le moyen", Toast.LENGTH_LONG).show();
                                    }
                                });

            } catch (IllegalArgumentException ignored) {
            }
        }
        return true;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (!mapActivity.isDroneMode()) return;

        DroneTargetActionFragment droneTargetActionFragment = mapActivity.getDroneTargetActionFragment();
        Position position = new Position();
        position.setLatitude(latLng.latitude);
        position.setLongitude(latLng.longitude);

        ImageMarkerClusterItem marker = new ImageMarkerClusterItem(latLng, null);
        mapActivity.getDroneClusterManager().addItem(marker);
        mapActivity.getDroneClusterManager().cluster();

        // unclose target if add marker after close target
        if (droneTargetActionFragment.getTarget().isClose()) {
            Position pos = droneTargetActionFragment.getTarget().getPositions().get(0);

            droneTargetActionFragment.getTarget().setClose(false);
            droneTargetActionFragment.getTarget().getPositions().add(pos);
        }

        if (!droneTargetActionFragment.getTarget().getPositions().isEmpty()) {
            Position pos = droneTargetActionFragment.getTarget().getPositions()
                    .get(droneTargetActionFragment.getTarget().getPositions().size() - 1);

            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(new LatLng(pos.getLatitude(), pos.getLongitude()))
                    .add(latLng)
                    .width(5)
                    .color(Color.rgb(143, 0, 71));

            mapActivity.getPolylineList().add(mapActivity.map.addPolyline(polylineOptions));
        }

        droneTargetActionFragment.addPosition(position);
    }

    /**
     * Marker drag listener method used when marker is dragged
     * Used to disable raiseOnDrag
     *
     * @param marker dragged marker element
     */
    @Override
    public void onMarkerDrag(Marker marker) {
        disableRaiseOnDrag(marker);
    }

    /**
     * Marker drag listener method used when marker is dropped
     * Changes symbol to use dashed lines whenever its position is changed
     *
     * @param marker dragged marker element
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Disable raiseOnDrag
        disableRaiseOnDrag(marker);

        //Use REST to update position on confirmation
        Position position = new Position();
        position.setLatitude(marker.getPosition().latitude);
        position.setLongitude(marker.getPosition().longitude);
        Mean mean = new Mean();
        mean.setId(markerSymbolLink.get(marker.getId()).getSymbol().getId());
        mean.setCoordinates(position);

        final String markerId = marker.getId();

        mapActivity.setDraggingMode(false);
        RestServiceImpl.getInstance()
                .post(RestAPI.POST_POSITION_MOVE, mapActivity.restParams, mean, Mean.class,
                        new Command() {
                            @Override
                            public void execute(Object response) {
                                Log.e(TAG, "Post new position success");
                                //Change symbol image to dashed one
                                if (markerSymbolLink.containsKey(markerId)) {
                                    mapActivity.loadMeansInMap();
                                }
                            }
                        },
                        new Command() {
                            @Override
                            public void execute(Object response) {
                                Log.e(TAG, "Post new position error");
                                Toast.makeText(mapActivity, "Impossible de positionner le moyen", Toast.LENGTH_LONG).show();
                            }
                        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //Log.d(TAG, "main onClusterItemInfoWindowClick");
        final Symbol meanSymbol = markerSymbolLink.get(marker.getId()).getSymbol();
        final Marker _marker = marker;
        if (!meanSymbol.isTopographic()) {
            new AlertDialog.Builder(mapActivity)
                    .setTitle(R.string.actions_moyens)
                    .setItems(R.array.optionsMoyenEngage, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position of the selected item
                            switch (which) {
                                case 0: {
                                    // Valider la position du moyen
                                    Mean mean = new Mean();
                                    mean.setId(meanSymbol.getId());
                                    Position position = new Position();
                                    LatLng markerPosition = _marker.getPosition();
                                    position.setLatitude(markerPosition.latitude);
                                    position.setLongitude(markerPosition.longitude);
                                    mean.setCoordinates(position);
                                    mean.setInPosition(true);

                                    RestServiceImpl.getInstance()
                                            .post(RestAPI.POST_POSITION_CONFIRMATION, mapActivity.restParams, mean, Mean.class,
                                                    new Command() {
                                                        @Override
                                                        public void execute(Object response) {
                                                            Log.i(TAG, "Confirm position success");
                                                            mapActivity.loadMeansInMap();
                                                        }
                                                    },
                                                    new Command() {
                                                        @Override
                                                        public void execute(Object response) {
                                                            Log.e(TAG, "Confirm position error");
                                                            Toast.makeText(mapActivity, "Impossible de confirmer la position de ce moyen", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                            );
                                    break;
                                }
                                case 1: {
                                    // TODO: Libérer le moyen
                                    // Supprimer ses coordonnées
                                    // Supprimer le marker
                                    // TODO : Le supprimer de la liste de moyens validés
                                    Mean mean = new Mean();
                                    mean.setId(meanSymbol.getId());
                                    Position position = new Position();
                                    position.setLatitude(Double.NaN);
                                    position.setLongitude(Double.NaN);
                                    mean.setCoordinates(position);
                                    mean.setInPosition(false);

                                    RestServiceImpl.getInstance()
                                            .post(RestAPI.POST_RELEASE, mapActivity.restParams, mean, Mean.class,
                                                    new Command() {
                                                        @Override
                                                        public void execute(Object response) {
                                                            Log.i(TAG, "Libérer moyen success");
                                                            mapActivity.loadMeansInMap();
                                                        }
                                                    },
                                                    new Command() {
                                                        @Override
                                                        public void execute(Object response) {
                                                            Log.e(TAG, "Libérer moyen error");
                                                            Toast.makeText(mapActivity, "Impossible de libérer ce moyen", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                            );
                                    break;
                                }
                                case 2: {
                                    //TODO: Retour CRM, disponible pour mettre sur la carte
                                    // Supprimer ses coordonnées
                                    // Ajouter dans la liste de moyens validés (automatique car la liste se rafraîchit)
                                    Mean mean = new Mean();
                                    mean.setId(meanSymbol.getId());
                                    Position position = new Position();
                                    position.setLatitude(Double.NaN);
                                    position.setLongitude(Double.NaN);
                                    mean.setCoordinates(position);
                                    mean.setInPosition(false);

                                    RestServiceImpl.getInstance()
                                            //TODO: utiliser la méthode post correcte
                                            .post(RestAPI.POST_RELEASE, mapActivity.restParams, mean, Mean.class,
                                                    new Command() {
                                                        @Override
                                                        public void execute(Object response) {
                                                            Log.e(TAG, "Retour CRM success");
                                                            mapActivity.loadMeansInMap();
                                                        }
                                                    },
                                                    new Command() {
                                                        @Override
                                                        public void execute(Object response) {
                                                            Log.e(TAG, "Retour CRM error");
                                                            Toast.makeText(mapActivity, "Impossible de retourner ce moyen au CRM", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                            );
                                    break;
                                }
                            }
                        }})
                    .show();
        }
    }

    /**
     * Marker drag listener method used when drag starts after a long click
     * Used to disable raiseOnDrag
     *
     * @param marker dragged marker element
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        mapActivity.setDraggingMode(true);
        disableRaiseOnDrag(marker);
    }

    /**
     * Whenever a marker starts to be dragged it is raised,
     * this method allows to undo this shift to simulate raiseOnDrag = false behaviour
     *
     * @param marker dragged marker
     */
    private void disableRaiseOnDrag(Marker marker) {
        LatLng latLng = marker.getPosition();
        Point point = mapActivity.map.getProjection().toScreenLocation(latLng);
        point.set(point.x, point.y + SHIFT_RAISE_ON_DRAG);
        LatLng latLng2 = mapActivity.map.getProjection().fromScreenLocation(point);
        marker.setPosition(latLng2);
    }
}
