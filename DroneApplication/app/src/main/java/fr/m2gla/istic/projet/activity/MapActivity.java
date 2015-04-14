package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

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
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fr.m2gla.istic.projet.command.Command;
import fr.m2gla.istic.projet.context.GeneralConstants;
import fr.m2gla.istic.projet.context.RestAPI;
import fr.m2gla.istic.projet.model.Mean;
import fr.m2gla.istic.projet.model.Position;
import fr.m2gla.istic.projet.model.Symbol;
import fr.m2gla.istic.projet.model.SymbolMarkerClusterItem;
import fr.m2gla.istic.projet.model.Topographie;
import fr.m2gla.istic.projet.service.impl.RestServiceImpl;

public class MapActivity extends Activity implements ClusterManager.OnClusterItemInfoWindowClickListener<SymbolMarkerClusterItem> {

    private static MapFragment mapFragment;
    private GoogleMap map;
    private static final String TAG = "MapActivity";
    private ClusterManager<SymbolMarkerClusterItem> mClusterManager;

    // default latitude and longitude to center map if error
    double latitude = 48.1119800 ;
    double longitude = -1.6742900;

    // offset to place the icon when dropped
    private static final int OFFSET_X = -100;
    private static final int OFFSET_Y = 30;

    private Map<String, String> param;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        param = new HashMap<String, String>();
        param.put("id", "");

        Intent intent = getIntent();

        if (intent != null) {
            String extras = intent.getStringExtra(GeneralConstants.ID_INTERVENTION);
            Toast.makeText(getApplication(), "Bonjour\nID intervention " + extras, Toast.LENGTH_LONG);
        }

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        map = mapFragment.getMap();
        map.getUiSettings().setCompassEnabled(true);

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                // TODO: mettre en pointillés les icones
                Log.d(TAG, "onMarkerDragStart " + marker.getId());

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

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, map);
        mClusterManager.setRenderer(new SymbolRendered(this, map, mClusterManager));

        mapFragment.getView().setOnDragListener(new AdapterView.OnDragListener() {
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
                        createSymbolMarker(latlng.latitude, latlng.longitude, symbol);
                        mClusterManager.cluster();

                    } catch (IllegalArgumentException e){}
                }
                return true;
            }
        });

        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);
        map.setOnInfoWindowClickListener(mClusterManager);
        loadSymbols();
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
                    Position pos = null;
                    for (int i = 0; i < topographies.length; i++) {
                        pos = topographies[i].getPosition();
                        //Draw a symbol with texts and color at a position
                        Symbol symbol = new Symbol(Symbol.SymbolType.valueOf(topographies[i].getFilename()),
                                topographies[i].getFirstContent(),
                                topographies[i].getSecondContent(),
                                topographies[i].getColor(),
                                topographies[i].getFirstContent(),
                                true);
                        createSymbolMarker(pos.getLatitude(), pos.getLongitude(), symbol);
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
                    createSymbolMarker(latitude, longitude, symbol);
                }
            });
    }

    /**
     * Creates a map marker at chosen coordinates using the given resource name, text contents and color.
     *
     * @param latitude       latitude from given point
     * @param longitude      longitude from given point
     * @param symbol         symbol to draw
     */
    public void createSymbolMarker(double latitude, double longitude, Symbol symbol) {
        try {
            InputStream is = getApplicationContext().getResources().openRawResource(getResources().getIdentifier(symbol.getSymbolType().name(), "raw", getPackageName()));
            SVG svg = SVG.getFromInputStream(SVGAdapter.modifySVG(is, symbol.getFirstText(), symbol.getSecondText(), symbol.getColor()));
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            Bitmap image = Bitmap.createScaledBitmap(SVGAdapter.convertDrawableToBitmap(drawable, 64, 64), 50, 50, true);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(image);

            SymbolMarkerClusterItem markerItem = new SymbolMarkerClusterItem(latitude, longitude, icon, symbol);
            mClusterManager.addItem(markerItem);

        } catch (SVGParseException ignored) {
        }
    }

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
     * Cluster rendered used to draw firemen symbols
     */
    public class SymbolRendered extends DefaultClusterRenderer<SymbolMarkerClusterItem> {
        public SymbolRendered(Context context, GoogleMap map, ClusterManager<SymbolMarkerClusterItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(SymbolMarkerClusterItem item,
                                                   MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);
            markerOptions.icon(item.getIcon());
            if (!item.getSymbol().isTopographic()) {
                markerOptions.draggable(true);
            }
            markerOptions.title(item.getSymbol().getDescription());
        }
    }
}