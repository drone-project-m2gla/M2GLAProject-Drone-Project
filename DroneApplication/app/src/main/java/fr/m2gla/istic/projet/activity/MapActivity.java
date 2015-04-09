package fr.m2gla.istic.projet.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
        //On affiche dans un Toat la nouvelle Localisation
        final StringBuilder msg = new StringBuilder("lat : ");
        msg.append(latitude);
        msg.append( "; lng : ");
        msg.append(longitude);

        Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show();

        //Mise à jour des coordonnées
        final LatLng latLng = new LatLng(latitude, longitude);
        try
        {
            SVG svg = SVG.getFromResource(this, R.raw.point_revitaillement);
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            Bitmap bitmapImg = Bitmap.createScaledBitmap(convertToBitmap(drawable,64,64), 100, 100, true);
            createSymbolMarker(latitude, longitude, bitmapImg);
        }
        catch(SVGParseException e)
        {}


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        //marker.setPosition(latLng);
    }



    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    // methode afficher positions
    public void createSymbolMarker(double latitude, double longitude, Bitmap image) {


        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(50, 30, conf);
        Canvas canvas = new Canvas(bmp);

        Paint color = new Paint();
        color.setTextSize(6);
        color.setColor(Color.WHITE);


        canvas.drawBitmap(Bitmap.createScaledBitmap(image, 50, 30, true), 0, 0, color);

        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                .anchor(0.5f, 1)).showInfoWindow();
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