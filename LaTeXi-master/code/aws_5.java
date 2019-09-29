send_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isConnected==true) {
                publish(messageJSON);
                //disconnect();
                Toast.makeText(getApplicationContext(), "Publication Complete", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Make sure you are connected to the server",Toast.LENGTH_SHORT);
            }
        }
    });
}
private void publish(String msg){
    try {
        mqttManager.publishString(msg, TOPIC_NAME, AWSIotMqttQos.QOS0);
    } catch (Exception e) {
        Log.e(LOG_TAG, "Publish error.", e);
    }
}