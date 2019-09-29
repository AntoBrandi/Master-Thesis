package com.example.myiotdevice;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // GPS VARIABLES
    private double latitude;
    private double longitude;
    private String address;
    private double radius;      // Radius in KM selected by the user
    private LatLng actualPosition;
    private String providerId = LocationManager.GPS_PROVIDER;
    private LocationManager locationManager=null;
    private static final int MIN_DIST=20;
    private static final int MIN_PERIOD=30000;
    private static final double EARTH_RADIUS = 6372.795477598;      // earth radius in KM

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
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

        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(latitude, longitude,getApplicationContext(), new GeocoderHandler());


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
}