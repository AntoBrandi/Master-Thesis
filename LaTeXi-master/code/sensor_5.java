public class MySensorListener implements SensorEventListener {
    public FirstSensorActivity mainActivity;

    public MySensorListener(FirstSensorActivity activity){
        this.mainActivity = activity;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy){}
    public void onSensorChanged(final SensorEvent event){
        this.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Sensor mySensor = event.sensor;
                if (mySensor.getType()==Sensor.TYPE_ACCELEROMETER){
                    ...
                }       
            }
        });
    }
}