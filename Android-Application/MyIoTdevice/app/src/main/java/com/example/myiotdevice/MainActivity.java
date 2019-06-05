package com.example.myiotdevice;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public  ArrayList<Sensors> sensors;
    public SensorsAdapter sensorsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ---- DATE AND TIME THREAD----
        Thread timeThread = null;
        Runnable myRunnableThread = new CountDownRunner(this);
        timeThread = new Thread(myRunnableThread);
        timeThread.start();


        // ---- POPULATING THE LIST VIEW WITH SENSORS DATA ----
        // Set the default views for the array List
        sensors = new ArrayList<Sensors>();
        sensors=assignDefaultParameters(sensors);
        sensorsAdapter = new SensorsAdapter(this,sensors);


        ListView mainListView = (ListView) findViewById(R.id.MainListView);
        mainListView.setAdapter(sensorsAdapter);


    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }


    public ArrayList<Sensors> assignDefaultParameters(ArrayList<Sensors> sensors){
        sensors.add(new Sensors("Posizione","Latitudine","Longitudine","Indirizzo","","","",null,null,null,null));
        sensors.add(new Sensors("Accelerazione","X","Y","Z","","","",null,null,null,null));
        sensors.add(new Sensors(null,null,null,null,null,null,null,"Pressione","Altitudine","",""));
        sensors.add(new Sensors(null,null,null,null,null,null,null,"Temperatura","Luce","",""));
        sensors.add(new Sensors("Orientamento","Pitch","Roll","Azimuth","","","",null,null,null,null));
        sensors.add(new Sensors("Giroscopio","X","Y","Z","","","",null,null,null,null));
        return sensors;
    }

}
