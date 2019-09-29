@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_of_event_activity);

        car_accident= (Button) findViewById(R.id.car_accident);
        ...

        car_accident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_accident_click++;
                if ((car_accident_click%2)-1==0) {
                    car_accident.setBackgroundColor(getResources()
                    .getColor(R.color.colorSecondaryDark));
                }
                else
                {
                    car_accident.setBackgroundColor(getResources()
                    .getColor(R.color.colorSecondaryLight));
                }
            }
        });
}