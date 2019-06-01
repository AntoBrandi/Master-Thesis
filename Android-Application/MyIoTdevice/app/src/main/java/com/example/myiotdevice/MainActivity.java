package com.example.myiotdevice;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ask for permissions


        // ---- DATE AND TIME ----
        Thread timeThread = null;
        Runnable myRunnableThread = new CountDownRunner();
        timeThread = new Thread(myRunnableThread);
        timeThread.start();
    }





    // --------- DATE AND TIME SECTION--------------------------------------------------------------
    class CountDownRunner implements Runnable {

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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TextView time = (TextView) findViewById(R.id.MainTime);
                        TextView data = (TextView) findViewById(R.id.MainDate);
                        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        int second = calendar.get(Calendar.SECOND);
                        time.setText(hour+":"+minute+":"+second);
                        data.setText(day + " / " + month + " / " + year);
                    }catch (Exception e) {}
                }
            });
        }
    }
    // ------ END OF DATE AND TIME SECTION ---------------------------------------------------------



}
