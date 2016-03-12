package com.example.aspire.election2016;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.util.Random;


public class PresidentialView extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presidential_view);
        final RelativeLayout stub = (RelativeLayout) findViewById(R.id.relative);

        TextView locText = (TextView) findViewById(R.id.loctext);
        TextView oba_num_text = (TextView) findViewById(R.id.oba_num);
        TextView rom_num_text = (TextView) findViewById(R.id.rom_num);
        RelativeLayout oba_bar = (RelativeLayout) findViewById(R.id.oba_bar);
        RelativeLayout gray_bar = (RelativeLayout) findViewById(R.id.gray_bar);
        RelativeLayout rom_bar = (RelativeLayout) findViewById(R.id.rom_bar);

        locText.setText(((Election2016) this.getApplication()).getZip());

        int oba_num = (((Election2016) this.getApplication()).getObamaVote());
        int rom_num = (((Election2016) this.getApplication()).getRomneyVote());
        int gray_num = 1000 - oba_num - rom_num;

        LinearLayout.LayoutParams paramObama = new LinearLayout.
                LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, oba_num);
        LinearLayout.LayoutParams paramGray = new LinearLayout.
                LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, gray_num);
        LinearLayout.LayoutParams paramRomney = new LinearLayout.
                LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, rom_num);

        oba_bar.setLayoutParams(paramObama);
        gray_bar.setLayoutParams(paramGray);
        rom_bar.setLayoutParams(paramRomney);
        Log.d("DEBUGTAG", "bars set to "
                + String.valueOf(oba_num) + " "
                + String.valueOf(gray_num) + " "
                + String.valueOf(rom_num));


                oba_num_text.setText(Double.toString(oba_num / 10.0) + "%");
        rom_num_text.setText(Double.toString(rom_num / 10.0) + "%");
    }
}
