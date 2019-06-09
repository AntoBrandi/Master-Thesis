package com.example.myiotdevice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class MySensorListener implements SensorEventListener {
    public MainActivity mainActivity;
    public float pressure;
    public float altitude;

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
                    mainActivity.acceleration_coordinateX.setText(String.format("%.2f", event.values[0])+" m/s^2");
                    mainActivity.acceleration_coordinateY.setText(String.format("%.2f", event.values[1])+" m/s^2");
                    mainActivity.acceleration_coordinateZ.setText(String.format("%.2f", event.values[2])+" m/s^2");
                    // Sensor information and name
                    mainActivity.accelerometer_name= mySensor.getName();

                }

                if(mySensor.getType()==Sensor.TYPE_PRESSURE){
                        pressure = event.values[0];
                        altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE,pressure);
                        mainActivity.pressure_view.setText(String.format("%.2f", pressure)+" hPa");
                        mainActivity.altitude_view.setText(String.format("%.2f", altitude)+" m");
                        // Sensor information and name
                    mainActivity.pressure_name = mySensor.getName();
                }
            }
        });
    }
}
