public class SensorActivity extends AppCompatActivity {
    public MySensorListener msl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firts_sensor_activity);

        ...

        msl = new MySensorListener(this);
        senSensorManager.registerListener(msl,senAccelerometer,
        SensorManager.SENSOR_DELAY_NORMAL,mAccelerometerHandler);
    }
}