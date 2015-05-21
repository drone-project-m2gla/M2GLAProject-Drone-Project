package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.fragments.DroneTargetActionFragment;
import fr.m2gla.istic.projet.fragments.MoyensInitFragment;
import fr.m2gla.istic.projet.fragments.MoyensSuppFragment;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.Position;
import fr.m2gla.istic.projet.model.SVGAdapter;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.model.SymbolMarkerClusterItem;
import fr.m2gla.istic.projet.model.Topographie;
import fr.m2gla.istic.projet.observer.ObserverTarget;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanMove;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanValidatePosition;
import fr.m2gla.istic.projet.strategy.impl.StrategyMoveDrone;

import static fr.m2gla.istic.projet.model.Symbol.SymbolType.valueOf;

public class MapActivity extends Activity implements
        ObserverTarget,
        AdapterView.OnDragListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = "MapActivity";
    // offsets used to place the icon when it is dropped
    private static final int OFFSET_X = -100;
    private static final int OFFSET_Y = 30;

    private static final int ZOOM_INDEX = 18;

    private static MapFragment mapFragment;

    private GoogleMap map;
    private ClusterManager<SymbolMarkerClusterItem> mClusterManager;
    // Keep track of all the symbols by marker id
    private Map<String, SymbolMarkerClusterItem> markerSymbolLink;

    // Shift used by raise on drag for map markers
    private static final int SHIFT_RAISE_ON_DRAG = 90;

    private Map<String, String> param;

    private Menu menu;
    private boolean isDroneMode;
    private static boolean isDragging;
    private Circle drone;
    private List<Polyline> polylineList;
    private List<Circle> circleList;
    private DroneTargetActionFragment droneTargetActionFragment;

    @Override
    protected void onDestroy() {
        StrategyMoveDrone.getINSTANCE().setActivity(null);
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        isDroneMode = false;
        isDragging = false;
        polylineList = new ArrayList<Polyline>();
        circleList = new ArrayList<Circle>();
        param = new HashMap<String, String>();

        markerSymbolLink = new HashMap<String, SymbolMarkerClusterItem>();

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();

        map.getUiSettings().setCompassEnabled(true);

        loadIntervention();

        // Set marker's drag listener to control marker drag & drop behaviour
        map.setOnMarkerDragListener(this);

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, map);
        mClusterManager.setRenderer(new SymbolRendered(this, map, mClusterManager));

        // Set map fragment drag listener to recover dropped symbols
        mapFragment.getView().setOnDragListener(this);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        // Enable info window click on each cluster element
        map.setOnInfoWindowClickListener(this);

        droneTargetActionFragment = (DroneTargetActionFragment) getFragmentManager().findFragmentById(R.id.drone_targer_action);
        droneTargetActionFragment.addObserver(this);

        findViewById(R.id.fragment_moyens_init).setVisibility(View.VISIBLE);
        findViewById(R.id.fragment_moyens_supp).setVisibility(View.VISIBLE);
        findViewById(R.id.drone_targer_action).setVisibility(View.INVISIBLE);

        addDroneListener();

        // Add activity to strategy
        StrategyMoveDrone.getINSTANCE().setActivity(this);
        StrategyMeanMove.getINSTANCE().setActivity(this);
        StrategyMeanValidatePosition.getINSTANCE().setActivity(this);

        loadTopographicSymbols();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);

        menu.getItem(0).setTitle(R.string.switch_dorne_mode);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_map_mode:
                isDroneMode = !isDroneMode;
                if (isDroneMode) {
                    menu.getItem(0).setTitle(R.string.switch_moyen_mode);
                    findViewById(R.id.fragment_moyens_init).setVisibility(View.INVISIBLE);
                    findViewById(R.id.fragment_moyens_supp).setVisibility(View.INVISIBLE);
                    findViewById(R.id.drone_targer_action).setVisibility(View.VISIBLE);
                } else {
                    menu.getItem(0).setTitle(R.string.switch_dorne_mode);
                    findViewById(R.id.fragment_moyens_init).setVisibility(View.VISIBLE);
                    findViewById(R.id.fragment_moyens_supp).setVisibility(View.VISIBLE);
                    findViewById(R.id.drone_targer_action).setVisibility(View.INVISIBLE);
                }
                break;
        }
        return true;
    }

    /**
     * Load symbols using topographic REST service
     */
    private void loadTopographicSymbols() {
        RestServiceImpl.getInstance().get(RestAPI.GET_ALL_TOPOGRAPHIE, null, Topographie[].class,
                new Command() {
                    /**
                     * Success connection
                     *
                     * @param response Response object type Intervention[]
                     */
                    @Override
                    public void execute(Object response) {
                        Topographie[] topographies = (Topographie[]) response;

                        for (Topographie topographie : topographies) {
                            Position pos = topographie.getPosition();
                            //Draw a symbol with texts and color at a position
                            Symbol symbol = new Symbol(Symbol.SymbolType.valueOf(topographie.getFilename()),
                                    topographie.getFirstContent(),
                                    topographie.getSecondContent(),
                                    topographie.getColor(),
                                    true);
                            SymbolMarkerClusterItem markerItem = new SymbolMarkerClusterItem(pos.getLatitude(), pos.getLongitude(), symbol);
                            mClusterManager.addItem(markerItem);
                        }

                        mClusterManager.cluster();
                    }
                }, new Command() {
                    /**
                     * Error connection
                     *
                     * @param response Response error type HttpClientErrorException
                     */
                    @Override
                    public void execute(Object response) {
                        Log.e(TAG, "connection error");
                        Toast.makeText(getApplicationContext(), "Impossible de charger la topographie", Toast.LENGTH_LONG).show();
                    }
                });
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
                final LatLng latlng = map.getProjection().fromScreenLocation(new Point((int) event.getX() + OFFSET_X, (int) event.getY() + OFFSET_Y));

                //Use REST to update position on confirmation
                Position position = new Position();
                position.setLatitude(latlng.latitude);
                position.setLongitude(latlng.longitude);
                Mean mean = new Mean();
                //Get mean id from ClipData saved onDrag
                mean.setId((String) clipData.getItemAt(0).getText());
                mean.setCoordinates(position);

                isDragging = false;
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
                                        loadMeansInMap();
                                    }
                                },
                                new Command() {
                                    @Override
                                    public void execute(Object response) {
                                        Log.e(TAG, "Post new position error");
                                        Toast.makeText(getApplicationContext(), "Impossible de positionner le moyen", Toast.LENGTH_LONG).show();
                                    }
                                });

            } catch (IllegalArgumentException ignored) {
            }
        }
        return true;
    }


    /**
     * Marker drag listener method used when drag starts after a long click
     * Used to disable raiseOnDrag
     *
     * @param marker dragged marker element
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        isDragging = true;
        disableRaiseOnDrag(marker);
    }

    private void addDroneListener() {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (!isDroneMode) return;

                Position position = new Position();
                position.setLatitude(latLng.latitude);
                position.setLongitude(latLng.longitude);

                CircleOptions circleOptions = new CircleOptions()
                        .center(latLng)
                        .radius(0.2)
                        .strokeColor(Color.BLUE);

                circleList.add(map.addCircle(circleOptions));

                if (!droneTargetActionFragment.getTarget().getPositions().isEmpty()) {
                    Position pos = droneTargetActionFragment.getTarget().getPositions().get(droneTargetActionFragment.getTarget().getPositions().size() - 1);

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .add(new LatLng(pos.getLatitude(), pos.getLongitude()))
                            .add(latLng)
                            .width(5)
                            .color(Color.CYAN);

                    polylineList.add(map.addPolyline(polylineOptions));
                }

                droneTargetActionFragment.addPosition(position);
            }
        });
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

        isDragging = false;
        RestServiceImpl.getInstance()
                .post(RestAPI.POST_POSITION_MOVE, param, mean, Mean.class,
                        new Command() {
                            @Override
                            public void execute(Object response) {
                                Log.e(TAG, "Post new position success");
                                //Change symbol image to dashed one
                                if (markerSymbolLink.containsKey(markerId)) {
                                    loadMeansInMap();
                                }
                            }
                        },
                        new Command() {
                            @Override
                            public void execute(Object response) {
                                Log.e(TAG, "Post new position error");
                                Toast.makeText(getApplicationContext(), "Impossible de positionner le moyen", Toast.LENGTH_LONG).show();
                            }
                        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //Log.d(TAG, "main onClusterItemInfoWindowClick");
        final Symbol meanSymbol = markerSymbolLink.get(marker.getId()).getSymbol();
        final Marker _marker = marker;
        if (!meanSymbol.isTopographic()) {
            new AlertDialog.Builder(this)
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
                                                            loadMeansInMap();
                                                        }
                                                    },
                                                    new Command() {
                                                        @Override
                                                        public void execute(Object response) {
                                                            Log.e(TAG, "Confirm position error");
                                                            Toast.makeText(getApplicationContext(), "Impossible de confirmer la position de ce moyen", Toast.LENGTH_LONG).show();
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
                                                            loadMeansInMap();
                                                        }
                                                    },
                                                    new Command() {
                                                        @Override
                                                        public void execute(Object response) {
                                                            Log.e(TAG, "Libérer moyen error");
                                                            Toast.makeText(getApplicationContext(), "Impossible de libérer ce moyen", Toast.LENGTH_LONG).show();
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
                                                            loadMeansInMap();
                                                        }
                                                    },
                                                    new Command() {
                                                        @Override
                                                        public void execute(Object response) {
                                                            Log.e(TAG, "Retour CRM error");
                                                            Toast.makeText(getApplicationContext(), "Impossible de retourner ce moyen au CRM", Toast.LENGTH_LONG).show();
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

    @Override
    public void notifySend() {
        if (drone != null) {
            drone.remove();
        }

        Position pos = droneTargetActionFragment.getTarget().getPositions().get(0);

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(pos.getLatitude(), pos.getLongitude()))
                .radius(0.4)
                .strokeColor(Color.RED);

        drone = map.addCircle(circleOptions);
    }

    @Override
    public void notifyClear() {
        for (Polyline polyline : polylineList) {
            polyline.remove();
        }

        for (Circle circle : circleList) {
            circle.remove();
        }

        polylineList.clear();
    }

    @Override
    public void notifyClose() {
        Position pos1 = droneTargetActionFragment.getTarget().getPositions().get(0);
        Position pos2 = droneTargetActionFragment.getTarget().getPositions().get(droneTargetActionFragment.getTarget().getPositions().size() - 1);

        PolylineOptions polylineOptions = new PolylineOptions()
                .add(new LatLng(pos1.getLatitude(), pos1.getLongitude()))
                .add(new LatLng(pos2.getLatitude(), pos2.getLongitude()))
                .width(5)
                .color(Color.CYAN);

        polylineList.add(map.addPolyline(polylineOptions));
    }

    public void moveDrone(final Position position) {
        if (drone != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    drone.setCenter(new LatLng(position.getLatitude(), position.getLongitude()));
                }
            });
        }
    }

    public void updateMeans(final Mean mean) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadMeansInMap();
            }
        });
    }

    /**
     * Cluster rendered used to draw firemen symbols
     * All the symbols are integrated into the map that links markers and symbols
     * Non-topographic symbols are draggable
     * Description is shown in marker's info window
     */
    public class SymbolRendered extends DefaultClusterRenderer<SymbolMarkerClusterItem> {
        public SymbolRendered(Context context, GoogleMap map, ClusterManager<SymbolMarkerClusterItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(SymbolMarkerClusterItem item,
                                                   MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);
            markerOptions.icon(SVGAdapter.convertSymbolToIcon(getApplicationContext(), item.getSymbol()));
            if (!item.getSymbol().isTopographic()) {
                markerOptions.draggable(true);
                markerOptions.title("Action sur le moyen");
            } else {
                markerOptions.title(item.getSymbol().getFirstText());
            }
        }

        @Override
        protected void onClusterItemRendered(SymbolMarkerClusterItem clusterItem, Marker marker) {
            markerSymbolLink.put(marker.getId(), clusterItem);
            //Log.d(TAG, "onClusterItemRendered " + marker.getId());
        }
    }

    /**
     * Whenever a marker starts to be dragged it is raised,
     * this method allows to undo this shift to simulate raiseOnDrag = false behaviour
     *
     * @param marker dragged marker
     */
    private void disableRaiseOnDrag(Marker marker) {
        LatLng latLng = marker.getPosition();
        Point point = map.getProjection().toScreenLocation(latLng);
        point.set(point.x, point.y + SHIFT_RAISE_ON_DRAG);
        LatLng latLng2 = map.getProjection().fromScreenLocation(point);
        marker.setPosition(latLng2);
    }

    /**
     * Loads current intervention symbols
     */
    private void loadIntervention() {
        Intent intent = getIntent();

        if (intent != null) {
            String idIntervention = intent.getStringExtra(fr.m2gla.istic.projet.context.GeneralConstants.REF_ACT_IDINTER);

            // Fragment ajout de moyens supplémentaires
            MoyensSuppFragment mSuppFragment = (MoyensSuppFragment) getFragmentManager().findFragmentById(R.id.fragment_moyens_supp);

            // Fragment list des moyens supplémentaires
            MoyensInitFragment mInitFragment = (MoyensInitFragment) getFragmentManager().findFragmentById(R.id.fragment_moyens_init);
            if (mSuppFragment != null && idIntervention != null) {
                mSuppFragment.setInterventionID(idIntervention);
            }
            if (mInitFragment != null && idIntervention != null) {
                mInitFragment.setInterventionID(idIntervention);
            }

            param.put("id", idIntervention);

            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION, param, Intervention.class,
                            new Command() {
                                @Override
                                public void execute(Object response) {
                                    Intervention intervention = (Intervention) response;
                                    Position pos = intervention.getCoordinates();

                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pos.getLatitude(), pos.getLongitude()), ZOOM_INDEX));
                                    loadMeansInMap();
                                }
                            },
                            new Command() {
                                @Override
                                public void execute(Object response) {
                                    Log.e(TAG, "Error get intervention");
                                    Toast.makeText(getApplicationContext(), "Impossible de charger l'intervention", Toast.LENGTH_LONG).show();
                                }
                            });
        }
    }

    private void loadMeansInMap() {
        if (!isDragging) {
            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION, param, Intervention.class,
                            getCallbackMeanUpdateSuccess(), getCallbackMeanUpdateError());
        }
    }

    /**
     * Command error
     *
     * @return
     */
    private Command getCallbackMeanUpdateError() {
        return new Command() {
            @Override
            public void execute(Object response) {
                Toast.makeText(getApplicationContext(), "ERROR\nRequête HTTP en échec", Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * Command success
     *
     * @return
     */
    private Command getCallbackMeanUpdateSuccess() {
        return new Command() {
            @Override
            public void execute(Object response) {
                Intervention intervention = (Intervention) response;
                List<Mean> meanList = intervention.getMeansList();

                List<Mean> meansWithCoordinates = new ArrayList<Mean>();
                for (Mean m : meanList) {
                    if (!Double.isNaN(m.getCoordinates().getLatitude()) && !Double.isNaN(m.getCoordinates().getLongitude())) {
                        meansWithCoordinates.add(m);
                    }
                }

                mClusterManager.clearItems();
                markerSymbolLink.clear();
                for (Mean m : meansWithCoordinates) {
                    String meanClass = m.getVehicle().toString();
                    String meanType = Symbol.getImage(meanClass);
                    Symbol symbol = new Symbol(m.getId(),
                            valueOf(meanType), meanClass, Symbol.getCityTrigram(), Symbol.getMeanColor(m.getVehicle()));
                    symbol.setValidated(m.isInPosition());
                    SymbolMarkerClusterItem markerItem = new SymbolMarkerClusterItem(
                            m.getCoordinates().getLatitude(),
                            m.getCoordinates().getLongitude(), symbol);

                    mClusterManager.addItem(markerItem);
                }
                mClusterManager.cluster();
            }
        };
    }

    public static void setDraggingMode(){
        isDragging = true;
    }
}