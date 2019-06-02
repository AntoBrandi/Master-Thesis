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
    public SensorsAdapter sensorsAdapter;

    public double longitude;
    public double latitude;
    public String address;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    public double altitude;
    public double pressure;
    public double temperature;
    public double light;
    public double roll;
    public double pitch;
    public double azimuth;
    public double gyroscopeX;
    public double gyroscopeY;
    public double gyroscopeZ;


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

        //sensors.add(new Sensors(null,null,null,null,null,null,null,"Pressione","Altitudine","1000kPa","110m"));

        sensorsAdapter = new SensorsAdapter(this,sensors);
        sensors.add(new Sensors("Posizione","Latitudine","Longitudine","Indirizzo",String.valueOf(latitude),String.valueOf(longitude),address,null,null,null,null));
        sensors.add(new Sensors("Accelerazione","X","Y","Z",String.valueOf(accelerationX),String.valueOf(accelerationY),String.valueOf(accelerationZ),null,null,null,null));
        sensors.add(new Sensors(null,null,null,null,null,null,null,"Pressione","Altitudine",String.valueOf(pressure),String.valueOf(altitude)));
        sensors.add(new Sensors(null,null,null,null,null,null,null,"Temperatura","Luce",String.valueOf(temperature),String.valueOf(light)));
        sensors.add(new Sensors("Orientamento","Pitch","Roll","Azimuth",String.valueOf(pitch),String.valueOf(roll),String.valueOf(azimuth),null,null,null,null));
        sensors.add(new Sensors("Giroscopio","X","Y","Z",String.valueOf(gyroscopeX),String.valueOf(gyroscopeY),String.valueOf(gyroscopeZ),null,null,null,null));



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
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        updateText(R.id.latitude,String.valueOf(latitude));
        updateText(R.id.longitude,String.valueOf(longitude));
        new AddressSolver(this).execute(location);
        TextView MainPosition = (TextView) findViewById(R.id.MainPosition);
        address=MainPosition.getText().toString();


        // Update the list View
        // TODO Aggiungo tutti gli elementi dell'arraylist in una prima istanza e tengo traccia delle loro posizioni nell'ordine. Nel momento in cui c'è una modifica aggiorno soltanto gli elementi dell'array list

    }

    private void updateText(int id,String text){
        TextView textView = (TextView) findViewById(id);
        textView.setText(text);
    }
    // ------ END OF GPS AND ADDRESS SECTION -------------------------------------------------------


}
