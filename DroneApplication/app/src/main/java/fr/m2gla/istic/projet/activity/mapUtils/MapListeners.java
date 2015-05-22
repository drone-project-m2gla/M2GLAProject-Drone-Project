package fr.m2gla.istic.projet.activity.mapUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import fr.m2gla.istic.projet.activity.MapActivity;
import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.Position;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.model.SymbolMarkerClusterItem;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

/**
 * Created by fernando on 22/05/15.
 */
public class MapListeners implements
        AdapterView.OnDragListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = "MapListeners";
    private Map<String, String> param;

    private MapActivity mapActivity;

    // Keep track of all the symbols by marker id
    public Map<String, SymbolMarkerClusterItem>markerSymbolLink = new HashMap<String, SymbolMarkerClusterItem>();

    // offsets used to place the icon when it is dropped
    private static final int OFFSET_X = -100;
    private static final int OFFSET_Y = 30;

    // Shift used by raise on drag for map markers
    private static final int SHIFT_RAISE_ON_DRAG = 90;

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

                MapActivity.setDraggingMode(false);
                RestServiceImpl.getInstance()
                        .post(RestAPI.POST_POSITION_MOVE, param, mean, Mean.class,
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Log.e(TAG, "Post new position success");
                                        // FIXME: Mise à jour de la liste des moyens.
                                        //Get symbol from ClipData saved onDrag
                                        /*Symbol symbol = new Symbol(
                                                (String) clipData.getItemAt(0).getText(),
                                                Symbol.SymbolType.valueOf((String) clipData.getItemAt(1).getText()),
                                                (String) clipData.getItemAt(2).getText(),
                                                (String) clipData.getItemAt(3).getText(),
                                                (String) clipData.getItemAt(4).getText());
                                        symbol.setValidated(false);
                                        SymbolMarkerClusterItem markerItem = new SymbolMarkerClusterItem(latlng.latitude, latlng.longitude, symbol);
                                        mClusterManager.addItem(markerItem);
                                        mClusterManager.cluster();*/
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

        MapActivity.setDraggingMode(false);
        RestServiceImpl.getInstance()
                .post(RestAPI.POST_POSITION_MOVE, param, mean, Mean.class,
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
                                            .post(RestAPI.POST_POSITION_CONFIRMATION, param, mean, Mean.class,
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
                                            //TODO: utiliser la méthode post correcte
                                            .post(RestAPI.POST_POSITION_CONFIRMATION, param, mean, Mean.class,
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
                                            .post(RestAPI.POST_POSITION_CONFIRMATION, param, mean, Mean.class,
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
        MapActivity.setDraggingMode(true);
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
