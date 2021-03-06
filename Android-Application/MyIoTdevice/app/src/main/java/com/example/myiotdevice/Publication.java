package com.example.myiotdevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Publication implements Serializable {

    private String publication_location;
    private String publication_latitude;
    private String publication_longitude;
    private String description;
    private double time_precision;
    private long reading_duration;
    private String language;
    private String creator;
    private String type;
    public ArrayList<Record> records;
    private boolean isCarAccident;
    private boolean isTrafficJam;
    private boolean isLandSlide;
    private boolean isSnow;
    private Date startDate;
    private Date endDate;
    private String country;
    private String nationalIdentifier;
    private String publicationTime;
    private String measurementOrCalculationTimePrecision;
    private String measurementOrCalculationPeriod;



    public Publication(){
        this.isCarAccident=false;
        this.isLandSlide=false;
        this.isSnow=false;
        this.isTrafficJam=false;
        this.description = "null";
        this.records = new ArrayList<Record>();
        this.type = "null";
        this.country="it";
        this.reading_duration=0;
        this.nationalIdentifier = "IT";
        this.publication_latitude = "null";
        this.publication_longitude = "null";
        this.publication_location = "null";
        this.time_precision = 0;
        this.language = "null";
        this.creator = "null";
        this.nationalIdentifier = "null";
        this.publicationTime = "null";
        this.measurementOrCalculationTimePrecision = "null";
        this.measurementOrCalculationPeriod = "null";

    }

    public String getMeasurementOrCalculationPeriod() {
        return measurementOrCalculationPeriod;
    }

    public void setMeasurementOrCalculationPeriod(String measurementOrCalculationPeriod) {
        this.measurementOrCalculationPeriod = measurementOrCalculationPeriod;
    }

    public String getMeasurementOrCalculationTimePrecision() {
        return measurementOrCalculationTimePrecision;
    }

    public void setMeasurementOrCalculationTimePrecision(String measurementOrCalculationTimePrecision) {
        this.measurementOrCalculationTimePrecision = measurementOrCalculationTimePrecision;
    }

    public String getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationalIdentifier() {
        return nationalIdentifier;
    }

    public void setNationalIdentifier(String nationalIdentifier) {
        this.nationalIdentifier = nationalIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void calculateDuration(){
        this.reading_duration = (startDate.getTime()-endDate.getTime());
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public String getPublication_latitude() {
        return publication_latitude;
    }

    public void setPublication_latitude(String publication_latitude) {
        this.publication_latitude = publication_latitude;
    }

    public String getPublication_longitude() {
        return publication_longitude;
    }

    public void setPublication_longitude(String publication_longitude) {
        this.publication_longitude = publication_longitude;
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

    public void setReading_duration(long reading_duration) {
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

    public String getType () {
        if (isCarAccident==true){
            type = type + "Car Accident";
        }
        if (isTrafficJam ==true){
            type = type + ", Traffic Jam";
        }
        if(isSnow==true){
            type = type + ", Climate Conditions";
        }
        if(isLandSlide==true){
            type = type + ", Obstruction";
        }
        return type;
    }
}
