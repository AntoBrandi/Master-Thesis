package com.example.myiotdevice;

import java.io.Serializable;

public class Response_Payload implements Serializable {
    private String _publication_latitude;
    private String _publication_longitude;
    private String _publication_location;
    private boolean _is_car_accident;
    private boolean _is_land_slide;
    private boolean _is_snow;
    private boolean _is_traffic_jam;

    public String get_publication_latitude() {
        return _publication_latitude;
    }

    public void set_publication_latitude(String _publication_latitude) {
        this._publication_latitude = _publication_latitude;
    }

    public String get_publication_longitude() {
        return _publication_longitude;
    }

    public void set_publication_longitude(String _publication_longitude) {
        this._publication_longitude = _publication_longitude;
    }

    public String get_publication_location() {
        return _publication_location;
    }

    public void set_publication_location(String _publication_location) {
        this._publication_location = _publication_location;
    }

    public boolean is_is_car_accident() {
        return _is_car_accident;
    }

    public void set_is_car_accident(boolean _is_car_accident) {
        this._is_car_accident = _is_car_accident;
    }

    public boolean is_is_land_slide() {
        return _is_land_slide;
    }

    public void set_is_land_slide(boolean _is_land_slide) {
        this._is_land_slide = _is_land_slide;
    }

    public boolean is_is_snow() {
        return _is_snow;
    }

    public void set_is_snow(boolean _is_snow) {
        this._is_snow = _is_snow;
    }

    public boolean is_is_traffic_jam() {
        return _is_traffic_jam;
    }

    public void set_is_traffic_jam(boolean _is_traffic_jam) {
        this._is_traffic_jam = _is_traffic_jam;
    }
}