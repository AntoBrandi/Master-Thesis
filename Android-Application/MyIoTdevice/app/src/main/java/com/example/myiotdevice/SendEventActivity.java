package com.example.myiotdevice;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SendEventActivity extends AppCompatActivity {
    public int car_accident_click;
    public int traffic_jam_click;
    public int landslide_click;
    public int snow_click;
    public Record r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_event_activity);
        // reset counter click
        car_accident_click=0;
        traffic_jam_click=0;
        landslide_click=0;
        snow_click=0;

        // Retrieve the Record object that has been sent over the intent
        Intent i = getIntent();
        r = (Record) i.getSerializableExtra("Record");


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
                    r.isCarAccident= true;
                }
                if((traffic_jam_click%2)-1==0) {
                    r.isTrafficJam=true;
                }
                if((landslide_click%2)-1==0) {
                    r.isLandSlide=true;
                }
                if((snow_click%2)-1==0) {
                    r.isSnow=true;
                }

                Toast.makeText(getApplicationContext(),"Data completed, Sending the data...",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
