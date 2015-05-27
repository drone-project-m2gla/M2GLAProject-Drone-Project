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

    // Ecarts utilisés plus placer l'icône d'un marqueur lorsque celui-ci est déposé
    private static final int OFFSET_X = -100;
    private static final int OFFSET_Y = 30;

    // Ecart utilisé pour éviter le soulèvement des marqueurs lors de l'action glisser
    private static final int SHIFT_RAISE_ON_DRAG = 90;

    // Activité android qui contient la carte
    private MapActivity mapActivity;

    // Map pour garder une trace des symboles utilisés par les moyens par id de marqueur
    public Map<String, SymbolMarkerClusterItem> markerSymbolLinkMap = new HashMap<String, SymbolMarkerClusterItem>();

    public void setMapActivity(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
    }

    /**
     * Ecouteur sur l'événement drag de la carte pour détecter le positionnement d'un marqueur
     * La position est envoyée au serveur et les marqueurs sont mis à jour
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
                                        mapActivity.updateMeans();
                                    }
                                },
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Log.e(TAG, "Post new position error");
                                        Toast.makeText(mapActivity, "Impossible de positionner le moyen : " + response.toString(), Toast.LENGTH_LONG).show();
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
                    .geodesic(true)
                    .width(5)
                    .color(Color.rgb(143, 0, 71));

            mapActivity.getPolylineList().add(mapActivity.map.addPolyline(polylineOptions));
        }

        droneTargetActionFragment.addPosition(position);
    }

    /**
     * Ecouteur sur l'événement drag (glissement) de chaque marqueur
     * Utilisé pour éviter le déplacement du marqueur (soulèvement) lors de son glissement
     *
     * @param marker marqueur déplacé
     */
    @Override
    public void onMarkerDrag(Marker marker) {
        disableRaiseOnDrag(marker);
    }

    /**
     * Ecouteur sur l'événement dragend (fin de glissement) de chaque marqueur
     * Permet d'envoyer la nouvelle position au serveur et de lui indiquer que la nouvelle position n'a pas été validée
     * Met à jour les marqueurs sur la carte
     *
     * @param marker  marqueur déposé
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Disable raiseOnDrag
        disableRaiseOnDrag(marker);

        // éviter null pointer exception lors du clic long
        SymbolMarkerClusterItem symbolMarkerClusterItem = markerSymbolLinkMap.get(marker.getId());
        if (symbolMarkerClusterItem != null) {
            Symbol symbol = symbolMarkerClusterItem.getSymbol();
            if (symbol != null) {
                //Use REST to update position on confirmation
                Position position = new Position();
                position.setLatitude(marker.getPosition().latitude);
                position.setLongitude(marker.getPosition().longitude);
                Mean mean = new Mean();

                mean.setId(symbol.getId());
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
                                        if (markerSymbolLinkMap.containsKey(markerId)) {
                                            mapActivity.updateMeans();
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
        }
    }

    /**
     * Affiche un menu lors d'un clic sur le marqueur d'un moyen, ce qui permet d'effectuer les actions de :
     * Confirmation de position
     * Libération
     * Renvoi au CRM
     * @param marker marqueur sélectionné
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        //Log.d(TAG, "main onClusterItemInfoWindowClick");
        final Symbol meanSymbol = markerSymbolLinkMap.get(marker.getId()).getSymbol();
        final String meanSymbolId = meanSymbol.getId();
        final Marker _marker = marker;
        if (!meanSymbol.isTopographic()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mapActivity).setTitle(R.string.actions_moyens);

            if (!meanSymbol.isValidated()) {
                alertDialogBuilder.setItems(R.array.optionsMoyenEngage, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        switch (which) {
                            case 0: {
                                validateMeanPosition(_marker.getPosition(), meanSymbolId);
                                break;
                            }
                            case 1: {
                                freeMean(meanSymbolId);
                                break;
                            }
                            case 2: {
                                sendMeanBackToCRM(meanSymbolId);
                                break;
                            }
                        }
                    }
                });
            } else {
                alertDialogBuilder.setItems(R.array.optionsMoyenEngageEnPosition, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        switch (which) {
                            case 0: {
                                freeMean(meanSymbolId);
                                break;
                            }
                            case 1: {
                                sendMeanBackToCRM(meanSymbolId);
                                break;
                            }
                        }
                    }
                });
            }
            alertDialogBuilder.show();
        }
    }

    /**
     * Valider la position du moyen
     * @param markerPosition position du moyen
     * @param meanSymbolId id du moyen
     */
    private void validateMeanPosition(LatLng markerPosition, String meanSymbolId) {
        Mean mean = new Mean();
        mean.setId(meanSymbolId);
        Position position = new Position();
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
                                mapActivity.updateMeans();
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
    }

    /**
     * Libérer le moyen
     * Supprimer ses coordonnées
     * Mettre à jour la carte
     * @param meanSymbolId id du moyen
     */
    private void freeMean(String meanSymbolId){
        Mean mean = new Mean();
        mean.setId(meanSymbolId);
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
                                mapActivity.updateMeans();
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
    }

    /**
     * Retour CRM, disponible pour mettre sur la carte
     * Supprimer ses coordonnées
     * Ajouter dans la liste de moyens validés (automatique car la liste se rafraîchit)
     * @param meanSymbolId id du moyen
     */
    private void sendMeanBackToCRM(String meanSymbolId){
        Mean mean = new Mean();
        mean.setId(meanSymbolId);
        Position position = new Position();
        position.setLatitude(Double.NaN);
        position.setLongitude(Double.NaN);
        mean.setCoordinates(position);
        mean.setInPosition(false);

        RestServiceImpl.getInstance()
                .post(RestAPI.POST_RETOURCRM, mapActivity.restParams, mean, Mean.class,
                        new Command() {
                            @Override
                            public void execute(Object response) {
                                Log.e(TAG, "Retour CRM success");
                                mapActivity.updateMeans();
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
    }

    /**
     * Ecouteur sur le début du glissement d'un marqueur pour éviter son soulèvement
     * @param marker marqueur glissé
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        mapActivity.setDraggingMode(true);
        disableRaiseOnDrag(marker);
    }

    /**
     * Dès qu'un marqueur est glissé il est soulévé
     * cette méthode permet d'éviter ce comportement
     * Comportement similaire à l'option raiseOnDrag = false de l'API Google Maps V3
     *
     * @param marker marqueur glissé
     */
    private void disableRaiseOnDrag(Marker marker) {
        LatLng latLng = marker.getPosition();
        Point point = mapActivity.map.getProjection().toScreenLocation(latLng);
        point.set(point.x, point.y + SHIFT_RAISE_ON_DRAG);
        LatLng latLng2 = mapActivity.map.getProjection().fromScreenLocation(point);
        marker.setPosition(latLng2);
    }
}
