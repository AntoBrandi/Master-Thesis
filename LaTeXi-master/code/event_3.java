@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_of_event_activity);
        ...
        Gson gson= new Gson();
        messageJSON = gson.toJson(p);
}