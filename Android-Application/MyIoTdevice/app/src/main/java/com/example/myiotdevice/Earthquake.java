package com.example.myiotdevice;

public class Earthquake {
    private String place;
    private double mag;
    private long time;
    private String url;

    public Earthquake(double mag,String place,long time,String url)
    {
        this.place=place;
        this.mag=mag;
        this.time=time;
        this.url=url;
    }

    public String getPlace() {
        return place;
    }

    public double getMag() {
        return mag;
    }

    public long getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
