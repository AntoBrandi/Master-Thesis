private class QueryTask extends AsyncTask<String,Void,String>{

    @Override
    protected String doInBackground(String... params) {
        try {
            ...
        }
        catch (Exception e){}
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
        ...
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}