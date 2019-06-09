package com.example.myiotdevice;

import java.io.Serializable;
import java.util.Calendar;

public class Record implements Serializable {
    public Calendar calendar;
    public double latitude;
    public double longitude;
    public String address;
    public String accelerationX;
    public String accelerationY;
    public String accelerationZ;
    public String accelerometerName;
    public String pressureName;
    public String temperatureName;
    public String lightName;
    public String orientationName;
    public String gyroscopeName;
    public String pressure;
    public String altitude;
    public String temperature;
    public String light;
    public String roll;
    public String pitch;
    public String azimuth;
    public String gyroscopeX;
    public String gyroscopeY;
    public String gyroscopeZ;
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
