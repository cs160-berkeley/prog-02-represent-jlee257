package com.example.aspire.election2016;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Locations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button useCurrentLocation = (Button) findViewById(R.id.button2);
        useCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("CANDIDATE", "zip12345");
                startService(sendIntent);
                Intent i = new Intent(Locations.this, Candidates.class);
                startActivity(i);
            }
        });


        Button useZipcode = (Button) findViewById(R.id.button3);
        final EditText editText = (EditText) findViewById(R.id.editText);
        useZipcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("CANDIDATE", "zip"+editText.getText().toString());
                startService(sendIntent);
                Intent i = new Intent(Locations.this, Candidates.class);
                startActivity(i);
            }
        });

    }

}
