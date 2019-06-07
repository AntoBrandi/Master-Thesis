package com.example.myiotdevice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class MySensorListener implements SensorEventListener {
    public MainActivity mainActivity;
    public float accelerationX;
    public float accelerationY;
    public float accelerationZ;
    public float pressure;
    public float altitude;
    public float temperature;
    public float light;
    public float roll;
    public float pitch;
    public float azimuth;
    public float gyroscopeX;
    public float gyroscopeY;
    public float gyroscopeZ;
    private static final int ACCELERATION_INDEX =1;
    private static final int PRESSURE_ALTITUDE_INDEX =2;
    private static final int TEMPERATURE_LIGHT_INDEX =3;
    private static final int ORIENTATION_INDEX =4;
    private static final int GYROSCOPE_INDEX =5;


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
                    mainActivity.sensors.set(ACCELERATION_INDEX,new Sensors("Accelerazione","X","Y","Z",String.valueOf(accelerationX),String.valueOf(accelerationY),String.valueOf(accelerationZ),null,null,null,null));
                    mainActivity.sensorsAdapter.notifyDataSetChanged();
                }

                if(mySensor.getType()==Sensor.TYPE_PRESSURE){
                    pressure = event.values[0];
                    altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE,pressure);
                    mainActivity.sensors.set(PRESSURE_ALTITUDE_INDEX,new Sensors(null,null,null,null,null,null,null,"Pressione","Altitudine",String.valueOf(pressure),String.valueOf(altitude)));
                    mainActivity.sensorsAdapter.notifyDataSetChanged();
                }

                if ((mySensor.getType()==Sensor.TYPE_AMBIENT_TEMPERATURE) || (mySensor.getType()==Sensor.TYPE_LIGHT)){
                    if(mySensor.getType()==Sensor.TYPE_AMBIENT_TEMPERATURE) {
                        temperature = event.values[0];
                    }
                    else if (mySensor.getType()==Sensor.TYPE_LIGHT){
                        light = event.values[0];
                    }
                    mainActivity.sensors.set(TEMPERATURE_LIGHT_INDEX,new Sensors(null,null,null,null,null,null,null,"Temperatura","Luce",String.valueOf(temperature),String.valueOf(light)));
                    mainActivity.sensorsAdapter.notifyDataSetChanged();
                }

                if (mySensor.getType()==Sensor.TYPE_ROTATION_VECTOR){
                    azimuth = event.values[0];
                    pitch = event.values[1];
                    roll = event.values[2];
                    mainActivity.sensors.set(ORIENTATION_INDEX,new Sensors("Orientamento","Pitch","Roll","Azimuth",String.valueOf(pitch),String.valueOf(roll),String.valueOf(azimuth),null,null,null,null));
                    mainActivity.sensorsAdapter.notifyDataSetChanged();
                }

                if (mySensor.getType()==Sensor.TYPE_GYROSCOPE){
                    gyroscopeX = event.values[0];
                    gyroscopeY = event.values[1];
                    gyroscopeZ = event.values[2];
                    mainActivity.sensors.set(GYROSCOPE_INDEX,new Sensors("Giroscopio","X","Y","Z",String.valueOf(gyroscopeX),String.valueOf(gyroscopeY),String.valueOf(gyroscopeZ),null,null,null,null));
                    mainActivity.sensorsAdapter.notifyDataSetChanged();
                }


            }
        });

    }
}
