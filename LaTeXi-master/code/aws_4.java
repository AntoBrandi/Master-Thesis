private void refreshConnection(){
    Log.d(LOG_TAG, "clientId = " + clientId);
    try {
        mqttManager.connect(clientKeyStore, new AWSIotMqttClientStatusCallback() {
            @Override
            public void onStatusChanged(final AWSIotMqttClientStatus status,
                                        final Throwable throwable) {
                Log.d(LOG_TAG, "Status = " + String.valueOf(status));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (status == AWSIotMqttClientStatus.Connecting) {
                            statusView.setText("Connecting...");
                        } else if (status == AWSIotMqttClientStatus.Connected) {
                            statusView.setText("Connected");
                            isConnected = true;
                        } else if (status == AWSIotMqttClientStatus.Reconnecting) {
                            if (throwable != null) {
                                Log.e(LOG_TAG, "Connection error.", throwable);
                            }
                            statusView.setText("Reconnecting");
                        } else if (status == AWSIotMqttClientStatus.ConnectionLost) {
                            if (throwable != null) {
                                Log.e(LOG_TAG, "Connection error.", throwable);
                            }
                            statusView.setText("Disconnected");
                            isConnected = false;
                        } else {
                            statusView.setText("Disconnected");
                            isConnected = false;
                        }
                    }
                });
            }
        });
    } catch (final Exception e) {
        Log.e(LOG_TAG, "Connection error.", e);
        statusView.setText("Error! " + e.getMessage());
    }
}