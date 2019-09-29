@Override
protected String doInBackground(String... params) {
    try {
        ...

        result = dynamoDBMapper.query(PublicationDO.class, queryExpression);

        gson = new Gson();
        responses = new ArrayList<JSON_Response>();
        stringBuilder = new StringBuilder();
        // Loop through query results
        for (int i = 0; i < result.size(); i++) {
            jsonFormOfItem = gson.toJson(result.get(i));
            response = new JSON_Response();
            response = new Gson().fromJson(jsonFormOfItem, JSON_Response.class);
        }
    }
    catch (Exception e){}
    return "Executed";
}
