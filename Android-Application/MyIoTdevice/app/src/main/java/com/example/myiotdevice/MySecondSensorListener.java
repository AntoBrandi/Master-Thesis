package com.example.myiotdevice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class MySecondSensorListener implements SensorEventListener {
    public SecondActivity secondActivity;

    public MySecondSensorListener(SecondActivity activity){
        this.secondActivity = activity;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy){}
    public void onSensorChanged(final SensorEvent event){
        this.secondActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Sensor mySensor = event.sensor;
                if (mySensor.getType()==Sensor.TYPE_ROTATION_VECTOR){
                    secondActivity.orientation_coordinatePitch.setText(String.format("%.2f", event.values[0]));
                    secondActivity.orientation_coordinateRoll.setText(String.format("%.2f", event.values[1]));
                    secondActivity.orientation_coordinateAzimuth.setText(String.format("%.2f", event.values[2]));
                }
                if (mySensor.getType()==Sensor.TYPE_GYROSCOPE){
                    secondActivity.gyroscope_coordinateX.setText(String.format("%.2f", event.values[0])+" rad/s");
                    secondActivity.gyroscope_coordinateY.setText(String.format("%.2f", event.values[1])+" rad/s");
                    secondActivity.gyroscope_coordinateZ.setText(String.format("%.2f", event.values[2])+" rad/s");
                }

                if(mySensor.getType()==Sensor.TYPE_AMBIENT_TEMPERATURE){
                    secondActivity.temperature_view.setText(String.format("%.2f", event.values[0])+" °C");
                }
                if(mySensor.getType()==Sensor.TYPE_LIGHT){
                    secondActivity.light_view.setText(String.format("%.2f", event.values[0])+" lx");
                }
            }
        });
    }
}
