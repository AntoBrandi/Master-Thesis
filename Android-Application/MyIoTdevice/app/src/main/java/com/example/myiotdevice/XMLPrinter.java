package com.example.myiotdevice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class XMLPrinter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xml_printer_activity);
        TextView xml_view = (TextView) findViewById(R.id.xml_text);

        Intent i = getIntent();
        String XML = i.getStringExtra("XML");

        xml_view.setText(XML);
    }
}
