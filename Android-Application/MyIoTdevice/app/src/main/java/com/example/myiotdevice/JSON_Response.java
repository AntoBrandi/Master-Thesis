package com.example.myiotdevice;

import java.io.Serializable;

public class JSON_Response implements Serializable {
    private String _creator;
    private String _publication_time;
    private Response_Payload _payload;

    public String get_creator() {
        return _creator;
    }

    public void set_creator(String _creator) {
        this._creator = _creator;
    }

    public String get_publication_time() {
        return _publication_time;
    }

    public void set_publication_time(String _publication_time) {
        this._publication_time = _publication_time;
    }

    public Response_Payload get_payload() {
        return _payload;
    }

    public void set_payload(Response_Payload _payload) {
        this._payload = _payload;
    }
}
