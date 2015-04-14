package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.Position;
import fr.m2gla.istic.projet.model.Topographie;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

public class MapActivity extends Activity {

    public enum Symbols {
        colonne_incendie_active,
        groupe_incendie_actif,
        moyen_intervention_aerien_actif,
        moyen_intervention_aerien_prevu,
        pc_colonne_deux_fonctions_actif,
        pc_site,
        point_ravitaillement,
        poste_commandement_prevu,
        prise_eau_non_perenne,
        prise_eau_perenne,
        secours_a_personnes_actif,
        secours_a_personnes_prevu,
        vehicule_incendie_seul_actif,
        vehicule_incendie_seul_prevu,
        danger,
        etoile,
        point_sensible
    }

    private static MapFragment mapFragment;
    private GoogleMap map;
    private static final String TAG = "MapActivity";
    private ClusterManager<SymbolMarkerClusterItem> mClusterManager;

    // latitude and longitude
    double latitude = 48.1119800 ;
    double longitude = -1.6742900;

    private static final int OFFSET_X = -100;
    private static final int OFFSET_Y = 30;

    private Map<String, String> param;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        param = new HashMap<String, String>();
        param.put("id", "");


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();

        final Activity _this = this;

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                new AlertDialog.Builder(_this)
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
                                Log.i(TAG, "Invalide position");
                            }
                        })
                        .show();
            }
        });

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                // TODO mettre en pointiller les icons
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // Not use
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
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
        });

        //Obtention de la référence du service
        //locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, map);
        mClusterManager.setRenderer(new SymbolRendered(this, map, mClusterManager));

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        loadSymbols();
        mapFragment.getView().setOnDragListener(new AdapterView.OnDragListener(){
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (event.getAction()==DragEvent.ACTION_DROP) {
                    Log.i("MapActivity", event.toString());

                    LatLng latlng = map.getProjection().fromScreenLocation(new Point((int)event.getX() + OFFSET_X, (int)event.getY() + OFFSET_Y));
                    createSymbolMarker(latlng.latitude, latlng.longitude, Symbols.vehicule_incendie_seul_prevu, "AAA", "BBB", "ff0000", "");
                    mClusterManager.cluster();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
                        Position pos = new Position();
                        for (int i = 0; i < topographies.length; i++) {
                            pos = topographies[i].getPosition();
                            //Draw a symbol with texts and color at a position
                            createSymbolMarker(pos.getLatitude(), pos.getLongitude(),
                                    Symbols.valueOf(topographies[i].getFilename()),
                                    topographies[i].getFirstContent(), topographies[i].getSecondContent(), topographies[i].getColor(),
                                    topographies[i].getFirstContent()
                            );
                        }

                        mClusterManager.cluster();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pos.getLatitude(), pos.getLongitude()), 15));
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
                        createSymbolMarker(latitude, longitude, Symbols.secours_a_personnes_prevu,
                                "SAP", "REN", "FF0000", "SAP REN");
                    }
                });
    }

    /**
     * Creates a map marker at chosen coordinates using the given resource name, text contents and color.
     *
     * @param latitude       latitude from given point
     * @param longitude      longitude from given point
     * @param resourceSymbol resource name from enumeration used to choose symbol type
     * @param textContent1   first text
     * @param textContent2   second text
     * @param hexaColor      symbol color
     */
    public void createSymbolMarker(double latitude, double longitude, Symbols resourceSymbol, String textContent1, String textContent2, String hexaColor, String description) {
        try {
            //Mise à jour des coordonnées
            final LatLng latLng = new LatLng(latitude, longitude);
            //InputStream is = getApplicationContext().getResources().openRawResource(R.raw.colonne_incendie_active);
            InputStream is = getApplicationContext().getResources().openRawResource(getResources().getIdentifier(resourceSymbol.name(), "raw", getPackageName()));
            SVG svg = SVG.getFromInputStream(SVGAdapter.modifySVG(is, textContent1, textContent2, hexaColor));
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            Bitmap image = Bitmap.createScaledBitmap(SVGAdapter.convertToBitmap(drawable, 64, 64), 50, 50, true);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(image);

            SymbolMarkerClusterItem marketItem = new SymbolMarkerClusterItem(latitude, longitude, icon, description);
            mClusterManager.addItem(marketItem);

        } catch (SVGParseException ignored) {
        }
    }

    public class SymbolMarkerClusterItem implements ClusterItem {
        private final LatLng mPosition;
        private BitmapDescriptor icon;
        private String description;

        public SymbolMarkerClusterItem(double lat, double lng, BitmapDescriptor icon, String description) {
            mPosition = new LatLng(lat, lng);
            this.icon = icon;
            this.description = description;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        public BitmapDescriptor getIcon() {
            return icon;
        }
    }

    /**
     * Cluster rendered used to draw firemen symbols
     */
    public class SymbolRendered extends DefaultClusterRenderer<SymbolMarkerClusterItem> {
        public SymbolRendered(Context context, GoogleMap map, ClusterManager<SymbolMarkerClusterItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(SymbolMarkerClusterItem item,
                                                   MarkerOptions markerOptions) {
            markerOptions.icon(item.getIcon());
            markerOptions.draggable(true);
            markerOptions.title(item.description);
        }
    }
}