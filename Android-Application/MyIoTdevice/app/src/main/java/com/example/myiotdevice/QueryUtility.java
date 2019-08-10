package com.example.myiotdevice;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtility {

    /** Tag per i messaggi di LOG */
    public static final String LOG_TAG = QueryUtility.class.getSimpleName();

    private QueryUtility() {
    }

    public static List<Earthquake> fetchEarthquakeData(String requestURL)
    {
        // Creo un oggetto URL
        URL url = createUrl(requestURL);

        // Eseguo la richiesta HTTP al sito URL e ricevo la risposta
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"IOException throwed.",e);
        }

        /**
         * Ritorno alla main activity la lista di earthquakes da stampare a schermo nella UI
         */
        List<Earthquake> earthquakes = extractEarthquakes(jsonResponse);
        return earthquakes;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;
        if (url==null)
            return jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG,"Error during response code. Returned code: "+urlConnection.getResponseCode());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static List<Earthquake> extractEarthquakes(String JSON_Response)
    {

        if (TextUtils.isEmpty(JSON_Response)) {
            return null;
        }

        List<Earthquake> earthquakes = new ArrayList<>();

        /**
         * Provo a leggere dalla stringa in formato JSON per estrarre i valori desiderati.
         * Se ci sono problemi con questa estrazione, eseguo il blocco catch che cattura l'errore in maniera tae da
         * non far crashare la applicazione.
         */

        try {

            // Definisco il percorso che porta dalla root alla informazione che vogliamo
            JSONObject root = new JSONObject(JSON_Response);
            JSONArray features = root.getJSONArray("features");


            // Estraggo tutti gli elementi dell'array features (tutti gli earhquakes) contenuti nell'array
            for (int i=0;i<=features.length();i++)
            {
                JSONObject elements = features.getJSONObject(i);
                JSONObject properties = elements.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");


                // Ogni qual volta estraggo i valori che mi servono creo un nuovo elemento dell'array
                Earthquake earthquake = new Earthquake(mag,place,time,url);
                earthquakes.add(earthquake);
            }

        }
        catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }

}

