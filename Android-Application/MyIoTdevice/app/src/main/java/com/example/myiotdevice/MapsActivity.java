package com.example.myiotdevice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.gson.Gson;

import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Database variables
    private DynamoDBMapper dynamoDBMapper;
    private MapsActivity mapsActivity;
    private PublicationDO publicationItem;
    private StringBuilder stringBuilder;
    private PaginatedList<PublicationDO> result;
    private ArrayList<JSON_Response> responses;
    private Gson gson;
    private String jsonFormOfItem;
    private JSON_Response response;
    private MarkerOptions markerOption;
    private double item_latitude;
    private double item_longitude;
    private String item_title;

    private String TAG = "DynamoDb_Demo";
    // GPS VARIABLES
    private double latitude;
    private double longitude;
    private String address;
    private double minLat;
    private double maxLat;
    private double minLong;
    private double maxLong;
    private double radius;
    private LatLng actualPosition;
    private String providerId = LocationManager.GPS_PROVIDER;
    private LocationManager locationManager=null;
    private static final int MIN_DIST=20;
    private static final int MIN_PERIOD=30000;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateGUI(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
            showSettingsAlert();
        }
    };

    private GoogleMap mMap;
    // View poliba as staring point of the map
    private static final LatLng PoliBa = new LatLng(41.109044, 16.878381);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Default value for the radius
        radius=100;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ASK FOR PERMISSIONS
        // GPS access
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        // INTERNET access
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
            return;
        }

        // Database comunication setup
        mapsActivity = this;

        // AWSMobileClient enables AWS user credentials to access your table
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {

            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {

                // Add code to instantiate a AmazonDynamoDBClient
                AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
                dynamoDBMapper = DynamoDBMapper.builder()
                        .dynamoDBClient(dynamoDBClient)
                        .awsConfiguration(
                                AWSMobileClient.getInstance().getConfiguration())
                        .build();

            }
        }).execute();
    }

    @Override
    protected void onResume(){
        super.onResume();

        // GPS
        refreshGPS();
    }

    @Override
    protected void onPause(){
        super.onPause();

        if (locationManager!=null && locationManager.isProviderEnabled(providerId))
            locationManager.removeUpdates(locationListener);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_update:
                refreshGPS();
                return true;
            case R.id.item_upload:
                // Go to the new activity
                Intent i = new Intent(MapsActivity.this,FirstSensorActivity.class);
                i.putExtra("latitude",String.valueOf(latitude));
                i.putExtra("longitude",String.valueOf(longitude));
                i.putExtra("address",address);
                startActivity(i);
                return true;
            case R.id.item_small_radius:
                // set min e max latitude/logitude
                radius=100;
                return true;
            case R.id.item_medium_radius:
                // set min e max latitude/logitude
                radius=500;
                return true;
            case R.id.item_large_radius:
                // set min e max latitude/logitude
                radius=1000;
                return true;
            case R.id.item_all:
                // set min e max latitude/logitude
                radius=0;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(PoliBa).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PoliBa));
    }

    /**
     * GPS Data and functions
     */
    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MapsActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MapsActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void updateGUI(Location location)
    {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        // set min/max longitude/latitude
        if(radius==100){
            minLat = latitude-10;
            maxLat = latitude+10;
            minLong = longitude-10;
            maxLong = longitude+10;
        }
        else if (radius==500){
            minLat = latitude-50;
            maxLat = latitude+50;
            minLong = longitude-50;
            maxLong = longitude+50;
        }


        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(latitude, longitude,getApplicationContext(), new GeocoderHandler());

        // remove old marker
        mMap.clear();
        // add new circle into the actual position
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        else{
            actualPosition = new LatLng(latitude,longitude);
            mMap.addCircle(new CircleOptions().center(actualPosition).radius(50).fillColor(R.color.colorSecondary));
        }
    }

    private void refreshGPS(){
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null)
                updateGUI(location);

            locationManager.requestLocationUpdates(providerId, MIN_PERIOD, MIN_DIST, locationListener);

            // query execution managed by async task
            new QueryTask().execute("");
            //query();
        }
        catch(Exception e){}

    }


    private class GeocoderHandler extends Handler {

        @Override
        public void handleMessage(Message message) {


            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    address = bundle.getString("address");
                    break;
                default:
                    address = null;
            }
        }
    }


    /**
     * Database functions
     * Gestione attraverso gli AsyncTask
     */
    private class QueryTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                PublicationDO publication = new PublicationDO();
                publication.set_creator("it-357221066422461");       //partition key
                publication.set_publication_time("14/07/2019 09:49:51"); //range key

                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                        .withAttributeValueList(new AttributeValue().withS("it"));
                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(publication)
                        //.withRangeKeyCondition("creator", rangeKeyCondition)
                        .withConsistentRead(false);

                result = dynamoDBMapper.query(PublicationDO.class, queryExpression);

                gson = new Gson();
                responses = new ArrayList<JSON_Response>();
                stringBuilder = new StringBuilder();

                // Loop through query results
                for (int i = 0; i < result.size(); i++) {
                    jsonFormOfItem = gson.toJson(result.get(i));
                    response = new JSON_Response();
                    response = new Gson().fromJson(jsonFormOfItem, JSON_Response.class);
                    responses.add(response);
                    Log.d("Latitude: ", responses.get(i).get_payload().get_publication_latitude());
                    stringBuilder.append(jsonFormOfItem + "\n\n");
                }
            }
            catch (Exception e){}


            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            // Print markers on the map
            if(responses!=null) {
                for (int i = 0; i < responses.size(); i++) {
                    item_latitude = Double.parseDouble(responses.get(i).get_payload().get_publication_latitude());
                    item_longitude = Double.parseDouble(responses.get(i).get_payload().get_publication_longitude());
                    item_title = responses.get(i).get_payload().get_publication_location();

                    Log.d("Latitude : ", responses.get(i).get_payload().get_publication_latitude());

                    // Reading valid data
                    if ((item_latitude != 0.0) && (item_longitude != 0.0)) {

                        markerOption = new MarkerOptions();
                        markerOption.position(new LatLng(item_latitude, item_longitude));
                        markerOption.title(item_title);
                        markerOption.icon(getMarkerIcon("#fdd835"));
                        mMap.addMarker(markerOption);
                    }
                }
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }



    /*
    public void query() {

        new Thread(new Runnable() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public void run() {
                PublicationDO publication = new PublicationDO();
                publication.set_creator("it-357221066422461");       //partition key
                publication.set_publication_time("14/07/2019 09:49:51"); //range key

                Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                        .withAttributeValueList(new AttributeValue().withS("it"));
                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(publication)
                        //.withRangeKeyCondition("creator", rangeKeyCondition)
                        .withConsistentRead(false);

                result = dynamoDBMapper.query(PublicationDO.class, queryExpression);

                gson = new Gson();
                responses = new ArrayList<JSON_Response>();
                stringBuilder = new StringBuilder();

                // Loop through query results
                for (int i = 0; i < result.size(); i++) {
                    jsonFormOfItem = gson.toJson(result.get(i));
                    response = new JSON_Response();
                    response = new Gson().fromJson(jsonFormOfItem,JSON_Response.class);
                    responses.add(response);
                    Log.d("Latitude: ",responses.get(i).get_payload().get_publication_latitude());
                    stringBuilder.append(jsonFormOfItem + "\n\n");
                }

                // Add your code here to deal with the data result
                mapsActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Query results: ", stringBuilder.toString());

                        if (result.isEmpty()) {
                            // There were no items matching your query.
                            Log.d("No data",null);
                        }
                    }
                });
            }
        }).start();
    }




    public void read() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                publicationItem = dynamoDBMapper.load(
                        PublicationDO.class,
                        "it-357221066422461",       // Partition key (hash key)
                        "14/07/2019 09:49:51");    // Sort key (range key)

                // Item read
                mapsActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (publicationItem != null) {
                            String creator = publicationItem.get_creator();
                            Log.d("Publication class= ",publicationItem.toString()+". \n The publication creator is: "+creator);
                        }
                        else{
                            Log.d("No result",null);
                        }
                    }
                });

            }
        }).start();
    }
    */
}