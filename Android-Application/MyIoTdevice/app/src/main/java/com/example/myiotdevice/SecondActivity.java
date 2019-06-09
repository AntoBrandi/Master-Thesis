package com.example.myiotdevice;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

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

    public FloatingActionButton fab;

    public SensorManager senSensorManager;
    public Thread timeThread;
    public MySecondSensorListener msl;

    public Record r;
    public String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
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
        r=(Record) i.getSerializableExtra("Record");
        address = (String) i.getStringExtra("Address");

        if(address != null){
            address_view.setText(address);
        }


        // SEND DATA
        fab =findViewById(R.id.continue_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r = createRecord();
                Intent j = new Intent(SecondActivity.this,SendEventActivity.class);
                j.putExtra("Record",r);
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


    public Record createRecord(){

        if(orientation_cb.isChecked()){
            r.pitch = orientation_coordinatePitch.getText().toString();
            r.azimuth = orientation_coordinateAzimuth.getText().toString();
            r.roll=orientation_coordinateRoll.getText().toString();
            r.orientationName = orientation_name;
        }

        if(gyroscope_cb.isChecked()){
            r.gyroscopeX=gyroscope_coordinateX.getText().toString();
            r.gyroscopeY=gyroscope_coordinateY.getText().toString();
            r.gyroscopeZ=gyroscope_coordinateZ.getText().toString();
            r.gyroscopeName = gyroscope_name;
        }

        if(light_cb.isChecked()){
            r.light=light_view.getText().toString();
            r.lightName = light_name;
        }

        if(temperature_cb.isChecked()){
            r.temperature=temperature_view.getText().toString();
            r.temperatureName = temperature_name;
        }

        return r;
    }
}
