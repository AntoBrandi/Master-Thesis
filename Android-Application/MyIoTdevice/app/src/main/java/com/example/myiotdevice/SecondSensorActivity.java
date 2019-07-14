package com.example.myiotdevice;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SecondSensorActivity extends AppCompatActivity {

    public TextView temperature_view;
    public TextView light_view;
    public TextView orientation_coordinatePitch;
    public TextView orientation_coordinateRoll;
    public TextView orientation_coordinateAzimuth;
    public TextView gyroscope_coordinateX;
    public TextView gyroscope_coordinateY;
    public TextView gyroscope_coordinateZ;
    public TextView address_view;
    public CheckBox orientation_cb;
    public CheckBox gyroscope_cb;
    public CheckBox temperature_cb;
    public CheckBox light_cb;
    private HandlerThread mTemperatureThread,mLightThread,mOrientationThread,mGyroscopeThread;
    private Handler mTemperatureHandler,mLightHandler,mOrientationHandler,mGyroscopeHandler;
    public Sensor senTemperature,senLight,senOrientation,senGyroscope;
    public String orientation_name;
    public String gyroscope_name;
    public String temperature_name;
    public String light_name;
    public String gyroscope_accuracy;
    public String gyroscope_vendor;
    public String light_accuracy;
    public String light_vendor;
    public String temperature_accuracy;
    public String temperature_vendor;
    public String orientation_type;
    public String gyroscope_type;
    public String temperature_type;
    public String light_type;
    public ImageButton continue_btn;
    public SensorManager senSensorManager;
    public Thread timeThread;
    public MySecondSensorListener msl;
    public Publication p;
    public String address;
    public String orientation_accuracy;
    public String orientation_vendor;
    public Record orientationRecord;
    public Record gyroscopeRecord;
    public Record temperatureRecord;
    public Record lightRecord;
    private String longitude;
    private String latitude;
    private SimpleDateFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_sensor_activity);

        orientation_coordinatePitch = (TextView) findViewById(R.id.item_value_1_orientation);
        orientation_coordinateRoll = (TextView) findViewById(R.id.item_value_2_orientation);
        orientation_coordinateAzimuth=(TextView) findViewById(R.id.item_value_3_orientation);
        gyroscope_coordinateX = (TextView) findViewById(R.id.item_value_1_gyroscope);
        gyroscope_coordinateY =(TextView) findViewById(R.id.item_value_2_gyroscope);
        gyroscope_coordinateZ = (TextView) findViewById(R.id.item_value_3_gyroscope);
        temperature_view = (TextView) findViewById(R.id.item_splitted_value_1_temperature);
        light_view = (TextView) findViewById(R.id.item_splitted_value_2_light);
        address_view = (TextView) findViewById(R.id.MainPosition);

        orientation_cb = (CheckBox) findViewById(R.id.checkbox_orientation);
        gyroscope_cb = (CheckBox) findViewById(R.id.checkbox_gyroscope);
        temperature_cb = (CheckBox) findViewById(R.id.checkbox_temperature);
        light_cb = (CheckBox) findViewById(R.id.checkbox_light);

        Intent i = getIntent();
        p=(Publication) i.getSerializableExtra("Publication");
        address = (String) i.getStringExtra("Address");
        longitude = (String) i.getStringExtra("Longitude");
        latitude = (String) i.getStringExtra("Latitude");


        df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");


        if(address != null){
            address_view.setText(address);
        }


        // SEND DATA
        continue_btn =findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = createRecord();
                Intent j = new Intent(SecondSensorActivity.this,TypeOfEventActivity.class);
                j.putExtra("Publication",p);
                startActivity(j);
            }
        });


        // Parameters shared by all the sensors
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        msl = new MySecondSensorListener(this);



        // SENSORS AND TIME THREAD MANAGEMENT
        // ---- DATE AND TIME THREAD----
        Runnable myRunnableThread = new SecondCountDownRunner(this);
        timeThread = new Thread(myRunnableThread);
        timeThread.start();

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
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(msl, senTemperature, SensorManager.SENSOR_DELAY_NORMAL, mTemperatureHandler);
        senSensorManager.registerListener(msl, senLight, SensorManager.SENSOR_DELAY_NORMAL, mLightHandler);
        senSensorManager.registerListener(msl, senOrientation, SensorManager.SENSOR_DELAY_NORMAL, mOrientationHandler);
        senSensorManager.registerListener(msl, senGyroscope, SensorManager.SENSOR_DELAY_NORMAL, mGyroscopeHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTemperatureThread.quitSafely();
        mLightThread.quitSafely();
        mOrientationThread.quitSafely();
        mGyroscopeThread.quitSafely();
    }

    public Publication createRecord(){

        if(orientation_cb.isChecked()){
            orientationRecord = new Record();
            orientationRecord.setOverallStartTime(df.format(Calendar.getInstance().getTime()));
            orientationRecord.setSensor_reading_1(orientation_coordinatePitch.getText().toString().matches("")?"null":orientation_coordinatePitch.getText().toString());
            orientationRecord.setSensor_reading_2(orientation_coordinateAzimuth.getText().toString().matches("")?"null":orientation_coordinateAzimuth.getText().toString());
            orientationRecord.setSensor_reading_3(orientation_coordinateRoll.getText().toString().matches("")?"null":orientation_coordinateRoll.getText().toString());
            orientationRecord.setSensor_name(orientation_name);
            orientationRecord.setSensor_resolution(orientation_accuracy);
            orientationRecord.setSensor_vendor(orientation_vendor);
            orientationRecord.setSensor_type(orientation_type);
            orientationRecord.setSensor_latitude(String.valueOf(latitude));
            orientationRecord.setSensor_longitude(String.valueOf(longitude));
            orientationRecord.setSensor_address(address);
            orientationRecord.setOverallEndTime(df.format(Calendar.getInstance().getTime()));

            orientationRecord.ID = "ORIEN";

            p.records.add(orientationRecord);
        }

        if(gyroscope_cb.isChecked()){
            gyroscopeRecord = new Record();
            gyroscopeRecord.setOverallStartTime(df.format(Calendar.getInstance().getTime()));
            gyroscopeRecord.setSensor_reading_1(gyroscope_coordinateX.getText().toString().matches("")?"null":gyroscope_coordinateX.getText().toString());
            gyroscopeRecord.setSensor_reading_2(gyroscope_coordinateY.getText().toString().matches("")?"null":gyroscope_coordinateY.getText().toString());
            gyroscopeRecord.setSensor_reading_3(gyroscope_coordinateZ.getText().toString().matches("")?"null":gyroscope_coordinateZ.getText().toString());
            gyroscopeRecord.setSensor_name(gyroscope_name);
            gyroscopeRecord.setSensor_resolution(gyroscope_accuracy);
            gyroscopeRecord.setSensor_vendor(gyroscope_vendor);
            gyroscopeRecord.setSensor_type(gyroscope_type);
            gyroscopeRecord.setSensor_latitude(String.valueOf(latitude));
            gyroscopeRecord.setSensor_longitude(String.valueOf(longitude));
            gyroscopeRecord.setSensor_address(address);
            gyroscopeRecord.setOverallEndTime(df.format(Calendar.getInstance().getTime()));
            gyroscopeRecord.ID = "GYRO";

            p.records.add(gyroscopeRecord);
        }

        if(light_cb.isChecked()){
            lightRecord = new Record();
            lightRecord.setOverallStartTime(df.format(Calendar.getInstance().getTime()));
            lightRecord.setSensor_reading_1(light_view.getText().toString().matches("")?"null":light_view.getText().toString());
            lightRecord.setSensor_name(light_name);
            lightRecord.setSensor_resolution(light_accuracy);
            lightRecord.setSensor_vendor(light_vendor);
            lightRecord.setSensor_type(light_type);
            lightRecord.setSensor_latitude(String.valueOf(latitude));
            lightRecord.setSensor_longitude(String.valueOf(longitude));
            lightRecord.setSensor_address(address);
            lightRecord.setOverallEndTime(df.format(Calendar.getInstance().getTime()));
            lightRecord.ID="LIGHT";

            p.records.add(lightRecord);
        }

        if(temperature_cb.isChecked()){
            temperatureRecord = new Record();
            temperatureRecord.setOverallStartTime(df.format(Calendar.getInstance().getTime()));
            temperatureRecord.setSensor_reading_1(temperature_view.getText().toString().matches("")?"null":temperature_view.getText().toString());
            temperatureRecord.setSensor_name(temperature_name);
            temperatureRecord.setSensor_resolution(temperature_accuracy);
            temperatureRecord.setSensor_vendor(temperature_vendor);
            temperatureRecord.setSensor_type(temperature_type);
            temperatureRecord.setSensor_latitude(String.valueOf(latitude));
            temperatureRecord.setSensor_longitude(String.valueOf(longitude));
            temperatureRecord.setSensor_address(address);
            temperatureRecord.setOverallEndTime(df.format(Calendar.getInstance().getTime()));
            temperatureRecord.ID = "TEMP";

            p.records.add(temperatureRecord);
        }

        return p;
    }



}
