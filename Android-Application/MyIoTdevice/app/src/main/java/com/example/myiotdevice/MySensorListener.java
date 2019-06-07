package com.example.myiotdevice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class MySensorListener implements SensorEventListener {
    public MainActivity mainActivity;
    public float accelerationX;
    public float accelerationY;
    public float accelerationZ;

    public MySensorListener(MainActivity activity){
        this.mainActivity = activity;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy){}
    public void onSensorChanged(final SensorEvent event){
        this.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Sensor mySensor = event.sensor;
                if (mySensor.getType()==Sensor.TYPE_ACCELEROMETER){
                    accelerationX = event.values[0];
                    accelerationY = event.values[1];
                    accelerationZ = event.values[2];
                    TextView x_prova = (TextView) mainActivity.findViewById(R.id.x_prova);
                    TextView y_prova = (TextView) mainActivity.findViewById(R.id.y_prova);
                    TextView z_prova = (TextView) mainActivity.findViewById(R.id.z_prova);
                    x_prova.setText(String.valueOf(accelerationX));
                    y_prova.setText(String.valueOf(accelerationY));
                    z_prova.setText(String.valueOf(accelerationZ));
                }
            }
        });

    }
}
