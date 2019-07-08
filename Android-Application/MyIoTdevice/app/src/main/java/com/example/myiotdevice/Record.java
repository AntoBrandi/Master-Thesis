package com.example.myiotdevice;

import java.io.Serializable;
import java.util.Calendar;

public class Record implements Serializable {
    private Calendar calendar;
    private int recordId;
    private boolean safetyRelatedMessage;
    private String validity_status; // suspended or active
    private String overallStartTime;
    private String overallEndTime;
    private String validity_period;
    private String exception_period;
    private boolean isOverrunning;
    private String type; // Traffic Element , Operator Action , Non-road Event Information
    private String sensor_type;
    private String sensor_name;
    private String sensor_resolution;
    private String sensor_reading_1;
    private String sensor_reading_2;
    private String sensor_reading_3;
    private String sensor_vendor;
    private String sensor_latitude;
    private String sensor_longitude;
    private String sensor_address;
    public String ID;
    private String computationalMethod;



    public Record(){
        this.safetyRelatedMessage = false;
        this.validity_status = "suspended";
        this.isOverrunning = false;
        this.validity_period = "100";
        this.sensor_latitude = "No Latitude";
        this.sensor_longitude = "No Longitude";
        this.computationalMethod = "low pass filter";
    }

    public String getComputationalMethod() {
        return computationalMethod;
    }

    public void setComputationalMethod(String computationalMethod) {
        this.computationalMethod = computationalMethod;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public boolean isSafetyRelatedMessage() {
        return safetyRelatedMessage;
    }

    public void setSafetyRelatedMessage(boolean safetyRelatedMessage) {
        this.safetyRelatedMessage = safetyRelatedMessage;
    }

    public String getValidity_status() {
        return validity_status;
    }

    public void setValidity_status_active() {
        this.validity_status = "active";
    }

    public void setValidity_status_suspended() {
        this.validity_status = "suspended";
    }

    public String getOverallStartTime() {
        return overallStartTime;
    }

    public void setOverallStartTime(String overallStartTime) {
        this.overallStartTime = overallStartTime;
    }

    public String getOverallEndTime() {
        return overallEndTime;
    }

    public void setOverallEndTime(String overallEndTime) {
        this.overallEndTime = overallEndTime;
    }

    public String getValidity_period() {
        return validity_period;
    }

    public void setValidity_period(String validity_period) {

        this.validity_period = validity_period;
    }

    public String getException_period() {
        return exception_period;
    }

    public void setException_period(String exception_period) {
        this.exception_period = exception_period;
    }

    public boolean isOverrunning() {
        return isOverrunning;
    }

    public void setOverrunning(boolean overrunning) {
        isOverrunning = overrunning;
    }

    public String getType() {
        return type;
    }

    public void setType_Traffic_Element() {
        this.type = "Traffic Element";
    }
    public void setType_Operator_Action() {
        this.type = "Operator Action";
    }
    public void setType_Non_road_event_information() {
        this.type = "Non-road Event Information";
    }

    public String getSensor_type() {
        return sensor_type;
    }

    public void setSensor_type(String sensor_type) {
        this.sensor_type = sensor_type;
    }

    public String getSensor_name() {
        return sensor_name;
    }

    public void setSensor_name(String sensor_name) {
        this.sensor_name = sensor_name;
    }

    public String getSensor_resolution() {
        return sensor_resolution;
    }

    public String getSensor_reading_1() {
        return sensor_reading_1;
    }

    public void setSensor_reading_1(String sensor_reading_1) {
        this.sensor_reading_1 = sensor_reading_1;
    }

    public String getSensor_reading_2() {
        return sensor_reading_2;
    }

    public void setSensor_reading_2(String sensor_reading_2) {
        this.sensor_reading_2 = sensor_reading_2;
    }

    public String getSensor_reading_3() {
        return sensor_reading_3;
    }

    public void setSensor_reading_3(String sensor_reading_3) {
        this.sensor_reading_3 = sensor_reading_3;
    }

    public String getSensor_vendor() {
        return sensor_vendor;
    }

    public void setSensor_vendor(String sensor_vendor) {
        this.sensor_vendor = sensor_vendor;
    }

    public void setSensor_resolution(String sensor_resolution) {
        this.sensor_resolution = sensor_resolution;
    }

    public String getSensor_latitude() {
        return sensor_latitude;
    }

    public void setSensor_latitude(String sensor_latitude) {
        this.sensor_latitude = sensor_latitude;
    }

    public String getSensor_longitude() {
        return sensor_longitude;
    }

    public void setSensor_longitude(String sensor_longitude) {
        this.sensor_longitude = sensor_longitude;
    }

    public String getSensor_address() {
        return sensor_address;
    }

    public void setSensor_address(String sensor_address) {
        this.sensor_address = sensor_address;
    }
}
