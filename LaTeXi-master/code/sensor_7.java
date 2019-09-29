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
                    output = lowPassFilter(event.values.clone(),output);
                    // FILTERED
                    mainActivity.acceleration_coordinateX
                    .setText(String.format("%.2f", output[0])+" m/s^2");
                    mainActivity.acceleration_coordinateY
                    .setText(String.format("%.2f", output[1])+" m/s^2");
                    mainActivity.acceleration_coordinateZ
                    .setText(String.format("%.2f", output[2])+" m/s^2");
                }       
            }
        });
    }
}