package com.example.aspire.election2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final LinearLayout stub = (LinearLayout) findViewById(R.id.watch_view_stub);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        
        if (extras != null) {
            String location_info = extras.getString("LOCATIONINFO");
            if (!((Election2016) this.getApplication()).setLocationInfo(location_info)) {
                Toast.makeText(this, "LOCATION CHANGE FAILED", Toast.LENGTH_LONG).show();
            };
        }


        RelativeLayout menu1 = (RelativeLayout) findViewById(R.id.localCandidates);
        RelativeLayout menu2 = (RelativeLayout) findViewById(R.id.election2012);


        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, CandidatesView.class);
                startActivity(i);
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, PresidentialView.class);
                startActivity(i);
            }
        });


    }
}
