package com.example.myiotdevice;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBDocument;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;


@DynamoDBTable(tableName = "IoT_Data_Storage")
public class PublicationDO {
    private String _creator;
    private String _publication_time;
    private Payload _payload;

    @DynamoDBHashKey(attributeName = "Creator")
    @DynamoDBAttribute(attributeName = "Creator")
    public String get_creator() {
        return _creator;
    }

    public void set_creator(final String _creator) {
        this._creator = _creator;
    }

    @DynamoDBRangeKey (attributeName = "PublicationTime")
    @DynamoDBAttribute(attributeName = "PublicationTime")
    public String get_publication_time() {
        return _publication_time;
    }

    public void set_publication_time(final String _publication_time) {
        this._publication_time= _publication_time;
    }

    @DynamoDBAttribute(attributeName = "payload")
    public Payload getPayload(){return _payload;}
    public void setPayload(Payload payload){this._payload=payload;}

    @DynamoDBDocument
    public static class Payload{
        private String _publication_latitude;
        private String _publication_longitude;
        private String _publication_location;
        private boolean _is_car_accident;
        private boolean _is_land_slide;
        private boolean _is_snow;
        private boolean _is_traffic_jam;

        @DynamoDBAttribute(attributeName = "publication_latitude")
        public String getPublication_latitude(){return _publication_latitude;}
        public void setPublication_latitude(String publication_latitude){this._publication_latitude=publication_latitude;}

        @DynamoDBAttribute(attributeName = "publication_location")
        public String getPublication_location(){return _publication_location;}
        public void setPublication_location(String publication_location){this._publication_location=publication_location;}

        @DynamoDBAttribute(attributeName = "publication_longitude")
        public String getPublication_longitude(){return _publication_longitude;}
        public void setPublication_longitude(String publication_longitude){this._publication_longitude=publication_longitude;}

        @DynamoDBAttribute(attributeName = "isCarAccident")
        public boolean get_is_car_accident(){return _is_car_accident;}
        public void set_is_car_accident(boolean car_accident){this._is_car_accident=car_accident;}

        @DynamoDBAttribute(attributeName = "isLandSlide")
        public boolean get_is_land_slide(){return _is_land_slide;}
        public void set_is_land_slide(boolean land_slide){this._is_land_slide=land_slide;}

        @DynamoDBAttribute(attributeName = "isSnow")
        public boolean get_is_snow(){return _is_snow;}
        public void set_is_snow(boolean is_snow){this._is_snow=is_snow;}

        @DynamoDBAttribute(attributeName = "isTrafficJam")
        public boolean get_is_traffic_jam(){return _is_traffic_jam;}
        public void set_is_traffic_jam(boolean is_traffic_jam){this._is_traffic_jam=is_traffic_jam;}

    }
}
