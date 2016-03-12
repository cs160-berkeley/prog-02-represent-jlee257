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

        String[] locInfo = ((Election2016) this.getApplication()).getLocInfo();

        if (extras != null) {
            int repNum = extras.getInt("REPSELECT");
            pager.setAdapter(new MyGridViewPagerAdapter(this, repNum, locInfo));

        } else {
            pager.setAdapter(new MyGridViewPagerAdapter(this, 0, locInfo));
        }
    }


    private class MyGridViewPagerAdapter extends GridPagerAdapter {
        final Context mContext;
        final int row0;
        final int row_num;
        final String[] locInfo0;

        public MyGridViewPagerAdapter(final Context context, int row, String[] locInfo) {
            mContext = context;
            row0 = row;
            locInfo0 = locInfo;
            row_num = (locInfo0.length - 2) / 2;
        }

        @Override
        public int getColumnCount(int arg0) {
            return 1;
        }

        @Override
        public int getRowCount() {
            return row_num;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int row, int col) {
            final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.grid_view_pager_item, container, false);
            final TextView cand_name = (TextView) view.findViewById(R.id.cand_name);
            final TextView cand_party = (TextView) view.findViewById(R.id.cand_party);
            final Button button = (Button) view.findViewById(R.id.sendbutton);

            final int index = (row0 + row) % row_num;
            String party_name = locInfo0[index * 2 + 4];
            cand_name.setText(locInfo0[index * 2 + 3]);
            cand_party.setText(party_name);

            if (party_name.equals("Democratic")) {
                button.getBackground().setColorFilter(0xFF0000FF, PorterDuff.Mode.MULTIPLY);
            } else if (party_name.equals("Republican")) {
                button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
            } else {
                button.getBackground().setColorFilter(0xFF888888, PorterDuff.Mode.MULTIPLY);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("DEBUGTAG", "Watch button " + String.valueOf(index) + "is clicked");
                    Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                    sendIntent.putExtra("REPSELECT", String.valueOf(index));
                    startService(sendIntent);
                }
            });
            Log.d("DEBUGTAG", "Watch button is set");
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
