public class SensorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firts_sensor_activity);

        ...

        // Parameters shared by all the sensors
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }
}