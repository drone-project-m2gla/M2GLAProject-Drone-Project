package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.io.InputStream;
import java.util.Random;

public class MapActivity extends Activity {

    public enum Symbols{
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

    public static MapFragment mapFragment;

    //private LocationManager locationManager;
    //private Location location ;

    GoogleMap map;

    private ClusterManager<SymbolMarkerClusterItem> mClusterManager;
    // latitude and longitude
    double latitude = 48.1119800 ;
    double longitude = -1.6742900;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();

        //Obtention de la référence du service
        //locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, map);
        //mClusterManager.setAlgorithm(new GridBasedAlgorithm<SymbolMarkerClusterItem>());
        mClusterManager.setRenderer(new SymbolRendered(this, map, mClusterManager));

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        onLocationChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // onLocation Changed
    public void onLocationChanged() {
        //On affiche dans un Toast la nouvelle Localisation
        Toast.makeText(this, "lat : " + latitude + "; lng : " + longitude, Toast.LENGTH_SHORT).show();

        Random rnd = new Random();
        for (int i = 0; i < 50; i++) {
            //Build a 6 character length random uppercase string
            StringBuilder sb = new StringBuilder();
            for (int j=0; j<7; j++) {
                sb.append((char)(rnd.nextInt(25)+65));
            }
            //Draw a random symbol with random texts and random color at a random position
            createSymbolMarker(latitude + rnd.nextDouble() * 0.008 - 0.004,
                               longitude + rnd.nextDouble() * 0.008 - 0.004,
                               Symbols.values()[rnd.nextInt(Symbols.values().length)],
                               sb.substring(0, 3), sb.substring(3, 6),
                                Long.toHexString(Math.round(rnd.nextDouble()*0xFFFFFF)));
        }
    }

    /**
     * Converts a drawable object into a bitmap
     * @param drawable drawable object to convert
     * @param widthPixels output image width
     * @param heightPixels output image height
     * @return converted bitmap
     */
    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    /**
     * Creates a map marker at chosen coordinates using the given resource name, text contents and color.
     * @param latitude latitude from given point
     * @param longitude longitude from given point
     * @param resourceSymbol resource name from enumeration used to choose symbol type
     * @param textContent1 first text
     * @param textContent2 second text
     * @param hexaColor symbol color
     */
    public void createSymbolMarker(double latitude, double longitude, Symbols resourceSymbol, String textContent1, String textContent2, String hexaColor) {
        try {
            //Mise à jour des coordonnées
            final LatLng latLng = new LatLng(latitude, longitude);
            //InputStream is = getApplicationContext().getResources().openRawResource(R.raw.colonne_incendie_active);
            InputStream is = getApplicationContext().getResources().openRawResource(getResources().getIdentifier(resourceSymbol.name(), "raw", getPackageName()));
            SVG svg = SVG.getFromInputStream(SVGAdapter.modifySVG(is, textContent1, textContent2, hexaColor));
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            Bitmap image = Bitmap.createScaledBitmap(convertToBitmap(drawable, 64, 64), 50, 50, true);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(image);

            /*map.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(icon).anchor(0.5f, 0.5f)).showInfoWindow();*/

            SymbolMarkerClusterItem marketItem = new SymbolMarkerClusterItem(latitude, longitude, icon);
            mClusterManager.addItem(marketItem);
            mClusterManager.cluster();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
        catch(SVGParseException ignored){}
    }

    public class SymbolMarkerClusterItem implements ClusterItem {
        private final LatLng mPosition;
        private BitmapDescriptor icon;
        public SymbolMarkerClusterItem(double lat, double lng, BitmapDescriptor icon) {
            mPosition = new LatLng(lat, lng);
            this.icon = icon;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        public BitmapDescriptor getIcon(){
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
        }

        /*@Override
        protected boolean shouldRenderAsCluster(Cluster<SymbolMarkerClusterItem> cluster) {
            return cluster.getSize() > 5; // when count of markers is more than 5, render as cluster
        }*/
    }
/*
    public void onResume() {
        super.onResume();

        //Obtention de la référence du service
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        //Si le GPS est disponible, on s'y abonne
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            abonnementGPS();
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void abonnementGPS() {
        //On s'abonne
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,999999999,999999999, this);
    }

    public void desabonnementGPS() {
        //Si le GPS est disponible, on s'y abonne
        locationManager.removeUpdates(this);
    }

    public void onLocationChanged(final Location location) {
        //On affiche dans un Toat la nouvelle Localisation
        final StringBuilder msg = new StringBuilder("lat : ");
        msg.append(location.getLatitude());
        msg.append( "; lng : ");
        msg.append(location.getLongitude());

        Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show();

        //Mise à jour des coordonnées
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Bitmap bitmapImg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.test), 100, 75, true);
        createMarketOfPerson(location.getLatitude(), location.getLongitude(), bitmapImg);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        //marker.setPosition(latLng);
    }

    // methode afficher positions
    public void createMarketOfPerson(double latitude, double longitude, Bitmap image, String email) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(50, 30, conf);
        Canvas canvas = new Canvas(bmp);

        Paint color = new Paint();
        color.setTextSize(6);
        color.setColor(Color.WHITE);


        canvas.drawBitmap(Bitmap.createScaledBitmap(image, 50, 30, true), 0, 0, color);

        gMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                .anchor(0.5f, 1)
                .title(email)).showInfoWindow();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderDisabled(final String provider) {
        //Si le GPS est désactivé on se désabonne
        if("gps".equals(provider)) {
            desabonnementGPS();
        }
    }

    public void onProviderEnabled(final String provider) {
        //Si le GPS est activé on s'abonne
        if("gps".equals(provider)) {
            abonnementGPS();
        }
    }*/
}