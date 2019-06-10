package com.example.myiotdevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Publication implements Serializable {

    private String publication_location;
    private double publication_latitude;
    private double publication_longitude;
    private String reading_period;
    private double time_precision;
    private double reading_duration;
    private String language;
    private String creator;
    public ArrayList<Record> records;
    private boolean isCarAccident;
    private boolean isTrafficJam;
    private boolean isLandSlide;
    private boolean isSnow;

    public Publication(){
        this.isCarAccident=false;
        this.isLandSlide=false;
        this.isSnow=false;
        this.isTrafficJam=false;
        records = new ArrayList<Record>();
    }

    public boolean isCarAccident() {
        return isCarAccident;
    }

    public void setCarAccident(boolean carAccident) {
        isCarAccident = carAccident;
    }

    public boolean isTrafficJam() {
        return isTrafficJam;
    }

    public void setTrafficJam(boolean trafficJam) {
        isTrafficJam = trafficJam;
    }

    public boolean isLandSlide() {
        return isLandSlide;
    }

    public void setLandSlide(boolean landSlide) {
        isLandSlide = landSlide;
    }

    public boolean isSnow() {
        return isSnow;
    }

    public void setSnow(boolean snow) {
        isSnow = snow;
    }

    public String getPublication_location() {
        return publication_location;
    }

    public void setPublication_location(String publication_location) {
        this.publication_location = publication_location;
    }

    public double getPublication_latitude() {
        return publication_latitude;
    }

    public void setPublication_latitude(double publication_latitude) {
        this.publication_latitude = publication_latitude;
    }

    public double getPublication_longitude() {
        return publication_longitude;
    }

    public void setPublication_longitude(double publication_longitude) {
        this.publication_longitude = publication_longitude;
    }

    public String getReading_period() {
        return reading_period;
    }

    public void setReading_period(String reading_period) {
        this.reading_period = reading_period;
    }

    public double getTime_precision() {
        return time_precision;
    }

    public void setTime_precision(double time_precision) {
        this.time_precision = time_precision;
    }

    public double getReading_duration() {
        return reading_duration;
    }

    public void setReading_duration(double reading_duration) {
        this.reading_duration = reading_duration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
