package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.Random;

import fr.m2gla.istic.projet.R;

public class MapActivity extends Activity {

    public static MapFragment mapFragment;
    public static View myView;

    private LocationManager locationManager;
    private Location location ;

    GoogleMap map;
    private Marker marker;
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

        onLocationChanged();

        //myView = (MyView)findViewById(R.id.myview);
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
        final StringBuilder msg = new StringBuilder("lat : ");
        msg.append(latitude);
        msg.append( "; lng : ");
        msg.append(longitude);

        Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show();

        Random rnd = new Random();

        for (int i = 0; i < 50; i++) {
            //Draw a colonne_incendie_active with texts ABC et DEF and blue hex color
            //createSymbolMarker(latitude, longitude - 0.05, "moyen_intervention_aerien_actif", "ABC", "DEF", "0000FF");
            latitude = latitude + rnd.nextDouble() * 0.008 - 0.004;
            longitude = longitude + rnd.nextDouble() * 0.008 - 0.004;
            createSymbolMarker(latitude, longitude, "colonne_incendie_active", "ABC", "DEF", "0000FF");
            //createSymbolMarker(latitude, longitude, "colonne_incendie_active", "ABC", "DEF", "0000FF");
        }
        //marker.setPosition(latLng);
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    /**
     * Creates a map marker at chosen coordinates using the given resource name, text contents and color.
     * @param latitude
     * @param longitude
     * @param resourceName
     * @param textContent1
     * @param textContent2
     * @param hexaColor
     */
    public void createSymbolMarker(double latitude, double longitude, String resourceName, String textContent1, String textContent2, String hexaColor) {
        try {
            //Mise à jour des coordonnées
            final LatLng latLng = new LatLng(latitude, longitude);
            //InputStream is = getApplicationContext().getResources().openRawResource(R.raw.colonne_incendie_active);
            InputStream is = getApplicationContext().getResources().openRawResource(getResources().getIdentifier(resourceName, "raw", getPackageName()));
            SVG svg = SVG.getFromInputStream(SVGAdapter.modifySVG(is, textContent1, textContent2, hexaColor));
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            Bitmap image = Bitmap.createScaledBitmap(convertToBitmap(drawable, 64, 64), 50, 50, true);

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromBitmap(image))
                    .anchor(0.5f, 0.5f)).showInfoWindow();

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
        catch(SVGParseException e){}
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