public class AWSConnectionActivity extends AppCompatActivity {
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.aws_connection_activity);
    ...
    // Initialize the AWS Cognito credentials provider
    credentialsProvider = new CognitoCachingCredentialsProvider(
            getApplicationContext(), // context
            COGNITO_POOL_ID, // Identity Pool ID
            MY_REGION // Region
    );
    Region region = Region.getRegion(MY_REGION);
    // MQTT Client
    mqttManager = new AWSIotMqttManager(clientId, CUSTOMER_SPECIFIC_ENDPOINT);
    // Set keepalive to 10 seconds.  Will recognize disconnects more quickly but will also send
    // MQTT pings every 10 seconds.
    mqttManager.setKeepAlive(10);
    // Set Last Will and Testament for MQTT.  On an unclean disconnect (loss of connection)
    // AWS IoT will publish this message to alert other clients.
    AWSIotMqttLastWillAndTestament lwt = new AWSIotMqttLastWillAndTestament("my/lwt/topic",
            "Android client lost connection", AWSIotMqttQos.QOS0);
    mqttManager.setMqttLastWillAndTestament(lwt);
    // IoT Client (for creation of certificate if needed)
    mIotAndroidClient = new AWSIotClient(credentialsProvider);
    mIotAndroidClient.setRegion(region);
    keystorePath = getFilesDir().getPath();
    keystoreName = KEYSTORE_NAME;
    keystorePassword = KEYSTORE_PASSWORD;
    certificateId = CERTIFICATE_ID;<
    try {
        if (AWSIotKeystoreHelper.isKeystorePresent(keystorePath, keystoreName)) {
            if (AWSIotKeystoreHelper.keystoreContainsAlias(certificateId, keystorePath,
                    keystoreName, keystorePassword)) {
                Log.i(LOG_TAG, "Certificate " + certificateId
                        + " found in keystore - using for MQTT.");
                clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(certificateId,
                        keystorePath, keystoreName, keystorePassword);
            } else {
                Log.i(LOG_TAG, "Key/cert " + certificateId + " not found in keystore.");
            }
        } else {
            Log.i(LOG_TAG, "Keystore " + keystorePath + "/" + keystoreName + " not found.");
        }
    } catch (Exception e) {
        Log.e(LOG_TAG, "An error occurred retrieving cert/key from keystore.", e);
    }
    if (clientKeyStore == null) {
        Log.i(LOG_TAG, "Cert/key was not found in keystore - creating new key and certificate.");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CreateKeysAndCertificateRequest createKeysAndCertificateRequest =
                            new CreateKeysAndCertificateRequest();
                    createKeysAndCertificateRequest.setSetAsActive(true);
                    final CreateKeysAndCertificateResult createKeysAndCertificateResult;
                    createKeysAndCertificateResult =
                            mIotAndroidClient
                            .createKeysAndCertificate(
                                createKeysAndCertificateRequest);
                    Log.i(LOG_TAG,
                            "Cert ID: " +
                                    createKeysAndCertificateResult
                                    .getCertificateId() +
                                    " created.");
                    AWSIotKeystoreHelper
                    .saveCertificateAndPrivateKey(certificateId,
                            createKeysAndCertificateResult
                            .getCertificatePem(),
                            createKeysAndCertificateResult
                            .getKeyPair().getPrivateKey(),
                            keystorePath, keystoreName, keystorePassword);
                    clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(certificateId,
                            keystorePath, keystoreName, keystorePassword);
                    AttachPrincipalPolicyRequest policyAttachRequest =
                            new AttachPrincipalPolicyRequest();
                    policyAttachRequest.setPolicyName(AWS_IOT_POLICY_NAME);
                    policyAttachRequest
                    .setPrincipal(createKeysAndCertificateResult
                            .getCertificateArn());
                    mIotAndroidClient
                    .attachPrincipalPolicy(policyAttachRequest);
                } catch (Exception e) {
                    Log.e(LOG_TAG,
                            "Exception occurred when generating new private key and certificate.",
                            e);
                }
            }
        }).start();
    }
}

