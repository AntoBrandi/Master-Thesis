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

public class FirstSensorActivity extends AppCompatActivity {

    public Sensor senAccelerometer,senPressure;
    public SensorManager senSensorManager;
    public Thread timeThread;
    public HandlerThread mAccelerometerThread,mPressureThread;
    public Handler mAccelerometerHandler,mPressureHandler;
    public MySensorListener msl;
    public String latitude;
    public String longitude;
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
    private ImageButton continue_btn;
    public Record accelerometerRecord;
    public Record pressureRecord;
    public Record altimeterRecord;
    public SimpleDateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firts_sensor_activity);

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

        df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        Intent i = getIntent();
        address = (String) i.getStringExtra("address");
        longitude = (String) i.getStringExtra("longitude");
        latitude = (String) i.getStringExtra("latitude");

        position_address.setText(address);
        tvAddress.setText(address);
        position_latitude.setText(latitude);
        position_longitude.setText(longitude);

        // SEND DATA
        continue_btn =findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Publication p = createPublication();
                Intent i = new Intent(FirstSensorActivity.this,SecondSensorActivity.class);
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
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(msl, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL, mAccelerometerHandler);
        senSensorManager.registerListener(msl, senPressure, SensorManager.SENSOR_DELAY_NORMAL, mPressureHandler);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mAccelerometerThread.quitSafely();
        mPressureThread.quitSafely();
    }


    public Publication createPublication(){
        Publication publication = new Publication();

        if(acceleration_cb.isChecked()){
            accelerometerRecord=new Record();

            accelerometerRecord.setOverallStartTime(df.format(Calendar.getInstance().getTime()));
            accelerometerRecord.setSensor_reading_1(acceleration_coordinateX.getText().toString().matches("")?"null":acceleration_coordinateX.getText().toString());
            accelerometerRecord.setSensor_reading_2(acceleration_coordinateY.getText().toString().matches("")?"null":acceleration_coordinateY.getText().toString());
            accelerometerRecord.setSensor_reading_3(acceleration_coordinateZ.getText().toString().matches("")?"null":acceleration_coordinateZ.getText().toString());
            accelerometerRecord.setSensor_name(accelerometer_name);
            accelerometerRecord.setSensor_resolution(accelerometer_resolution);
            accelerometerRecord.setSensor_vendor(accelerometer_vendor);
            accelerometerRecord.setSensor_type(accelerometer_type);
            accelerometerRecord.setSensor_latitude(String.valueOf(latitude));
            accelerometerRecord.setSensor_longitude(String.valueOf(longitude));
            accelerometerRecord.setSensor_address(address);
            accelerometerRecord.setOverallEndTime(df.format(Calendar.getInstance().getTime()));

            accelerometerRecord.ID = "ACC";

            publication.records.add(accelerometerRecord);
        }

        if(pressure_cb.isChecked()){
            pressureRecord = new Record();

            pressureRecord.setOverallStartTime(df.format(Calendar.getInstance().getTime()));
            pressureRecord.setSensor_reading_1(pressure_view.getText().toString().matches("")?"null":pressure_view.getText().toString());
            pressureRecord.setSensor_name(pressure_name);
            pressureRecord.setSensor_resolution(pressure_accuracy);
            pressureRecord.setSensor_vendor(pressure_vendor);
            pressureRecord.setSensor_type(pressure_type);
            pressureRecord.setSensor_latitude(String.valueOf(latitude));
            pressureRecord.setSensor_longitude(String.valueOf(longitude));
            pressureRecord.setSensor_address(address);
            pressureRecord.setOverallEndTime(df.format(Calendar.getInstance().getTime()));

            pressureRecord.ID = "PRESS";

            publication.records.add(pressureRecord);
        }

        if(altitude_cb.isChecked()){
            altimeterRecord = new Record();
            altimeterRecord.setOverallStartTime(df.format(Calendar.getInstance().getTime()));
            altimeterRecord.setSensor_reading_1(altitude_view.getText().toString().matches("")?"null":altitude_view.getText().toString());
            altimeterRecord.setSensor_name(pressure_name);
            altimeterRecord.setSensor_resolution(pressure_accuracy);
            altimeterRecord.setSensor_vendor(pressure_vendor);
            altimeterRecord.setSensor_type(pressure_type);
            altimeterRecord.setSensor_latitude(String.valueOf(latitude));
            altimeterRecord.setSensor_longitude(String.valueOf(longitude));
            altimeterRecord.setSensor_address(address);
            altimeterRecord.setOverallEndTime(df.format(Calendar.getInstance().getTime()));

            altimeterRecord.ID = "ALT";

            publication.records.add(altimeterRecord);
        }


        publication.setPublication_latitude(String.valueOf(latitude));
        publication.setPublication_longitude(String.valueOf(longitude));
        publication.setPublication_location(String.valueOf(address));
        publication.setStartDate(Calendar.getInstance().getTime());

        return publication;
    }

}

