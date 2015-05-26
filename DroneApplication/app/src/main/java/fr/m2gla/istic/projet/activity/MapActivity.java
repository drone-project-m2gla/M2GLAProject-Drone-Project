package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.activity.mapUtils.ImageDroneRenderer;
import fr.m2gla.istic.projet.activity.mapUtils.ImageMarkerClusterItem;
import fr.m2gla.istic.projet.activity.mapUtils.SymbolRenderer;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.fragments.DroneTargetActionFragment;
import fr.m2gla.istic.projet.fragments.MoyensInitFragment;
import fr.m2gla.istic.projet.fragments.MoyensSuppFragment;
import fr.m2gla.istic.projet.activity.mapUtils.MapListeners;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.Position;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.activity.mapUtils.SymbolMarkerClusterItem;
import fr.m2gla.istic.projet.model.Topographie;
import fr.m2gla.istic.projet.observer.ObserverTarget;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanMove;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanValidatePosition;
import fr.m2gla.istic.projet.strategy.impl.StrategyMoveDrone;


public class MapActivity extends Activity implements ObserverTarget {
    private static final String TAG = "MapActivity";
    private static final int ZOOM_INDEX = 18;
    private MapFragment mapFragment;
    private boolean isDragging;

    public GoogleMap map;
    public Map<String, String> restParams;

    private ClusterManager<SymbolMarkerClusterItem> meansTopoClusterManager;
    private ClusterManager<ImageMarkerClusterItem> droneClusterManager;
    private MapListeners mapListeners;

    private Menu menu;
    private boolean isDroneMode;
    private Circle drone;
    private List<Polyline> polylineList;
    private DroneTargetActionFragment droneTargetActionFragment;


    /**
     * Methode renvoyant si le mode de fonctionnement de la carte est le mode drone
     * @return true si mode drone, false sinon
     */
    public boolean isDroneMode() {
        return isDroneMode;
    }


    /**
     * Methode renvoyant la liste des trajets successifs du drone
     * @return liste demandée
     */
    public List<Polyline> getPolylineList() {
        return polylineList;
    }


    public ClusterManager<ImageMarkerClusterItem> getDroneClusterManager() {
        return droneClusterManager;
    }

    public DroneTargetActionFragment getDroneTargetActionFragment() {
        return droneTargetActionFragment;
    }

    @Override
    protected void onDestroy() {
        StrategyMoveDrone.getINSTANCE().setActivity(null);
        super.onDestroy();
    }


    /**
     * Methode Principale de l'activité de gestion de la carte
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        isDroneMode = false;
        isDragging = false;
        polylineList = new ArrayList<Polyline>();
        restParams = new HashMap<String, String>();

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();

        map.getUiSettings().setCompassEnabled(true);

        loadIntervention();

        mapListeners = new MapListeners();

        mapListeners.setMapActivity(this);
        // Set marker's drag listener to control marker drag & drop behaviour
        map.setOnMarkerDragListener(mapListeners);

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        meansTopoClusterManager = new ClusterManager<>(this, map);
        SymbolRenderer symbolRenderer = new SymbolRenderer(this, map, meansTopoClusterManager);
        symbolRenderer.setContext(getApplicationContext());
        symbolRenderer.setMapListeners(mapListeners);

        meansTopoClusterManager.setRenderer(symbolRenderer);

        droneClusterManager = new ClusterManager<>(this, map);
        ImageDroneRenderer imageDroneRenderer = new ImageDroneRenderer(this, map, droneClusterManager);

        droneClusterManager.setRenderer(imageDroneRenderer);

        // Set map fragment drag listener to recover dropped symbols
        mapFragment.getView().setOnDragListener(mapListeners);

        // Set map long click listener to draw drone target
        map.setOnMapLongClickListener(mapListeners);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(meansTopoClusterManager);
        map.setOnMarkerClickListener(meansTopoClusterManager);

        // Enable info window click on each cluster element
        map.setOnInfoWindowClickListener(mapListeners);

        droneTargetActionFragment = (DroneTargetActionFragment) getFragmentManager().findFragmentById(R.id.drone_targer_action);
        droneTargetActionFragment.addObserver(this);

        findViewById(R.id.fragment_moyens_init).setVisibility(View.VISIBLE);
        findViewById(R.id.fragment_moyens_supp).setVisibility(View.VISIBLE);
        findViewById(R.id.drone_targer_action).setVisibility(View.INVISIBLE);

        // Add activity to strategy
        StrategyMoveDrone.getINSTANCE().setActivity(this);
        StrategyMeanMove.getINSTANCE().setActivity(this);
        StrategyMeanValidatePosition.getINSTANCE().setActivity(this);

        loadTopographicSymbols();
    }


    /**
     * Methode de creation du menu de l'activité
     *
     * @param menu : Objet de definition du menu principal
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);

        menu.getItem(0).setTitle(R.string.switch_dorne_mode);
        return true;
    }


    /**
     * Methode de gestion de l'usage du menu de l'activité
     *
     * @param item : Objet de sélection dans le menu principal
     */
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
            case R.id.disp_mean_table:
                Intent  intent;
                String  idIntervention;

