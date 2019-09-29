@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_of_event_activity);
        ...
        XStream xml = new XStream();
        xml.registerConverter(new SingleObjectConverter([p].class));
}