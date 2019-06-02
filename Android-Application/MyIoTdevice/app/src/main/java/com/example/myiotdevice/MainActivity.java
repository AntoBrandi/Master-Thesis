package com.example.myiotdevice;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public  ArrayList<Sensors> sensors;

    // ---- GPS AND ADDRESS VARIABLES ---------------

    private String providerId = LocationManager.GPS_PROVIDER;
    private LocationManager locationManager = null;
    private static final int MIN_DIST = 20;
    private static final int MIN_PERIOD = 30000;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateGUI(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            updateText(R.id.MainPosition,"GPS ENABLED");
        }

        @Override
        public void onProviderDisabled(String provider) {
            updateText(R.id.MainPosition,"GPS DISABLED");
        }
    };
    // ---- END OF GPS AND ADDRESS VARIABLES ----------



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ---- ASK FOR LOCATION PERMISSIONS ----
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }


        // ---- DATE AND TIME ----
        Thread timeThread = null;
        Runnable myRunnableThread = new CountDownRunner(this);
        timeThread = new Thread(myRunnableThread);
        timeThread.start();

        // ---- POPULATING THE LIST VIEW WITH SENSORS DATA ----
        sensors = new ArrayList<Sensors>();

        sensors.add(new Sensors(null,null,null,null,null,null,null,"Pressione","Altitudine","1000kPa","110m"));
        sensors.add(new Sensors("Posizione","Latitudine","Longitudine","Indirizzo","Value1","Value2","Value3",null,null,null,null));

        SensorsAdapter sensorsAdapter = new SensorsAdapter(this,sensors);

        ListView mainListView = (ListView) findViewById(R.id.MainListView);
        mainListView.setAdapter(sensorsAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null)
                updateGUI(location);
            if (locationManager != null && locationManager.isProviderEnabled(providerId))
                updateText(R.id.MainPosition, "GPS ENABLED");
            else
                updateText(R.id.MainPosition, "GPS DISABLED");
            locationManager.requestLocationUpdates(providerId, MIN_PERIOD, MIN_DIST, locationListener);
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this,"Error creating location service: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.println( Log.ERROR,null,"Error creating location service:" +e.getMessage());

        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(locationManager!=null && locationManager.isProviderEnabled(providerId))
            locationManager.removeUpdates(locationListener);
    }



    // ------ GPS AND ADDRESS SECTION --------------------------------------------------------------
    private void updateGUI(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        updateText(R.id.latitude,String.valueOf(latitude));
        updateText(R.id.longitude,String.valueOf(longitude));
        new AddressSolver(this).execute(location);
    }

    private void updateText(int id,String text){
        TextView textView = (TextView) findViewById(id);
        textView.setText(text);
    }
    // ------ END OF GPS AND ADDRESS SECTION -------------------------------------------------------


}
