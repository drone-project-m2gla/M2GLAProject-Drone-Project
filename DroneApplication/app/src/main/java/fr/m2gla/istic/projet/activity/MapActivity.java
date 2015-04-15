package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.HashMap;
import java.util.Map;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.fragments.MoyensSuppFragment;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.Position;
import fr.m2gla.istic.projet.model.SVGAdapter;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.model.SymbolMarkerClusterItem;
import fr.m2gla.istic.projet.model.Topographie;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

public class MapActivity extends Activity implements
        ClusterManager.OnClusterItemInfoWindowClickListener<SymbolMarkerClusterItem>,
        AdapterView.OnDragListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener

{
    private static final String TAG = "MapActivity";
    // offsets used to place the icon when it is dropped
    private static final int OFFSET_X = -100;
    private static final int OFFSET_Y = 30;

    private static MapFragment mapFragment;

    private GoogleMap map;
    private ClusterManager<SymbolMarkerClusterItem> mClusterManager;
    // Keep track of all the symbols by marker id
    private Map<String, SymbolMarkerClusterItem> markerSymbolLink;

    // default latitude and longitude to center map if error
    private double latitude = 48.1119800 ;
    private double longitude = -1.6742900;

    // Shift used by raise on drag for map markers
    private static final int SHIFT_RAISE_ON_DRAG = 90;

    private Map<String, String> param;

    private boolean isDroneMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        param = new HashMap<String, String>();
        markerSymbolLink = new HashMap<String, SymbolMarkerClusterItem>();

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();

        map.getUiSettings().setCompassEnabled(true);

        loadIntervention();
        // Set map's long click listener to control drone
        map.setOnMapLongClickListener(this);

        // Set marker's drag listener to control marker drag & drop behaviour
        map.setOnMarkerDragListener(this);

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, map);
        mClusterManager.setRenderer(new SymbolRendered(this, map, mClusterManager));

        // Set map fragment drag listener to recover dropped symbols
        mapFragment.getView().setOnDragListener(this);

        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        // Enable info window click on each cluster element
        map.setOnInfoWindowClickListener(mClusterManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_map_mode:
                isDroneMode = !isDroneMode;
                break;
        }
        return true;
    }

    /**
     * Load symbols using topographic REST service
     */
    public void loadSymbols() {
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
            Position pos = null;
            for (Topographie topographie: topographies) {
                pos = topographie.getPosition();
                //Draw a symbol with texts and color at a position
                Symbol symbol = new Symbol(Symbol.SymbolType.valueOf(topographie.getFilename()),
                        topographie.getFirstContent(),
                        topographie.getSecondContent(),
                        topographie.getColor(),
                        topographie.getFirstContent(),
                        true);
                SymbolMarkerClusterItem markerItem = new SymbolMarkerClusterItem(pos.getLatitude(), pos.getLongitude(), symbol);
                mClusterManager.addItem(markerItem);
            }

            if (pos != null) {
                mClusterManager.cluster();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pos.getLatitude(), pos.getLongitude()), 15));
            }
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
            Symbol symbol = new Symbol(Symbol.SymbolType.secours_a_personnes_prevu,"SAP", "REN", "FF0000", "SAP REN");
            SymbolMarkerClusterItem markerItem = new SymbolMarkerClusterItem(latitude, longitude, symbol);
            mClusterManager.addItem(markerItem);
            mClusterManager.cluster();
            }
        });

    }

    /**
     * Item info window click listener
     * Used to show a dialog on info window click to validate the new position
     * @param symbolMarkerClusterItem
     */
    @Override
    public void onClusterItemInfoWindowClick(SymbolMarkerClusterItem symbolMarkerClusterItem) {
        Log.d(TAG, "main onClusterItemInfoWindowClick");
        if (!symbolMarkerClusterItem.getSymbol().isTopographic()) {
            new AlertDialog.Builder(this)
            .setMessage(R.string.query_position_confirmation)
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                Mean mean = new Mean();
                mean.setId("");

                RestServiceImpl.getInstance()
                .post(RestAPI.POST_POSITION_CONFIRMATION, param, mean, Mean.class,
                        new Command() {
                            @Override
                            public void execute(Object response) {
                                Log.i(TAG, "Confirm position success");
                            }
                        },
                        new Command() {
                            @Override
                            public void execute(Object response) {
                                Log.e(TAG, "Confirm position error");
                            }
                        });
                }
            })
            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i(TAG, "Invalid position");
                }
            })
            .show();
        }
    }

    /**
     * Map fragment drag listener used to get the dropped symbol
     * A new symbol is created and added to the map
     * @param v current view
     * @param event drag element
     * @return
     */
    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            Log.i(TAG, event.toString());

            ClipData clipData = event.getClipData();
            try {
                //Get symbol name from ClipData saved onDrag
                Symbol.SymbolType symbolType = Symbol.SymbolType.valueOf((String)clipData.getItemAt(0).getText());
                Log.d(TAG, clipData.getItemAt(0).toString());
                LatLng latlng = map.getProjection().fromScreenLocation(new Point((int) event.getX() + OFFSET_X, (int) event.getY() + OFFSET_Y));
                Symbol symbol = new Symbol(symbolType, "AAA", "BBB", "ff0000", "Description");
                symbol.setValidated(true);
                SymbolMarkerClusterItem markerItem = new SymbolMarkerClusterItem(latlng.latitude, latlng.longitude, symbol);
                mClusterManager.addItem(markerItem);
                mClusterManager.cluster();

            } catch (IllegalArgumentException ignored){}
        }
        return true;
    }

    /**
     * Marker drag listener method used when drag starts after a long click
     * Used to disable raiseOnDrag
     * @param marker dragged marker element
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        disableRaiseOnDrag(marker);
    }

    /**
     * Marker drag listener method used when marker is dragged
     * Used to disable raiseOnDrag
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
        //Change symbol image to dashed one
        if (markerSymbolLink.containsKey(marker.getId())){
            Log.d(TAG, "onMarkerDragStart found " + marker.getId());
            Symbol symbol =  markerSymbolLink.get(marker.getId()).getSymbol();
            symbol.setValidated(false);
            marker.setIcon(SVGAdapter.convertSymbolToIcon(getApplicationContext(), symbol));
        }
        //Disable raiseOnDrag
        disableRaiseOnDrag(marker);

        //Use REST to update position on confirmation
        Position position = new Position();
        position.setLatitude(marker.getPosition().latitude);
        position.setLongitude(marker.getPosition().longitude);
        Mean mean = new Mean();
        mean.setId("");
        mean.setCoordinates(position);

        RestServiceImpl.getInstance()
                .post(RestAPI.POST_POSITION_MOVE, param, mean, Mean.class,
                        new Command() {
                            @Override
                            public void execute(Object response) {
                                Log.e(TAG, "Post new position success");
                            }
                        },
                        new Command() {
                            @Override
                            public void execute(Object response) {
                                Log.e(TAG, "Post new position error");
                            }
                        });
    }

    /**
     * Map long click listener
     * Allows to control drone
     * @param latLng current long click position
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        Position position = new Position();
        position.setLatitude(latLng.latitude);
        position.setLongitude(latLng.longitude);

        RestServiceImpl.getInstance()
           .post(RestAPI.POST_POSITION_DRONE, null, position, Void.class,
                new Command() {
                    @Override
                    public void execute(Object response) {
                        Log.i(TAG, "Drone move");
                    }
                },
                new Command() {
                    @Override
                    public void execute(Object response) {
                        Log.e(TAG, "Push position error");
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
            }
            markerOptions.title(item.getSymbol().getDescription());
        }

        @Override
        protected void onClusterItemRendered(SymbolMarkerClusterItem clusterItem, Marker marker){
            markerSymbolLink.put(marker.getId(), clusterItem);
            Log.d(TAG,"onClusterItemRendered " + marker.getId());
        }
    }

    /**
     * Whenever a marker starts to be dragged it is raised,
     * this method allows to undo this shift to simulate raiseOnDrag = false behaviour
     * @param marker dragged marker
     */
    private void disableRaiseOnDrag(Marker marker){
        LatLng latLng = marker.getPosition();
        Point point = map.getProjection().toScreenLocation(latLng);
        point.set(point.x, point.y + SHIFT_RAISE_ON_DRAG);
        LatLng latLng2 = map.getProjection().fromScreenLocation(point);
        marker.setPosition(latLng2);
    }

    /**
     * Loads current intervention symbols
     */
    private void loadIntervention(){
        Intent intent = getIntent();

        if (intent != null) {
            String idIntervention = intent.getStringExtra(GeneralConstants.ID_INTERVENTION);
            Toast.makeText(getApplication(), "Bonjour\nID intervention " + idIntervention, Toast.LENGTH_LONG).show();
            MoyensSuppFragment mSuppFragment = (MoyensSuppFragment) getFragmentManager().findFragmentById(R.id.fragment_moyens_supp);

            if (mSuppFragment != null){
                mSuppFragment.setInterventionID(idIntervention);
            }

            param.put("id", idIntervention);

            RestServiceImpl.getInstance()
            .get(RestAPI.GET_INTERVENTION, param, Intervention.class,
                new Command() {
                    @Override
                    public void execute(Object response) {
                        Intervention intervention = (Intervention)response;
                        Position pos = intervention.getCoordinates();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pos.getLatitude(), pos.getLongitude()), 15));
                        loadSymbols();
                    }
                },
                new Command() {
                    @Override
                    public void execute(Object response) {
                        Log.e(TAG, "Error get intervention");
                    }
                });
        }
    }

}