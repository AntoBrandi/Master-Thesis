
package com.example.myiotdevice;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressSolver extends AsyncTask<Location,Void,String> {

    private Geocoder geo = null;
    public MainActivity activity;

    // Uso il costruttore per ricevere il context della activity chiamante
    public AddressSolver(MainActivity _activity){
        this.activity=_activity;
    }

    @Override
    protected String doInBackground(Location... params)
    {
        geo = new Geocoder(this.activity, Locale.getDefault());
        Location pos = params[0];
        double latitude = pos.getLatitude();
        double longitude = pos.getLongitude();
        List<Address> addresses = null;
        try {
            addresses = geo.getFromLocation(latitude,longitude,1);
        }
        catch(IOException e){

        }

        if(addresses!=null)
        {
            if (addresses.isEmpty())
            {
                return null;
            }
            else{
                if (addresses.size()>0)
                {
                    StringBuffer address = new StringBuffer();
                    Address tmp = addresses.get(0);
                    for(int y=0;y<tmp.getMaxAddressLineIndex();y++)
                        address.append(tmp.getAddressLine(y)+"\n");
                    return address.toString();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        if(result!=null) {
            updateText(R.id.MainPosition, result);
            Toast.makeText(this.activity, "Position Acquired at:"+result, Toast.LENGTH_SHORT);
        }
        else
            updateText(R.id.MainPosition,"N.A.");
    }

    private void updateText(int id,String text){
        TextView textView = (TextView) this.activity.findViewById(id);
        textView.setText(text);
    }

}

