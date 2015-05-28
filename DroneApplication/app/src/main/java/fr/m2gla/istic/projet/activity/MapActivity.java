package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.m2gla.istic.projet.activity.mapUtils.MapListeners;
import fr.m2gla.istic.projet.activity.mapUtils.RefreshAlarmManager;
import fr.m2gla.istic.projet.activity.mapUtils.SymbolMarkerClusterItem;
import fr.m2gla.istic.projet.activity.mapUtils.SymbolRenderer;
import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.fragments.DroneTargetActionFragment;
import fr.m2gla.istic.projet.fragments.MoyensInitFragment;
import fr.m2gla.istic.projet.fragments.MoyensSuppFragment;
import fr.m2gla.istic.projet.model.GeoImage;
import fr.m2gla.istic.projet.model.Intervention;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.Position;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.model.Topographie;
import fr.m2gla.istic.projet.observer.ObserverTarget;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;
import fr.m2gla.istic.projet.strategy.impl.StrategyImageDrone;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanMove;
import fr.m2gla.istic.projet.strategy.impl.StrategyMeanValidatePosition;
import fr.m2gla.istic.projet.strategy.impl.StrategyMoveDrone;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap;


public class MapActivity extends Activity implements ObserverTarget {
    private static final String TAG = "MapActivity";
    private static final int ZOOM_INDEX = 18;
    private MapFragment mapFragment;
    private boolean isDragging;

    // Carte Google Maps
    public GoogleMap map;
    // Paramètres d'appel de la requête Rest
    public Map<String, String> restParams;

    // Cluster managers pour gérer les trois types de marqueurs
    private ClusterManager<SymbolMarkerClusterItem> meansClusterManager;
    private ClusterManager<SymbolMarkerClusterItem> topoClusterManager;


    // List of Arrows when drawing drone path
    private List<GroundOverlay> dronePathArrowImageList;

    // Définition des écouteurs sur la carte et les marqueurs
    private MapListeners mapListeners;

    private Menu menu;
    private boolean isDroneMode;
    private Circle drone;
    private List<Polyline> polylineList;
    private DroneTargetActionFragment droneTargetActionFragment;
    private List<Marker> droneMarkers;
    private RefreshAlarmManager refreshAlarmManager;

    /**
     * Methode renvoyant si le mode de fonctionnement de la carte est le mode drone
     *
     * @return true si mode drone, false sinon
     */
    public boolean isDroneMode() {
        return isDroneMode;
    }

    /**
     * Methode renvoyant la liste des trajets successifs du drone
     *
     * @return liste demandée
     */
    public List<Polyline> getPolylineList() {
        return polylineList;
    }

    public DroneTargetActionFragment getDroneTargetActionFragment() {
        return droneTargetActionFragment;
    }

    public List<Marker> getDroneMarkers()  {
        return droneMarkers;
    }

    @Override
    protected void onDestroy() {
        StrategyMoveDrone.getINSTANCE().setActivity(null);
        refreshAlarmManager.UnregisterAlarmBroadcast();
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
        droneMarkers = new ArrayList<Marker>();

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();

        map.getUiSettings().setCompassEnabled(true);

        loadIntervention();

        mapListeners = new MapListeners();

        mapListeners.setMapActivity(this);
        // Définit l'écouteur sur l'événement drag des marqueurs
        map.setOnMarkerDragListener(mapListeners);

        // Initialise le cluster manager avec la carte et le contexte
        meansClusterManager = new ClusterManager<>(this, map);
        topoClusterManager = new ClusterManager<>(this, map);
        SymbolRenderer meanSymbolRenderer = new SymbolRenderer(this, map, meansClusterManager);
        meanSymbolRenderer.setContext(getApplicationContext());
        meanSymbolRenderer.setMapListeners(mapListeners);

        meansClusterManager.setRenderer(meanSymbolRenderer);

        SymbolRenderer topoSymbolRenderer = new SymbolRenderer(this, map, topoClusterManager);
        topoSymbolRenderer.setContext(getApplicationContext());
        topoClusterManager.setRenderer(topoSymbolRenderer);

        droneClusterManager = new ClusterManager<>(this, map);
        ImageDroneRenderer imageDroneRenderer = new ImageDroneRenderer(this, map, droneClusterManager);

        droneClusterManager.setRenderer(imageDroneRenderer);

        dronePathArrowImageList = new ArrayList<>();

        // Définit les écouteurs pour les événements glisser et déposer afin de récupérer les symboles déplacés sur la carte
        mapFragment.getView().setOnDragListener(mapListeners);

        // Définit l'action d'un clic long sur la carte pour dessiner la cible du drone
        map.setOnMapLongClickListener(mapListeners);

        // Lie les écouteurs de la carte à ceux implémentés par le cluster manager des moyens.
        map.setOnCameraChangeListener(meansClusterManager);
        map.setOnMarkerClickListener(meansClusterManager);

        // Active l'info window dans chaque marqueur de la carte
        map.setOnInfoWindowClickListener(mapListeners);

        droneTargetActionFragment = (DroneTargetActionFragment) getFragmentManager().findFragmentById(R.id.drone_targer_action);
        droneTargetActionFragment.addObserver(this);

        findViewById(R.id.fragment_moyens_init).setVisibility(View.VISIBLE);
        findViewById(R.id.fragment_moyens_supp).setVisibility(View.VISIBLE);
        findViewById(R.id.drone_targer_action).setVisibility(View.INVISIBLE);

        loadTopographicSymbols();

        // Utiliser un timer pour rafraîchir les moyens de manière périodique
        refreshAlarmManager = new RefreshAlarmManager(this, new Command() {
            @Override
            public void execute(Object response) {
                Log.d(TAG, "Mise à jour périodique des moyens");
                //mettre à jour la carte
                updateMeans();
                MoyensInitFragment moyensInitFragment = ((MoyensInitFragment) getFragmentManager().findFragmentById(R.id.fragment_moyens_init));
                // mettre à jour les listes de moyens
                moyensInitFragment.demandMeanStrategy(null);
                moyensInitFragment.arrivedMeanStrategy(null);
                moyensInitFragment.transitMeanStrategy(null);
                moyensInitFragment.refusedMeanStrategy(null);
            }
        }, 30000L);
        refreshAlarmManager.RegisterAlarmBroadcast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        linkActivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        linkActivity();
    }

