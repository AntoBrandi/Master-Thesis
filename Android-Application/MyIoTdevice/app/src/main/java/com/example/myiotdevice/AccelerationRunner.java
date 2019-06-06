/*
ACCELERATION RUNNER
Class that will implement the Runnable interface in order to execute a task that is demanding resources
The task will be executed every x seconds, executed by the sleep command.
The task is to retrieve accelerometer data and show off this data to the user
 */

package com.example.myiotdevice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

public class AccelerationRunner implements Runnable {

    public MainActivity mainActivity;
    private static final int ACCELERATION_INDEX =1;


    public float accelerationX;
    public float accelerationY;
    public float accelerationZ;

    public SensorManager senSensorManager;
    public Sensor senAccelerometer;

    public AccelerationRunner(MainActivity _activity,SensorManager sm,Sensor acc){
        this.mainActivity=_activity;
        this.senSensorManager = sm;
        this.senAccelerometer = acc;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                updateAcceleration();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
            }
        }
    }

    public void updateAcceleration(){
        this.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    Handler handler = new Handler();
                    MySensorListener msl = new MySensorListener();
                    senSensorManager.registerListener(msl,senAccelerometer,SensorManager.SENSOR_DELAY_UI,handler);

                    TextView x_prova = (TextView) mainActivity.findViewById(R.id.x_prova);
                    TextView y_prova = (TextView) mainActivity.findViewById(R.id.y_prova);
                    TextView z_prova = (TextView) mainActivity.findViewById(R.id.z_prova);
                    x_prova.setText(String.valueOf(accelerationX));
                    y_prova.setText(String.valueOf(accelerationY));
                    z_prova.setText(String.valueOf(accelerationZ));

                    Looper.loop();
                }catch (Exception e) {}
            }
        });
    }

    private class MySensorListener implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            Sensor mySensor = event.sensor;

            if (mySensor.getType()==Sensor.TYPE_ACCELEROMETER){
                accelerationX = event.values[0];
                accelerationY = event.values[1];
                accelerationZ = event.values[2];
            }
        }
    }
}
