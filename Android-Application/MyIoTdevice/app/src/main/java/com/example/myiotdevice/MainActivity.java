package com.example.myiotdevice;



import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Parameters shared by all the sensors
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        msl = new MySensorListener(this);

        // ---- POPULATING THE LIST VIEW WITH SENSORS DATA ----
        sensors = new ArrayList<Sensors>();
        sensors=assignDefaultParameters(sensors);
        sensorsAdapter = new SensorsAdapter(this,sensors);

        ListView mainListView = (ListView) findViewById(R.id.MainListView);
        mainListView.setAdapter(sensorsAdapter);




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
}
