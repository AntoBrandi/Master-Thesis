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

        @DynamoDBAttribute(attributeName = "publication_latitude")
        public String getPublication_latitude(){return _publication_latitude;}
        public void setPublication_latitude(String publication_latitude)
        {this._publication_latitude=publication_latitude;}

        @DynamoDBAttribute(attributeName = "publication_location")
        public String getPublication_location(){return _publication_location;}
        public void setPublication_location(String publication_location)
        {this._publication_location=publication_location;}

        @DynamoDBAttribute(attributeName = "publication_longitude")
        public String getPublication_longitude(){return _publication_longitude;}
        public void setPublication_longitude(String publication_longitude)
        {this._publication_longitude=publication_longitude;}
    }
}