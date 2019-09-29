public class SensorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firts_sensor_activity);

        ...

        mAccelerometerThread = new HandlerThread("Accelerometer Thread",Thread.NORM_PRIORITY);
        mAccelerometerThread.start();
        mAccelerometerHandler = new Handler(mAccelerometerThread.getLooper());
    }
}