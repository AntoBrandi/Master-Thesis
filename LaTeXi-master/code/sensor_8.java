if (mySensor.getType()==Sensor.TYPE_ACCELEROMETER){
    ...
    // Sensor information and name
    mainActivity.accelerometer_name= mySensor.getName();
    mainActivity.accelerometer_resolution = String.valueOf(mySensor.getResolution());
    mainActivity.accelerometer_vendor = mySensor.getVendor();
    mainActivity.accelerometer_type = String.valueOf(mySensor.getType());
}                      
            