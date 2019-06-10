package com.example.myiotdevice;



import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    public SensorManager senSensorManager;
    public Sensor senAccelerometer,senPressure;
    public Thread timeThread;
    private HandlerThread mAccelerometerThread,mPressureThread;
    private Handler mAccelerometerHandler,mPressureHandler;
    public MySensorListener msl;
    public double latitude;
    public double longitude;
    public String address;
    public TextView tvAddress;
    public TextView position_latitude;
    public TextView position_longitude;
    public TextView position_address;
    public TextView acceleration_coordinateX;
    public TextView acceleration_coordinateY;
    public TextView acceleration_coordinateZ;
    public TextView pressure_view;
    public TextView altitude_view;

    public CheckBox acceleration_cb;
    public CheckBox pressure_cb;
    public CheckBox altitude_cb;

    public String accelerometer_name;
    public String pressure_name;
    public String accelerometer_resolution;
    public String accelerometer_vendor;
    public String accelerometer_type;

    public String pressure_accuracy;
    public String pressure_vendor;
    public String pressure_type;
    public String altimeter_type;

    public FloatingActionButton fab;

    public Record accelerometerRecord;
    public Record pressureRecord;
    public Record altimeterRecord;


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
        tvAddress = (TextView) findViewById(R.id.MainPosition);
        position_latitude = (TextView) findViewById(R.id.item_value_1_position);
        position_longitude =(TextView) findViewById(R.id.item_value_2_position);
        position_address = (TextView) findViewById(R.id.item_value_3_position);
        acceleration_coordinateX = (TextView) findViewById(R.id.item_value_1_acceleration);
        acceleration_coordinateY = (TextView) findViewById(R.id.item_value_2_acceleration);
        acceleration_coordinateZ = (TextView) findViewById(R.id.item_value_3_acceleration);
        pressure_view = (TextView) findViewById(R.id.item_splitted_value_1_pressure);
        altitude_view = (TextView) findViewById(R.id.item_splitted_value_2_altitude);

        acceleration_cb = (CheckBox) findViewById(R.id.checkbox_acceleration);
        altitude_cb=(CheckBox) findViewById(R.id.checkbox_altitude);
        pressure_cb = (CheckBox) findViewById(R.id.checkbox_pressure);

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
        fab =findViewById(R.id.continue_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Publication p = createPublication();
                Intent i = new Intent(MainActivity.this,SecondActivity.class);
                i.putExtra("Publication",p);
                i.putExtra("Address",address);
                i.putExtra("Latitude",String.valueOf(latitude));
                i.putExtra("Longitude",String.valueOf(longitude));
                startActivity(i);
            }
        });



        // Parameters shared by all the sensors
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        msl = new MySensorListener(this);



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
    }

    @Override
    protected void onResume(){
        super.onResume();
        senSensorManager.registerListener(msl,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL,mAccelerometerHandler);
        senSensorManager.registerListener(msl,senPressure,SensorManager.SENSOR_DELAY_NORMAL,mPressureHandler);

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

        if (locationManager!=null && locationManager.isProviderEnabled(providerId))
            locationManager.removeUpdates(locationListener);
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
        position_latitude.setText(String.valueOf(latitude));
        position_longitude.setText(String.valueOf(longitude));
    }

    public Publication createPublication(){
        Publication publication = new Publication();

        if(acceleration_cb.isChecked()){
            accelerometerRecord=new Record();
            accelerometerRecord.setSensor_reading_1(acceleration_coordinateX.getText().toString());
            accelerometerRecord.setSensor_reading_2(acceleration_coordinateY.getText().toString());
            accelerometerRecord.setSensor_reading_3(acceleration_coordinateZ.getText().toString());
            accelerometerRecord.setSensor_name(accelerometer_name);
            accelerometerRecord.setSensor_resolution(accelerometer_resolution);
            accelerometerRecord.setSensor_vendor(accelerometer_vendor);
            accelerometerRecord.setSensor_type(accelerometer_type);
            accelerometerRecord.setSensor_latitude(String.valueOf(latitude));
            accelerometerRecord.setSensor_longitude(String.valueOf(longitude));
            accelerometerRecord.setSensor_address(address);
        }

        if(pressure_cb.isChecked()){
            pressureRecord = new Record();
            pressureRecord.setSensor_reading_1(pressure_view.getText().toString());
            pressureRecord.setSensor_name(pressure_name);
            pressureRecord.setSensor_resolution(pressure_accuracy);
            pressureRecord.setSensor_vendor(pressure_vendor);
            pressureRecord.setSensor_type(pressure_type);
            pressureRecord.setSensor_latitude(String.valueOf(latitude));
            pressureRecord.setSensor_longitude(String.valueOf(longitude));
            pressureRecord.setSensor_address(address);
        }

        if(altitude_cb.isChecked()){
            altimeterRecord = new Record();
            altimeterRecord.setSensor_reading_1(altitude_view.getText().toString());
            altimeterRecord.setSensor_name(pressure_name);
            altimeterRecord.setSensor_resolution(pressure_accuracy);
            altimeterRecord.setSensor_vendor(pressure_vendor);
            altimeterRecord.setSensor_type(pressure_type);
            altimeterRecord.setSensor_latitude(String.valueOf(latitude));
            altimeterRecord.setSensor_longitude(String.valueOf(longitude));
            altimeterRecord.setSensor_address(address);
        }

        publication.records.add(accelerometerRecord);
        publication.records.add(pressureRecord);
        publication.records.add(altimeterRecord);
        publication.setPublication_latitude(String.valueOf(latitude));
        publication.setPublication_longitude(String.valueOf(longitude));
        publication.setPublication_location(String.valueOf(address));
        publication.setStartDate(Calendar.getInstance().getTime());

        return publication;
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
            position_address.setText(address);
        }
    }
}
