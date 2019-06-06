package com.example.myiotdevice;



import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public  ArrayList<Sensors> sensors;
    public SensorsAdapter sensorsAdapter;
    public SensorManager senSensorManager;
    public Sensor senAccelerometer;
    public Thread accelerationThread;
    public Thread timeThread;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ---- POPULATING THE LIST VIEW WITH SENSORS DATA ----
        sensors = new ArrayList<Sensors>();
        sensors=assignDefaultParameters(sensors);
        sensorsAdapter = new SensorsAdapter(this,sensors);

        ListView mainListView = (ListView) findViewById(R.id.MainListView);
        mainListView.setAdapter(sensorsAdapter);


        // ---- DATE AND TIME THREAD----
        Runnable myRunnableThread = new CountDownRunner(this);
        timeThread = new Thread(myRunnableThread);
        timeThread.start();


        // ---- SENSOR THREAD ----
        // Thread that will be in charge of read the status of sensors and update the view
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Runnable myAccelerationThread = new AccelerationRunner(this,senSensorManager,senAccelerometer);
        accelerationThread = new Thread(myAccelerationThread);
        accelerationThread.start();

    }

    @Override
    protected void onResume(){
        super.onResume();
        //senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();

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
