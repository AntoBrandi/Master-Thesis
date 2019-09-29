@Override
protected void onPostExecute(String result) {
    // Print markers on the map
    if(responses!=null) {
        for (int i = 0; i < responses.size(); i++) {
            item_latitude = Double.parseDouble(responses.get(i)
            .get_payload().get_publication_latitude());
            item_longitude = Double.parseDouble(responses.get(i)
            .get_payload().get_publication_longitude());
            item_title = responses.get(i).get_payload()
            .get_publication_location();

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