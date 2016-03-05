package com.example.aspire.election2016;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Candidates extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView2);
        LinearLayout panels = (LinearLayout) findViewById(R.id.panels);

        panels.getChildAt(1).setBackgroundColor(getResources().getColor(R.color.republican));
        panels.getChildAt(2).setBackgroundColor(getResources().getColor(R.color.independent));

        ((TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) panels.getChildAt(1))
                .getChildAt(1)).getChildAt(0)).getChildAt(0)).setText("Republican");
        ((TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) panels.getChildAt(2))
                .getChildAt(1)).getChildAt(0)).getChildAt(0)).setText("Independent");




        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String candidateString = extras.getString("CANDIDATE");
            if (candidateString.equals("A")) {
                final ViewGroup panel = (ViewGroup) ((ViewGroup) scrollView.getChildAt(0)).getChildAt(0);
                View hidden = panel.getChildAt(3);
                TextView text = (TextView) ((ViewGroup) panel.getChildAt(4)).getChildAt(0);
                hidden.setVisibility(hidden.VISIBLE);
                text.setText("COLLAPSE");
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, panel.getTop());
                    }
                });

            } else if (candidateString.equals("B")) {
                final ViewGroup panel = (ViewGroup) ((ViewGroup) scrollView.getChildAt(0)).getChildAt(1);
                View hidden = panel.getChildAt(3);
                TextView text = (TextView) ((ViewGroup) panel.getChildAt(4)).getChildAt(0);
                hidden.setVisibility(hidden.VISIBLE);
                text.setText("COLLAPSE");
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, panel.getTop());
                    }
                });


            } else {
                final ViewGroup panel = (ViewGroup) ((ViewGroup) scrollView.getChildAt(0)).getChildAt(2);
                View hidden = panel.getChildAt(3);
                TextView text = (TextView) ((ViewGroup) panel.getChildAt(4)).getChildAt(0);
                hidden.setVisibility(hidden.VISIBLE);
                text.setText("COLLAPSE");
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, panel.getTop());
                    }
                });

            }
        }
    }

    public void expand(View v){
        View hidden = ((ViewGroup)v.getParent()).getChildAt(3);
        int index = ((ViewGroup)v.getParent().getParent()).indexOfChild((ViewGroup)v.getParent());
        hidden.setVisibility(hidden.isShown() ? hidden.GONE : hidden.VISIBLE);
        TextView text = (TextView) ((ViewGroup)v).getChildAt(0);

        if (text.getText().toString().equals("EXPAND")) {
            text.setText("COLLAPSE");
            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
            switch (index) {
                case 0:
                    sendIntent.putExtra("CANDIDATE", "CandA");
                    break;
                case 1:
                    sendIntent.putExtra("CANDIDATE", "CandB");
                    break;
                case 2:
                    sendIntent.putExtra("CANDIDATE", "CandC");
                    break;
                default:
                    sendIntent.putExtra("CANDIDATE", "CandC");
            }
            startService(sendIntent);
        } else {
            text.setText("EXPAND");
        }
    }
}
