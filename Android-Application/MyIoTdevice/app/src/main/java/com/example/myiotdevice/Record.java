package com.example.myiotdevice;

import java.util.Calendar;

public class Record {
    public Calendar calendar;
    public double latitude;
    public double longitude;
    public String address;
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
    public boolean isCarAccident;
    public boolean isTrafficJam;
    public boolean isLandSlide;
    public boolean isSnow;

    public Record(Calendar calendar){
        this.calendar=calendar;
        this.isCarAccident=false;
        this.isTrafficJam=false;
        this.isLandSlide=false;
        this.isSnow=false;
    }

}
