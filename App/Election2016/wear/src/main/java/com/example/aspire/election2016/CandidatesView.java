package com.example.aspire.election2016;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class CandidatesView extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates_view);
        final RelativeLayout stub = (RelativeLayout) findViewById(R.id.relative);
        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String candidateString = extras.getString("CANDIDATE");
            if (candidateString.equals("A")) {
                pager.setAdapter(new MyGridViewPagerAdapter(this, 0));
            } else if (candidateString.equals("B")) {
                pager.setAdapter(new MyGridViewPagerAdapter(this, 1));
            } else {
                pager.setAdapter(new MyGridViewPagerAdapter(this, 2));
            }

        } else {
            pager.setAdapter(new MyGridViewPagerAdapter(this, 2));
        }
    }


    private class MyGridViewPagerAdapter extends GridPagerAdapter {
        final Context mContext;
        final int row0;

        public MyGridViewPagerAdapter(final Context context, int row) {
            mContext = context;
            row0 = row;
        }

        @Override
        public int getColumnCount(int arg0) {
            return 1;
        }

        @Override
        public int getRowCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int row, int col) {
            final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.grid_view_pager_item, container, false);
            final TextView textView = (TextView) view.findViewById(R.id.textView4);
            final Button button = (Button) view.findViewById(R.id.sendbutton);

            if ((row0+row)%3==0) {
                textView.setText("Democratic");
                button.getBackground().setColorFilter(0xFF0000FF, PorterDuff.Mode.MULTIPLY);
            } else if ((row0+row)%3==1) {
                textView.setText("Republican");
                button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
            } else {
                textView.setText("Independent");
                button.getBackground().setColorFilter(0xFF888888, PorterDuff.Mode.MULTIPLY);
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("T", "Watch button is clicked");
                    Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);

                    if ((row0+row)%3==0) {
                        sendIntent.putExtra("CANDIDATE", "CandA");
                    } else if ((row0+row)%3==1) {
                        sendIntent.putExtra("CANDIDATE", "CandB");
                    } else {
                        sendIntent.putExtra("CANDIDATE", "CandC");
                    }
                    startService(sendIntent);
                }
            });
            Log.d("T", "Watch button is set");
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int row, int col, Object view) {
            container.removeView((View)view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
}


//private class MyGridViewPagerAdapter extends GridPagerAdapter {
//    @Override
//    public int getColumnCount(int arg0) {
//        return 2;
//    }
//
//    @Override
//    public int getRowCount() {
//        return 2;
//    }
//
//    @Override
//    protected Object instantiateItem(ViewGroup container, int row, int col) {
//        final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.grid_view_pager_item, container, false);
//        final TextView textView = (TextView) view.findViewById(R.id.textView);
//        textView.setText(String.format("Page:\n%1$s, %2$s", row, col));
//        container.addView(view);
//        return view;
//    }
//
//    @Override
//    protected void destroyItem(ViewGroup container, int row, int col, Object view) {
//        container.removeView((View)view);
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view==object;
//    }
//}
