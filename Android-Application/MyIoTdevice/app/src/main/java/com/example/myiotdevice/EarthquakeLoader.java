package com.example.myiotdevice;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Uso un LOader piuttosto che un async Task al fine di migliorare la gestione della memoria.
 * In tal caso quindi vado ad implementare necessariamente il metodo loadInBackground il quale
 * sostituisce in tutto e per tutto il metodo doInBAckground dell'AsyncTask.
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String mUrl;

    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    public EarthquakeLoader(Context context,String url) {
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Earthquake> earthquakes= QueryUtility.fetchEarthquakeData(mUrl);
        // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
        return earthquakes;
    }
}
