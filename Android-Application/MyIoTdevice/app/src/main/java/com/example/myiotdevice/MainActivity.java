package com.example.myiotdevice;



import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public  ArrayList<Sensors> sensors;
    public SensorsAdapter sensorsAdapter;
    public SensorManager senSensorManager;
    public Sensor senAccelerometer,senPressure,senTemperature,senLight,senOrientation,senGyroscope;
    public Thread timeThread;
    private HandlerThread mAccelerometerThread,mPressureThread,mTemperatureThread,mLightThread,mOrientationThread,mGyroscopeThread;
    private Handler mAccelerometerHandler,mPressureHandler,mTemperatureHandler,mLightHandler,mOrientationHandler,mGyroscopeHandler;
    public MySensorListener msl;
    private static final int POSITION_INDEX =0;
    public double latitude;
    public double longitude;
    public String address;
    public TextView tvAddress;

    public boolean freeze;
    public SparseBooleanArray checkboxStatus;


    // GPS VARIABLES
    private String providerId = LocationManager.GPS_PROVIDER;
    private LocationManager locationManager=null;
    private static final int MIN_DIST=20;
    private static final int MIN_PERIOD=30000;
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

        }

        @Override
        public void onProviderDisabled(String provider) {
            showSettingsAlert();
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        freeze = false;
        checkboxStatus = new SparseBooleanArray();

        // ASK FOR PERMISSIONS
        // GPS access
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        // INTERNET access
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
            return;
        }

        // SEND DATA
        final FloatingActionButton fab = findViewById(R.id.continue_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fullfill the event class to be printed in XML format
                Intent i = new Intent(MainActivity.this,SendEventActivity.class);
                startActivity(i);
            }
        });

        tvAddress = (TextView) findViewById(R.id.MainPosition);

        // Parameters shared by all the sensors
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        msl = new MySensorListener(this);

        // ---- POPULATING THE LIST VIEW WITH SENSORS DATA ----
        sensors = new ArrayList<Sensors>();
        sensors=assignDefaultParameters(sensors);
        sensorsAdapter = new SensorsAdapter(this,sensors,checkboxStatus);

        ListView mainListView = (ListView) findViewById(R.id.MainListView);
        mainListView.setAdapter(sensorsAdapter);

        // Manage List view Item Click
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox box = (CheckBox) view.findViewById(R.id.checkbox);
                if(!checkboxStatus.get(position,false)){
                    freeze = true;
                    box.setChecked(true);
                    checkboxStatus.put(position,true);
                }
                else{
                    box.setChecked(false);
                    freeze=false;
                    checkboxStatus.put(position,false);
                }

            }
        });



        // SENSORS AND TIME THREAD MANAGEMENT
        // ---- DATE AND TIME THREAD----
        Runnable myRunnableThread = new CountDownRunner(this);
        timeThread = new Thread(myRunnableThread);
        timeThread.start();


        // ---- ACCELEROMETER SENSOR THREAD ----
        // Thread that will be in charge of read the status of sensors and update the view
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelerometerThread = new HandlerThread("Accelerometer Thread",Thread.NORM_PRIORITY);
        mAccelerometerThread.start();
        mAccelerometerHandler = new Handler(mAccelerometerThread.getLooper());
        senSensorManager.registerListener(msl,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL,mAccelerometerHandler);

        // ---- PRESSURE AND ALTITUDE SENSOR THREAD ----
        // Thread that will be in charge of read the status of the pressure sensor and update it
        senPressure = senSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mPressureThread = new HandlerThread("Pressure Thread",Thread.NORM_PRIORITY);
        mPressureThread.start();
        mPressureHandler = new Handler(mPressureThread.getLooper());
        senSensorManager.registerListener(msl,senPressure,SensorManager.SENSOR_DELAY_NORMAL,mPressureHandler);

        // ---- TEMPERATURE AND LIGHT SENSOR THREAD ----
        // Thread that will be in charge of read the status of the temperature and light sensor and update it
        senTemperature = senSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mTemperatureThread = new HandlerThread("Temperature Thread",Thread.NORM_PRIORITY);
        mTemperatureThread.start();
        mTemperatureHandler = new Handler(mTemperatureThread.getLooper());
        senSensorManager.registerListener(msl,senTemperature,SensorManager.SENSOR_DELAY_NORMAL,mTemperatureHandler);

        senLight = senSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mLightThread = new HandlerThread("Light Thread",Thread.NORM_PRIORITY);
        mLightThread.start();
        mLightHandler = new Handler(mLightThread.getLooper());
        senSensorManager.registerListener(msl,senLight,SensorManager.SENSOR_DELAY_NORMAL,mLightHandler);


        // ---- ORIENTATION SENSOR THREAD ----
        // Thread that will be in charge of read the status of the orientation sensor and update it
        senOrientation = senSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mOrientationThread = new HandlerThread("Orientation Thread",Thread.NORM_PRIORITY);
        mOrientationThread.start();
        mOrientationHandler = new Handler(mOrientationThread.getLooper());
        senSensorManager.registerListener(msl,senOrientation,SensorManager.SENSOR_DELAY_NORMAL,mOrientationHandler);

        // ---- GYROSCOPE SENSOR THREAD ----
        // Thread that will be in charge of read the status of the gyroscope sensor and update it
        senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGyroscopeThread = new HandlerThread("Gyroscope Thread",Thread.NORM_PRIORITY);
        mGyroscopeThread.start();
        mGyroscopeHandler = new Handler(mGyroscopeThread.getLooper());
        senSensorManager.registerListener(msl,senGyroscope,SensorManager.SENSOR_DELAY_NORMAL,mGyroscopeHandler);

    }

    @Override
    protected void onResume(){
        super.onResume();
        senSensorManager.registerListener(msl,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL,mAccelerometerHandler);
        senSensorManager.registerListener(msl,senPressure,SensorManager.SENSOR_DELAY_NORMAL,mPressureHandler);
        senSensorManager.registerListener(msl,senTemperature,SensorManager.SENSOR_DELAY_NORMAL,mTemperatureHandler);
        senSensorManager.registerListener(msl,senLight,SensorManager.SENSOR_DELAY_NORMAL,mLightHandler);
        senSensorManager.registerListener(msl,senOrientation,SensorManager.SENSOR_DELAY_NORMAL,mOrientationHandler);
        senSensorManager.registerListener(msl,senGyroscope,SensorManager.SENSOR_DELAY_NORMAL,mGyroscopeHandler);

        // GPS
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

            else if (locationManager != null && !locationManager.isProviderEnabled(providerId))
                showSettingsAlert();
            locationManager.requestLocationUpdates(providerId, MIN_PERIOD, MIN_DIST, locationListener);
        }
        catch(Exception e){}
    }

    @Override
    protected void onPause(){
        super.onPause();
        mAccelerometerThread.quitSafely();
        mPressureThread.quitSafely();
        mTemperatureThread.quitSafely();
        mLightThread.quitSafely();
        mOrientationThread.quitSafely();
        mGyroscopeThread.quitSafely();

        if (locationManager!=null && locationManager.isProviderEnabled(providerId))
            locationManager.removeUpdates(locationListener);
    }


    public ArrayList<Sensors> assignDefaultParameters(ArrayList<Sensors> sensors){
        sensors.add(new Sensors("Posizione","Latitudine","Longitudine","Indirizzo","","","",null,null,null,null));
        sensors.add(new Sensors("Accelerazione","X","Y","Z","","","",null,null,null,null));
        sensors.add(new Sensors(null,null,null,null,null,null,null,"Pressione","Altitudine","",""));
        sensors.add(new Sensors(null,null,null,null,null,null,null,"Temperatura","Luce","",""));
        sensors.add(new Sensors("Orientamento","Pitch","Roll","Azimuth","","","",null,null,null,null));
        sensors.add(new Sensors("Giroscopio","X","Y","Z","","","",null,null,null,null));
        return sensors;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void updateGUI(Location location)
    {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(latitude, longitude,getApplicationContext(), new GeocoderHandler());
        sensors.set(POSITION_INDEX,new Sensors("Posizione","Latitudine","Longitudine","Indirizzo",String.valueOf(latitude),String.valueOf(longitude),address,null,null,null,null));
        if (freeze==false) {
            sensorsAdapter.notifyDataSetChanged();
        }
    }

    private class GeocoderHandler extends Handler {

        @Override
        public void handleMessage(Message message) {


            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    address = bundle.getString("address");
                    break;
                default:
                    address = null;
            }

            tvAddress.setText(address);
            sensors.set(POSITION_INDEX,new Sensors("Posizione","Latitudine","Longitudine","Indirizzo",String.valueOf(latitude),String.valueOf(longitude),address,null,null,null,null));
            if (freeze==false) {
                sensorsAdapter.notifyDataSetChanged();
            }
        }

    }

}
