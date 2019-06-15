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
import android.widget.EditText;
import android.widget.Toast;
import org.xmlpull.v1.XmlSerializer;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SendEventActivity extends AppCompatActivity {
    public int car_accident_click;
    public int traffic_jam_click;
    public int landslide_click;
    public int snow_click;
    public Publication p;
    public String XML_Document;
    private TelephonyManager telephonyManager;
    private String deviceID;
    private EditText description;
    private Button car_accident;
    private Button traffic_jam;
    private Button landslide;
    private Button snow;
    private FloatingActionButton fab;
    private Date actualDate;
    private SimpleDateFormat df;
    private String publicationTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_event_activity);
        // reset counter click
        car_accident_click=0;
        traffic_jam_click=0;
        landslide_click=0;
        snow_click=0;
        description = (EditText) findViewById(R.id.description);
        // Button Click manage
        car_accident= (Button) findViewById(R.id.car_accident);
        traffic_jam = (Button) findViewById(R.id.traffic_jam);
        landslide = (Button) findViewById(R.id.landslide);
        snow = (Button) findViewById(R.id.snow);

        actualDate = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        publicationTime = df.format(actualDate);

        // get ID for a smartphone
        if (ActivityCompat.checkSelfPermission(SendEventActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(SendEventActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
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
        fab = findViewById(R.id.send_fab);
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
                if (description.getText().toString() != null)
                {
                    p.setDescription(description.getText().toString());
                }else{
                    p.setDescription("No Description");
                }
                if(deviceID != null) {
                    p.setCreator("it-" + deviceID);
                }
                else{
                    p.setCreator("it-" +"-"+ Build.MANUFACTURER+"-"+Build.MODEL+"-"+Build.USER);
                }


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
            // OPEN d2LogicalModel
            xmlSerializer.startTag("","d2LogicalModel");
            xmlSerializer.attribute("","modelBaseVersion","2");
            xmlSerializer.attribute("","xmls","http://datex2.eu/schema/2/2_0");
            xmlSerializer.attribute("","xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
            xmlSerializer.attribute("","xsi:schemaLocation","http://datex2.eu/schema/2/2_0 http://www.datex2.eu/schema/2/2_3/DATEXIISchema_2_2_3.xsd");

            // OPEN Exchange
            xmlSerializer.startTag("","exchange");
            xmlSerializer.startTag("","supplierIdentification");
            xmlSerializer.startTag("","country");
            xmlSerializer.text("it");
            xmlSerializer.endTag("","country");
            xmlSerializer.startTag("","nationalIdentifier");
            xmlSerializer.text("IT");
            xmlSerializer.endTag("","nationalIdentifier");
            xmlSerializer.endTag("","supplierIdentification");
            xmlSerializer.endTag("","exchange");

            // OPEN payloadPublication
            xmlSerializer.startTag("","payloadPublication");
            xmlSerializer.attribute("","xsi:type","MeasuredDataPublication");
            xmlSerializer.attribute("","lang","it");
            xmlSerializer.startTag("","publicationTime");
            xmlSerializer.text(publicationTime);
            xmlSerializer.endTag("","publicationTime");
            xmlSerializer.startTag("","feedDescription");
            xmlSerializer.text(p.getDescription());
            xmlSerializer.endTag("","feedDescription");
            xmlSerializer.startTag("","feedType");
            xmlSerializer.text(p.getType());
            xmlSerializer.endTag("","feedType");
            xmlSerializer.startTag("","defaultLanguage");
            xmlSerializer.text(p.getLanguage());
            xmlSerializer.endTag("","defaultLanguage");
            xmlSerializer.startTag("","publicationCreator");
            xmlSerializer.startTag("","country");
            xmlSerializer.text(p.getLanguage());
            xmlSerializer.endTag("","country");
            xmlSerializer.startTag("","nationalID");
            xmlSerializer.text(p.getLanguage());
            xmlSerializer.endTag("","nationalID");
            xmlSerializer.endTag("","publicationCreator");
            xmlSerializer.endTag("","payloadPublication");


            // OPEN measured Value
            xmlSerializer.startTag("","measuredValue");
            for (int i = 0;i<p.records.size();i++){
                xmlSerializer.startTag("","measurementEquipmentTypeUsed");
                xmlSerializer.text(p.records.get(i).getSensor_name());
                xmlSerializer.endTag("","measurementEquipmentTypeUsed");
            }
            xmlSerializer.startTag("","siteMeasurement");
            xmlSerializer.startTag("","measurementSiteReference");
            xmlSerializer.text(p.getCreator());
            xmlSerializer.endTag("","measurementSiteReference");
            xmlSerializer.startTag("","measurementTimeDefault");
            xmlSerializer.text(publicationTime);
            xmlSerializer.endTag("","measurementTimeDefault");
            xmlSerializer.endTag("","siteMeasurement");
            xmlSerializer.startTag("","basicData");
            xmlSerializer.startTag("","measurementOrCalculationTimePrecision");
            xmlSerializer.text("1us");
            xmlSerializer.endTag("","measurementOrCalculationTimePrecision");
            xmlSerializer.startTag("","measurementOrCalculationPeriod");
            xmlSerializer.text(String.valueOf(p.getEndDate().getTime()-p.getStartDate().getTime()));
            xmlSerializer.endTag("","measurementOrCalculationPeriod");
            xmlSerializer.endTag("","basicData");
            xmlSerializer.endTag("","measuredValue");



            // OPEN measurementSiteRecord
            for (int i =0;i<p.records.size();i++) {
                xmlSerializer.startTag("", "measurementSiteRecord");
                xmlSerializer.startTag("","computationalMethod");
                xmlSerializer.text("low pass filter");
                xmlSerializer.endTag("","computationalMethod");
                xmlSerializer.startTag("","measurementEquipmentReference");
                xmlSerializer.text(p.records.get(i).getSensor_vendor());
                xmlSerializer.endTag("","measurementEquipmentReference");
                xmlSerializer.startTag("","measurementSpecificCharacteristics");
                xmlSerializer.startTag("","accuracy");
                xmlSerializer.text(p.records.get(i).getSensor_resolution());
                xmlSerializer.endTag("","accuracy");
                xmlSerializer.startTag("","period");
                xmlSerializer.text(p.records.get(i).getValidity_period());
                xmlSerializer.endTag("","period");
                xmlSerializer.endTag("","measurementSpecificCharacteristics");
                xmlSerializer.startTag("","groupOfLocations");
                xmlSerializer.startTag("","tpegPointLocation");
                xmlSerializer.attribute("","xsi:type","ns1:TPEGSimplePoint");
                xmlSerializer.startTag("","point");
                xmlSerializer.attribute("","xsi:type","ns1:TPEGNonJunctionPoint");
                xmlSerializer.startTag("","pointCoordinates");
                xmlSerializer.startTag("","latitude");
                xmlSerializer.text(p.records.get(i).getSensor_latitude());
                xmlSerializer.endTag("","latitude");
                xmlSerializer.startTag("","longitude");
                xmlSerializer.text(p.records.get(i).getSensor_longitude());
                xmlSerializer.endTag("","longitude");
                xmlSerializer.endTag("","pointCoordinates");
                xmlSerializer.startTag("","name");
                xmlSerializer.startTag("","descriptor");
                xmlSerializer.startTag("","value");
                xmlSerializer.text(p.records.get(i).getSensor_address());
                xmlSerializer.endTag("","value");
                xmlSerializer.endTag("","descriptor");
                xmlSerializer.endTag("","name");
                xmlSerializer.endTag("","point");
                xmlSerializer.endTag("","tpegPointLocation");
                xmlSerializer.endTag("","groupOfLocations");
                xmlSerializer.startTag("","weatherData");

                if(p.records.get(i).ID == "TEMP") {
                    xmlSerializer.startTag("", "temperature");
                    xmlSerializer.startTag("", "airTemperature");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_1());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "airTemperature");
                    xmlSerializer.endTag("", "temperature");
                }
                else if(p.records.get(i).ID == "LIGHT"){
                    xmlSerializer.startTag("", "light");
                    xmlSerializer.startTag("", "externalLight");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_1());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "externalLight");
                    xmlSerializer.endTag("", "light");
                }
                else if(p.records.get(i).ID == "PRESS"){
                    xmlSerializer.startTag("", "pressure");
                    xmlSerializer.startTag("", "atmosphericPressure");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_1());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "atmosphericPressure");
                    xmlSerializer.endTag("", "pressure");
                }
                else if (p.records.get(i).ID=="ALT"){
                    xmlSerializer.startTag("", "altimeter");
                    xmlSerializer.startTag("", "altitude");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_1());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "altitude");
                    xmlSerializer.endTag("", "altimeter");
                }
                else if(p.records.get(i).ID=="ORIEN"){
                    xmlSerializer.startTag("", "orientation");
                    xmlSerializer.startTag("", "pitch");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_1());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "pitch");
                    xmlSerializer.startTag("", "roll");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_2());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "roll");
                    xmlSerializer.startTag("", "yaw");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_3());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "yaw");
                    xmlSerializer.endTag("", "orientation");
                }
                else if(p.records.get(i).ID=="ACC"){
                    xmlSerializer.startTag("", "acceleration");
                    xmlSerializer.startTag("", "x");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_1());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "x");
                    xmlSerializer.startTag("", "y");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_2());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "y");
                    xmlSerializer.startTag("", "z");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_3());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "z");
                    xmlSerializer.endTag("", "acceleration");
                }
                else if(p.records.get(i).ID=="GYRO"){
                    xmlSerializer.startTag("", "gyroscope");
                    xmlSerializer.startTag("", "x");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_1());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "x");
                    xmlSerializer.startTag("", "y");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_2());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "y");
                    xmlSerializer.startTag("", "z");
                    xmlSerializer.startTag("","value");
                    xmlSerializer.text(p.records.get(i).getSensor_reading_3());
                    xmlSerializer.endTag("","value");
                    xmlSerializer.endTag("", "z");
                    xmlSerializer.endTag("", "gyroscope");
                }

                xmlSerializer.text("");
                xmlSerializer.endTag("","weatherData");
                xmlSerializer.startTag("","trafficElement");
                xmlSerializer.startTag("","abnormalTraffic");
                xmlSerializer.startTag("","abnormalTrafficType");
                xmlSerializer.text(p.getType());
                xmlSerializer.endTag("","abnormalTrafficType");
                xmlSerializer.endTag("","abnormalTraffic");
                xmlSerializer.endTag("","trafficElement");
                xmlSerializer.endTag("", "measurementSiteRecord");
            }




            // CLOSE d2LogicalModel
            xmlSerializer.endTag("","d2LogicalModel");
            xmlSerializer.endDocument();


            XML_Document = writer.toString();
        }
        catch (Exception e){}
    }
}
