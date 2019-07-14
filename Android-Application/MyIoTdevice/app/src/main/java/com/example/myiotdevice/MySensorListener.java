package com.example.myiotdevice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class MySensorListener implements SensorEventListener {
    public FirstSensorActivity mainActivity;
    public float pressure;
    public float altitude;
    static final float ALPHA = 0.4f; // manual settings
    public float[] output;

    public MySensorListener(FirstSensorActivity activity){
        this.mainActivity = activity;
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy){}
    public void onSensorChanged(final SensorEvent event){
        this.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Sensor mySensor = event.sensor;
                if (mySensor.getType()==Sensor.TYPE_ACCELEROMETER){


                    output = lowPassFilter(event.values.clone(),output);

                    // FILTERED
                    mainActivity.acceleration_coordinateX.setText(String.format("%.2f", output[0])+" m/s^2");
                    mainActivity.acceleration_coordinateY.setText(String.format("%.2f", output[1])+" m/s^2");
                    mainActivity.acceleration_coordinateZ.setText(String.format("%.2f", output[2])+" m/s^2");

                    // NOT FILTERED
//                    mainActivity.acceleration_coordinateX.setText(String.format("%.2f", event.values[0])+" m/s^2");
//                    mainActivity.acceleration_coordinateY.setText(String.format("%.2f", event.values[1])+" m/s^2");
//                    mainActivity.acceleration_coordinateZ.setText(String.format("%.2f", event.values[2])+" m/s^2");

                    // Sensor information and name
                    mainActivity.accelerometer_name= mySensor.getName();
                    mainActivity.accelerometer_resolution = String.valueOf(mySensor.getResolution());
                    mainActivity.accelerometer_vendor = mySensor.getVendor();
                    mainActivity.accelerometer_type = String.valueOf(mySensor.getType());
                }

                if(mySensor.getType()==Sensor.TYPE_PRESSURE){
                        pressure = event.values[0];
                        altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE,pressure);
                        mainActivity.pressure_view.setText(String.format("%.2f", pressure)+" hPa");
                        mainActivity.altitude_view.setText(String.format("%.2f", altitude)+" m");
                        // Sensor information and name
                    mainActivity.pressure_name = mySensor.getName();
                    mainActivity.pressure_accuracy = String.valueOf(mySensor.getResolution());
                    mainActivity.pressure_vendor = mySensor.getVendor();
                    mainActivity.pressure_type = String.valueOf(mySensor.getType());
                }
            }
        });
    }

    public float[] lowPassFilter(float[] input,float[] output){
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
}
