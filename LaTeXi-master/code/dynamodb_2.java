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
                    if(getDistanceFromPoint(response.get_payload().get_publication_latitude(),response.get_payload().get_publication_longitude())<radius){
                        responses.add(response);
                    }
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
}