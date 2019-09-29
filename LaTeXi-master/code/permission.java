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