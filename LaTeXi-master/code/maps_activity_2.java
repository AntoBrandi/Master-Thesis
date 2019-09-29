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
                mMap.clear();
                refreshGPS();
                return true;
            case R.id.item_medium_radius:
                // set min e max latitude/logitude
                radius=500;
                mMap.clear();
                refreshGPS();
                return true;
            case R.id.item_large_radius:
                // set min e max latitude/logitude
                radius=1000;
                mMap.clear();
                refreshGPS();
                return true;
            case R.id.item_all:
                // set min e max latitude/logitude
                radius=8000;
                mMap.clear();
                refreshGPS();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }