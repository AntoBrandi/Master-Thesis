package com.example.myiotdevice;

import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

public class CountDownRunner implements Runnable {

    public FirstSensorActivity mainActivity;

    public CountDownRunner(FirstSensorActivity _activity){
        this.mainActivity=_activity;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                updateTime();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
            }
        }
    }

    public void updateTime(){
        this.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    TextView time = (TextView) mainActivity.findViewById(R.id.MainTime);
                    TextView data = (TextView) mainActivity.findViewById(R.id.MainDate);
                    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH)+1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);
                    time.setText(((hour<10)? "0"+hour : hour)+":"+((minute<10)? "0"+minute : minute)+":"+((second<10)? "0"+second : second));
                    data.setText(((day<10)? "0"+day : day) + " / " + ((month<10)? "0"+month : month) + " / " + ((year<10)? "0"+year : year));
                }catch (Exception e) {}
            }
        });
    }
}