    /**
     * Add activity to strategy
     */
    private void linkActivity() {
        StrategyMoveDrone.getINSTANCE().setActivity(this);
        StrategyImageDrone.getINSTANCE().setActivity(this);
        StrategyMeanMove.getINSTANCE().setActivity(this);
        StrategyMeanValidatePosition.getINSTANCE().setActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unlinkActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unlinkActivity();
    }

    /**
     * Remove activity to strategy
     */
    private void unlinkActivity() {
        StrategyMoveDrone.getINSTANCE().setActivity(null);
        StrategyImageDrone.getINSTANCE().setActivity(null);
        StrategyMeanMove.getINSTANCE().setActivity(null);
        StrategyMeanValidatePosition.getINSTANCE().setActivity(null);
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
                Intent intent;
                String idIntervention;

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
     * Charge les symboles topographique à l'aide du service REST
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
                            topoClusterManager.addItem(markerItem);
                        }

                        topoClusterManager.cluster();
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

        for (int i = 0; i < droneMarkers.size(); i++) {
            Marker m = droneMarkers.get(i);
            m.remove();
        }


        for (int i = 0; i< dronePathArrowImageList.size(); i++){
            dronePathArrowImageList.get(i).remove();
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
                .geodesic(true)
                .color(Color.rgb(143, 0, 71));

        polylineList.add(map.addPolyline(polylineOptions));

        double xx = (pos1.getLongitude() + pos2.getLongitude())/2;
        double yy = (pos1.getLatitude() + pos2.getLatitude())/2;

        LatLng positionMiddle = new LatLng(yy, xx);

        double delta_x = pos2.getLongitude() - pos1.getLongitude();
        double delta_y = pos2.getLatitude() - pos1.getLatitude();

        float angle = (float) Math.toDegrees(Math.atan2(delta_y, delta_x));

        //Set the new marker to the overlay
        BitmapDrawable overlayItem = rotateDrawable(-angle);
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(fromBitmap(overlayItem.getBitmap()))
                .position(positionMiddle, 15f, 15f);

        // Add an overlay to the map, retaining a handle to the GroundOverlay object.
        //    mapActivity.map.addGroundOverlay(newarkMap);
        this.getDronePathArrowImageList().add(this.map.addGroundOverlay(newarkMap));

    }

    public BitmapDrawable rotateDrawable(float angle)
    {
        Bitmap arrowBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.fleche);
        // Create blank bitmap of equal size
        Bitmap canvasBitmap = arrowBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvasBitmap.eraseColor(0x00000000);

        // Create canvas
        Canvas canvas = new Canvas(canvasBitmap);

        // Create rotation matrix
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.setRotate(angle, canvas.getWidth()/2, canvas.getHeight()/2);

        // Draw bitmap onto canvas using matrix
        canvas.drawBitmap(arrowBitmap, rotateMatrix, null);

