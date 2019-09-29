public class AWSActivity extends AppCompatActivity {

    private final String TOPIC_NAME = "EU_WEST_2";

    // AWS Connection
    static final String LOG_TAG = AWSConnectionActivity.class.getCanonicalName();
    // --- Constants to modify per your configuration ---
    // IoT endpoint
    // AWS Iot CLI describe-endpoint call returns: XXXXXXXXXX.iot.<region>.amazonaws.com
    private static final String CUSTOMER_SPECIFIC_ENDPOINT = "ENDPOINT-HERE";
    // Cognito pool ID. For this app, pool needs to be unauthenticated pool with
    // AWS IoT permissions.
    private static final String COGNITO_POOL_ID = "POOLID-HERE";
    // Name of the AWS IoT policy to attach to a newly created certificate
    private static final String AWS_IOT_POLICY_NAME = "myAndroidIoTPolicy";
    // Region of AWS IoT
    private static final Regions MY_REGION = Regions.EU_WEST_2;
    // Filename of KeyStore file on the filesystem
    private static final String KEYSTORE_NAME = "iot_keystore";
    // Password for the private key in the KeyStore
    private static final String KEYSTORE_PASSWORD = "PASSWORD-HERE";
    // Certificate and key aliases in the KeyStore
    private static final String CERTIFICATE_ID = "default";
    AWSIotClient mIotAndroidClient;
    AWSIotMqttManager mqttManager;
    String clientId;
    String keystorePath;
    String keystoreName;
    String keystorePassword;
    KeyStore clientKeyStore = null;
    String certificateId;
    CognitoCachingCredentialsProvider credentialsProvider;
    private boolean isConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aws_connection_activity);
        ...
    }
}