                // Creation d'un intent pour appeler une autre activité (SecondaryActivity)
                intent = new Intent(getApplicationContext(), MeanTableActivity.class);

                // Demander un rafraichissement de la liste des interventions et des details de
                // l'intervention en cours
                MoyensInitFragment fragmentInitlMoyens = (MoyensInitFragment) getFragmentManager().findFragmentById(R.id.fragment_moyens_init);
                idIntervention = fragmentInitlMoyens.getIdIntervention();

                // Ajout de données supplémentaires dans l'intent
                intent.putExtra(GeneralConstants.REF_ACT_IDINTER, "" + idIntervention);

                // Lancement de l'activité, suivante
                startActivity(intent);

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
                        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
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
                            meansTopoClusterManager.addItem(markerItem);
                        }

                        meansTopoClusterManager.cluster();
                        findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
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

        droneClusterManager.clearItems();
        droneClusterManager.cluster();

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
                .color(Color.rgb(143, 0, 71));

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

    public void updateMeans() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadMeansInMap();
            }
        });
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

            restParams.put("id", idIntervention);

            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION, restParams, Intervention.class,
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

    public void loadMeansInMap() {
        if (!isDragging) {
            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION, restParams, Intervention.class,
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
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                Intervention intervention = (Intervention) response;
                List<Mean> meanList = intervention.getMeansList();

                List<Mean> meansWithCoordinates = new ArrayList<Mean>();
                for (Mean m : meanList) {
                    if (!Double.isNaN(m.getCoordinates().getLatitude()) && !Double.isNaN(m.getCoordinates().getLongitude())) {
                        meansWithCoordinates.add(m);
                    }
                }

                meansTopoClusterManager.clearItems();
                mapListeners.markerSymbolLinkMap.clear();
                for (Mean m : meansWithCoordinates) {
                    String meanClass = m.getVehicle().toString();
                    String meanType = Symbol.getImage(meanClass);
                    Symbol symbol = new Symbol(m.getId(),
                            Symbol.SymbolType.valueOf(meanType),
                            meanClass, Symbol.getCityTrigram(),
                            Symbol.getMeanColor(m.getVehicle()));
                    symbol.setValidated(m.isInPosition());
                    SymbolMarkerClusterItem markerItem = new SymbolMarkerClusterItem(
                            m.getCoordinates().getLatitude(),
                            m.getCoordinates().getLongitude(), symbol);

                    meansTopoClusterManager.addItem(markerItem);
                }
                meansTopoClusterManager.cluster();
                findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
            }
        };
    }

    public void setDraggingMode(boolean isDragging){
        this.isDragging = isDragging;
    }
}