        return new BitmapDrawable(this.getResources(),canvasBitmap);
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

    public void imageDrone(final GeoImage image) {
        if (drone != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (Marker m : droneMarkers) {
                        if (positionEqual(m.getPosition(), image.getPosition())) {
                            byte[] byteImage = Base64.decode(image.getImage(), Base64.DEFAULT);
                            Bitmap image = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
                            Bitmap imageMin = Bitmap.createScaledBitmap(image, 50, 50, false);

                            m.setIcon(BitmapDescriptorFactory.fromBitmap(imageMin));
                        }
                    }
                }
            });
        }
    }

    /**
     * Appelle dans un autre thread la méthode pour mettre à jour les marqueurs, mais uniquement si une action de type glisser-déposer n'est pas en cours
     */
    public void updateMeans() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDragging) {
                    loadMeansInMap();
                } else {
                    Toast.makeText(getApplicationContext(), "Un autre utilisateur a effectué des modifications sur les moyens", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Permet de comparer deux positions données dans les formats LatLnt et Position
     *
     * @param pos1 position 1 en format LatLng
     * @param pos2 position 1 en format Position
     * @return True si on doit considérer les positions comme équivalentes
     */
    private boolean positionEqual(LatLng pos1, Position pos2) {
        final double GPS_DELTA = 0.0005;

        boolean latitude1 = Double.compare(pos1.latitude + GPS_DELTA, pos2.getLatitude()) < 1;
        boolean latitude2 = Double.compare(pos1.latitude - GPS_DELTA, pos2.getLatitude()) < 1;
        boolean longitude1 = Double.compare(pos1.longitude + GPS_DELTA, pos2.getLongitude()) < 1;
        boolean longitude2 = Double.compare(pos1.longitude - GPS_DELTA, pos2.getLongitude()) < 1;

        return latitude1 || latitude2 || longitude1 || longitude2;
    }

    /**
     * Charge l'intervention et les moyens sur la carte
     */
    private void loadIntervention() {
        final Intent intent = getIntent();

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
            if (droneTargetActionFragment != null && idIntervention != null) {
                droneTargetActionFragment.setInterventionID(idIntervention);
            }

            restParams.put("id", idIntervention);

            RestServiceImpl.getInstance()
                    .get(RestAPI.GET_INTERVENTION, restParams, Intervention.class,
                            new Command() {
                                @Override
                                public void execute(Object response) {
                                    Intervention intervention = (Intervention) response;
                                    // Montrer l'id de l'intervention et l'adresse dans le titre
                                    getActionBar().setTitle(intervention.getLabel() + " : " +
                                            intervention.getAddress() + " " +
                                            intervention.getPostcode() + " " +
                                            intervention.getCity());

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

    /**
     * Charger les moyens sur la carte
     */
    private void loadMeansInMap() {
        RestServiceImpl.getInstance()
                .get(RestAPI.GET_INTERVENTION, restParams, Intervention.class,
                        getCallbackMeanUpdateSuccess(), getCallbackMeanUpdateError());

    }

    /**
     * Commande à exécuter en cas d'erreur sur une requête REST
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
     * Commande à exécuter lors de la récupération des moyens depuis le serveur rest
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

                meansClusterManager.clearItems();
                mapListeners.markerSymbolLinkMap.clear();
                for (Mean m : meansWithCoordinates) {
                    String meanClass = m.getVehicle().toString();
                    String meanType = Symbol.getImage(meanClass);
                    Symbol symbol = new Symbol(m.getId(),
                            Symbol.SymbolType.valueOf(meanType),
                            meanClass, m.getName(),
                            Symbol.getMeanColor(m.getVehicle()));

                    symbol.setValidated(m.isInPosition());

                    SymbolMarkerClusterItem markerItem = new SymbolMarkerClusterItem(
                            m.getCoordinates().getLatitude(),
                            m.getCoordinates().getLongitude(), symbol);

                    meansClusterManager.addItem(markerItem);
                }
                meansClusterManager.cluster();

                findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
            }
        };
    }

    /**
     * Permet de définir un booléen qui empêche la mise à jour des marquers,
     * lors de l'action glisser-déposer, suite aux actions des autres utilisateurs
     *
     * @param isDragging
     */
    public void setDraggingMode(boolean isDragging) {
        this.isDragging = isDragging;
    }

    /**
     * Permet de définir un booléen qui empêche la mise à jour des marquers,
     * lors de l'action glisser-déposer, suite aux actions des autres utilisateurs
     * @param
     */
    public List<GroundOverlay> getDronePathArrowImageList() {
        return dronePathArrowImageList;
    }
}