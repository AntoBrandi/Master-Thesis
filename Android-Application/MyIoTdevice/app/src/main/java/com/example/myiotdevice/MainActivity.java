package com.example.myiotdevice;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public  ArrayList<Sensors> sensors;
    public SensorsAdapter sensorsAdapter;

    public double longitude;
    public double latitude;
    public String address;
    public float accelerationX;
    public float accelerationY;
    public float accelerationZ;
    public float altitude;
    public float pressure;
    public float temperature;
    public float light;
    public float roll;
    public float pitch;
    public float azimuth;
    public double gyroscopeX;
    public double gyroscopeY;
    public double gyroscopeZ;

    // costanti che contengono l'ordine in cui saranno mostrati i sensori nella listView
    private static final int POSITION_INDEX =0;
    private static final int ACCELERATION_INDEX =1;
    private static final int PRESSURE_ALTITUDE_INDEX =2;
    private static final int TEMPERATURE_LIGHT_INDEX =3;
    private static final int ORIENTATION_INDEX =4;
    private static final int GYROSCOPE_INDEX =5;


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



    // ---- ACCELEROMETER VARIABLES ---------------------------
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    // ---- END OF ACCELEROMETER VARIABLES ---------------------

    // ---- PRESSURE AND ALTIMETER VARIABLES -------------------
    private Sensor senPressure;
    // ---- END OF PRESSURE AND ALTIMETER VARIABLES ------------

    // ---- TEMPERATURE AND LIGHT VARIABLES --------------------
    private Sensor senTemperature;
    private Sensor senLight;
    // ---- END OF TEMPERATURE AND LIGHT VARIABLES -------------

    // ---- ORIENTATION VARIABLES ------------------------------
    private Sensor senOrientation;
    // ---- END OF ORIENTATION VARIABLES -----------------------

    // ---- GYROSCOPE VARIABLES ------------------------------
    private Sensor senGyroscope;
    // ---- END OF GYROSCOPE VARIABLES -----------------------



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
        sensors=assignDefaultParameters(sensors);
        sensorsAdapter = new SensorsAdapter(this,sensors);


        ListView mainListView = (ListView) findViewById(R.id.MainListView);
        mainListView.setAdapter(sensorsAdapter);


        // ---- ACCELEROMETER SECTION ----
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        // ---- END OF ACCELEROMETER SECTION ----

        // ---- PRESSURE AND ALTIMETER SECTION ----
        senPressure = senSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        senSensorManager.registerListener(this,senPressure,SensorManager.SENSOR_DELAY_NORMAL);
        // ---- END OF PRESSURE AND ALTIMETER SECTION ----

        // ---- TEMPERATURE AND LIGHT SECTION ----
        senTemperature = senSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        senLight = senSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        senSensorManager.registerListener(this,senTemperature,SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this,senLight,SensorManager.SENSOR_DELAY_NORMAL);
        // ---- END OF TEMPERATURE AND LIGHT SECTION ----

        // ---- ORIENTATION SECTION ----
        senOrientation = senSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        senSensorManager.registerListener(this,senOrientation,SensorManager.SENSOR_DELAY_NORMAL);
        // ---- END OF ORIENTATION SECTION ----

        // ---- GYROSCOPE SECTION ----
        senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        senSensorManager.registerListener(this,senGyroscope,SensorManager.SENSOR_DELAY_NORMAL);
        // ---- END OF GYROSCOPE SECTION ----


    }

    @Override
    protected void onResume(){
        super.onResume();
        senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this,senPressure,SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this,senTemperature,SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this,senLight,SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this,senOrientation,SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this,senGyroscope,SensorManager.SENSOR_DELAY_NORMAL);

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
        senSensorManager.unregisterListener(this);

        if(locationManager!=null && locationManager.isProviderEnabled(providerId))
            locationManager.removeUpdates(locationListener);
    }


    public ArrayList<Sensors> assignDefaultParameters(ArrayList<Sensors> sensors){
        sensors.add(new Sensors("Posizione","Latitudine","Longitudine","Indirizzo",String.valueOf(latitude),String.valueOf(longitude),address,null,null,null,null));
        sensors.add(new Sensors("Accelerazione","X","Y","Z",String.valueOf(accelerationX),String.valueOf(accelerationY),String.valueOf(accelerationZ),null,null,null,null));
        sensors.add(new Sensors(null,null,null,null,null,null,null,"Pressione","Altitudine",String.valueOf(pressure),String.valueOf(altitude)));
        sensors.add(new Sensors(null,null,null,null,null,null,null,"Temperatura","Luce",String.valueOf(temperature),String.valueOf(light)));
        sensors.add(new Sensors("Orientamento","Pitch","Roll","Azimuth",String.valueOf(pitch),String.valueOf(roll),String.valueOf(azimuth),null,null,null,null));
        sensors.add(new Sensors("Giroscopio","X","Y","Z",String.valueOf(gyroscopeX),String.valueOf(gyroscopeY),String.valueOf(gyroscopeZ),null,null,null,null));
        return sensors;
    }

    // ------ ACCELEROMETER SECTION ----------------------------------------------------------------
    @Override
    public void onSensorChanged(SensorEvent event){
        Sensor mySensor = event.sensor;

        if (mySensor.getType()==Sensor.TYPE_ACCELEROMETER){
            accelerationX = event.values[0];
            accelerationY = event.values[1];
            accelerationZ = event.values[2];

            // Update List View
            sensors.set(ACCELERATION_INDEX,new Sensors("Accelerazione","X","Y","Z",String.valueOf(accelerationX),String.valueOf(accelerationY),String.valueOf(accelerationZ),null,null,null,null));
            sensorsAdapter.notifyDataSetChanged();
        }

        if(mySensor.getType()==Sensor.TYPE_PRESSURE){
            pressure = event.values[0];
            altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE,pressure);

            // Update List View
            sensors.set(PRESSURE_ALTITUDE_INDEX,new Sensors(null,null,null,null,null,null,null,"Pressione","Altitudine",String.valueOf(pressure),String.valueOf(altitude)));
            sensorsAdapter.notifyDataSetChanged();
        }

        if ((mySensor.getType()==Sensor.TYPE_AMBIENT_TEMPERATURE) || (mySensor.getType()==Sensor.TYPE_LIGHT)){
            if(mySensor.getType()==Sensor.TYPE_AMBIENT_TEMPERATURE) {
                temperature = event.values[0];
            }
            else if (mySensor.getType()==Sensor.TYPE_LIGHT){
                light = event.values[0];
            }

            // Update List View
            sensors.set(TEMPERATURE_LIGHT_INDEX,new Sensors(null,null,null,null,null,null,null,"Temperatura","Luce",String.valueOf(temperature),String.valueOf(light)));
            sensorsAdapter.notifyDataSetChanged();
        }

        if (mySensor.getType()==Sensor.TYPE_ROTATION_VECTOR){
            azimuth = event.values[0];
            pitch = event.values[1];
            roll = event.values[2];

            // Update ListView
            sensors.set(ORIENTATION_INDEX,new Sensors("Orientamento","Pitch","Roll","Azimuth",String.valueOf(pitch),String.valueOf(roll),String.valueOf(azimuth),null,null,null,null));
            sensorsAdapter.notifyDataSetChanged();
        }

        if (mySensor.getType()==Sensor.TYPE_GYROSCOPE){
            gyroscopeX = event.values[0];
            gyroscopeY = event.values[1];
            gyroscopeZ = event.values[2];

            // Update ListView
            sensors.set(GYROSCOPE_INDEX,new Sensors("Giroscopio","X","Y","Z",String.valueOf(gyroscopeX),String.valueOf(gyroscopeY),String.valueOf(gyroscopeZ),null,null,null,null));
            sensorsAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
    // ------ END OF ACCELEROMETER SECTION ---------------------------------------------------------



    // ------ GPS AND ADDRESS SECTION --------------------------------------------------------------
    private void updateGUI(Location location){
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        new AddressSolver(this).execute(location);
        TextView MainPosition = (TextView) findViewById(R.id.MainPosition);
        address=MainPosition.getText().toString();


        // Update the list View
        sensors.set(POSITION_INDEX,new Sensors("Posizione","Latitudine","Longitudine","Indirizzo",String.valueOf(latitude),String.valueOf(longitude),address,null,null,null,null));
        sensorsAdapter.notifyDataSetChanged();
    }

    private void updateText(int id,String text){
        TextView textView = (TextView) findViewById(id);
        textView.setText(text);
    }
    // ------ END OF GPS AND ADDRESS SECTION -------------------------------------------------------


}
