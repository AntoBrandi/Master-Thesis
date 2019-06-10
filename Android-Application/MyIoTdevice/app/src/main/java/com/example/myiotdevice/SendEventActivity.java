package com.example.myiotdevice;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Calendar;

public class SendEventActivity extends AppCompatActivity {
    public int car_accident_click;
    public int traffic_jam_click;
    public int landslide_click;
    public int snow_click;
    public Publication p;
    public String XML_Document;
    private TelephonyManager telephonyManager;
    private String deviceID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_event_activity);
        // reset counter click
        car_accident_click=0;
        traffic_jam_click=0;
        landslide_click=0;
        snow_click=0;

        // get ID for a smartphone
        if (ActivityCompat.checkSelfPermission(SendEventActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(SendEventActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        try {
            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            deviceID = telephonyManager.getDeviceId();
        }
        catch (Exception e){}

        // Retrieve the Record object that has been sent over the intent
        Intent i = getIntent();
        p = (Publication) i.getSerializableExtra("Publication");


        // Button Click manage
        final Button car_accident = (Button) findViewById(R.id.car_accident);
        final Button traffic_jam = (Button) findViewById(R.id.traffic_jam);
        final Button landslide = (Button) findViewById(R.id.landslide);
        final Button snow = (Button) findViewById(R.id.snow);

        car_accident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_accident_click++;
                if ((car_accident_click%2)-1==0) {
                    car_accident.setBackgroundColor(getResources().getColor(R.color.colorSecondaryDark));
                }
                else
                {
                    car_accident.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
                }
            }
        });

        traffic_jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traffic_jam_click++;
                if((traffic_jam_click%2)-1==0) {
                    traffic_jam.setBackgroundColor(getResources().getColor(R.color.colorSecondaryDark));
                }
                else{
                    traffic_jam.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
                }
            }
        });

        landslide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landslide_click++;
                if((landslide_click%2)-1==0) {
                    landslide.setBackgroundColor(getResources().getColor(R.color.colorSecondaryDark));
                }
                else{
                    landslide.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
                }
            }
        });

        snow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snow_click++;
                if((snow_click%2)-1==0) {
                    snow.setBackgroundColor(getResources().getColor(R.color.colorSecondaryDark));
                }
                else{
                    snow.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
                }
            }
        });


        // SEND DATA
        FloatingActionButton fab = findViewById(R.id.send_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((car_accident_click%2)-1==0) {
                    p.setCarAccident(true);
                }
                if((traffic_jam_click%2)-1==0) {
                    p.setTrafficJam(true);
                }
                if((landslide_click%2)-1==0) {
                    p.setLandSlide(true);

                }
                if((snow_click%2)-1==0) {
                    p.setSnow(true);
                }

                p.setEndDate(Calendar.getInstance().getTime());
                p.calculateDuration();
                p.setLanguage("it");
                if(deviceID != null) {
                    p.setCreator("it-" + deviceID);
                }
                else{
                    p.setCreator("it-" +"-"+ Build.MANUFACTURER+"-"+Build.MODEL+"-"+Build.USER);
                }

                // TODO: improve the XML data format with the DATEX II standard
                generateXML();

                Intent k = new Intent(SendEventActivity.this,XMLPrinter.class);
                k.putExtra("XML",XML_Document);
                startActivity(k);
            }
        });
    }



    private void generateXML() {
        try {
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            xmlSerializer.setOutput(writer);

            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            // Open tag
            xmlSerializer.startTag("", "record");
            xmlSerializer.startTag("", "sensor");
            xmlSerializer.attribute("", "SENSOR_NAME", "GPS");
            xmlSerializer.startTag("", "value");
            xmlSerializer.startTag("", "latitude");
            xmlSerializer.text(String.valueOf(p.getPublication_latitude()));
            xmlSerializer.endTag("", "latitude");
            xmlSerializer.startTag("", "longitude");
            xmlSerializer.text(String.valueOf(p.getPublication_longitude()));
            xmlSerializer.endTag("", "longitude");
            xmlSerializer.startTag("", "address");
            xmlSerializer.text(String.valueOf(p.getPublication_location()));
            xmlSerializer.endTag("", "address");
            xmlSerializer.endTag("", "value");
            xmlSerializer.endTag("", "sensor");
            xmlSerializer.endTag("", "record");
            xmlSerializer.endDocument();

            XML_Document = writer.toString();
        }
        catch (Exception e){}
    }
}
