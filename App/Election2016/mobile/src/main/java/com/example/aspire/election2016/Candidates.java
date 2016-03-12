package com.example.aspire.election2016;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Candidates extends AppCompatActivity {

    final static String auth_key = "338184c5c44a4d7ab7ec4bb199c12c49";
    final static String google_auth_key = "AIzaSyCQvutc1xrmbVWQ759edbD8q8nGaRHI1WU";

    private int num_candidates = 0;
    private ArrayList<RepBio> rep_bios = new ArrayList<RepBio>();
    private boolean use_sample = false;

    private ScrollView scrollView;
    private LinearLayout panels;
    private LayoutInflater inflater;

    private String zipCode;

    private BroadcastReceiver receiver;

//    "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=RepFrenchHill&count=2&consumerkey="

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("DEBUGTAG", "onCreate called");


        scrollView = (ScrollView) findViewById(R.id.scrollView2);
        panels = (LinearLayout) findViewById(R.id.panels);


        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            if (extras.getDouble("LAT", 9999) != 9999) {
                try {
                    decodeJSONRepInfo(getRepresentatives(
                            extras.getDouble("LAT"),
                            extras.getDouble("LONG")));
                    zipCode = extras.getString("ZIPCODE");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (extras.getString("ZIPCODE") != null) {
                try {
                    decodeJSONRepInfo(getRepresentatives(extras.getString("ZIPCODE")));
                    zipCode = extras.getString("ZIPCODE");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//            } else if (extras.getInt("REPSELECT", -1) != -1) {
//                String loc[] = ((Represent) this.getApplication()).getAddress();
//                try {
//                    decodeJSONRepInfo(getRepresentatives(loc[1]));
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                final ViewGroup panel = (ViewGroup) ((ViewGroup) scrollView
//                        .getChildAt(0))
//                        .getChildAt(extras.getInt("REPSELECT", -1));
//                View hidden = panel.getChildAt(3);
//                TextView text = (TextView) ((ViewGroup) panel.getChildAt(4)).getChildAt(0);
//                hidden.setVisibility(hidden.VISIBLE);
//                text.setText("COLLAPSE");
//                scrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        scrollView.scrollTo(0, panel.getTop());
//                    }
//                });
            } else {
                Log.d("DEBUGTAG", "Candidates: no extras found!");
            }
        }



        //get 2012 vote info
        String vote2012_string = null;
        try {
            InputStream is = getAssets().open("electionresult.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            vote2012_string = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            decodeJSONaddress2(getLocation(zipCode), vote2012_string);
        } catch (ExecutionException e) {
            Log.d("DEBUGTAG", "voting_data_failed");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("DEBUGTAG", "voting_data_failed");
            e.printStackTrace();
        }





        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("REPSELECT");
                Log.d("DEBUGTAG", "Broadcast received" + s);

                // do something here.
                final ViewGroup panel = (ViewGroup) ((ViewGroup) scrollView
                        .getChildAt(0))
                        .getChildAt(Integer.parseInt(s));
                View hidden = panel.findViewById(R.id.hidden);
                TextView text = (TextView) panel.findViewById(R.id.textView8);
                hidden.setVisibility(hidden.VISIBLE);
                text.setText("COLLAPSE");
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, panel.getTop());
                    }
                });

            }
        };
    }



    @Override
    protected void onResume () {
        super.onResume();
        Log.d("DEBUGTAG", "onResume called");

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("REPSELECT"));

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras.getInt("REPSELECT", -1) != -1) {
            final ViewGroup panel = (ViewGroup) ((ViewGroup) scrollView
                    .getChildAt(0))
                    .getChildAt(Integer.parseInt(extras.getString("REPSELECT")));
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









    public void expand(View v){
        View hidden = ((ViewGroup)v.getParent()).findViewById(R.id.hidden);
        int index = ((ViewGroup)v.getParent().getParent()).indexOfChild((ViewGroup) v.getParent());
        hidden.setVisibility(hidden.isShown() ? hidden.GONE : hidden.VISIBLE);
        TextView text = (TextView) ((ViewGroup)v).getChildAt(0);

        if (text.getText().toString().equals("EXPAND")) {
            text.setText("COLLAPSE");
//            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
//            sendIntent.putExtra("REPSELECT", index);
//            startService(sendIntent);
        } else {
            text.setText("EXPAND");
        }
    }



    public String getRepresentatives(Double lat, Double lon) throws ExecutionException, InterruptedException {
        Log.d("DEBUGTAG", "getRepresentatives(double) called");
        RequestClient hey = new RequestClient();
        return hey.execute("http://congress.api.sunlightfoundation.com/legislators/locate?latitude="
                + String.valueOf(lat) + "&longitude="
                + String.valueOf(lon) + "&apikey=" + auth_key).get();
    }

    public String getRepresentatives(String zip) throws ExecutionException, InterruptedException {
        Log.d("DEBUGTAG", "getRepresentatives(string) called with " + zip);
        RequestClient hey = new RequestClient();
        return hey.execute("http://congress.api.sunlightfoundation.com/legislators/locate?zip="
                + zip + "&apikey=" + auth_key).get();
    }

    public String getTwitter(String username) throws ExecutionException, InterruptedException {
        Log.d("DEBUGTAG", "getTwistterAuthentication called");
        RequestClient request = new RequestClient();
        return request.execute("https://api.twitter.com/1.1/statuses/user_timeline.json?count=1&screen_name=" + username + "&include_rts=true", username).get();
    }

    public String getBills(String bioguide_id) throws ExecutionException, InterruptedException {
        Log.d("DEBUGTAG", "getBills(string) called with " + bioguide_id);
        RequestClient hey = new RequestClient();
        return hey.execute("http://congress.api.sunlightfoundation.com/bills?sponsor_id="
                + bioguide_id + "&apikey=" + auth_key).get();
    }

    public String getCommittees(String bioguide_id) throws ExecutionException, InterruptedException {
        Log.d("DEBUGTAG", "getCommittees(string) called with " + bioguide_id);
        RequestClient hey = new RequestClient();
        return hey.execute("http://congress.api.sunlightfoundation.com/committees?member_ids="
                + bioguide_id + "&apikey=" + auth_key).get();
    }

    public String getLocation(String zip) throws ExecutionException, InterruptedException {


        if (use_sample) {
            return getSampleLocation();
        }



        Log.d("DEBUGTAG", "getLocation: " + zip);

        RequestClient hey = new RequestClient();
        return hey.execute("https://maps.googleapis.com/maps/api/geocode/json?address="
                + zip + ",united+states&key=" + google_auth_key).get();
    }

    private void decodeJSONRepInfo(String repsJSON0) {

        try {
            JSONObject repsJSON = new JSONObject(repsJSON0);

            Log.d("DEBUGTAG", "try decoding representative information");
            JSONArray repsArray = repsJSON.getJSONArray("results");
            int count = repsJSON.getInt("count");
            num_candidates = count;
            Log.d("DEBUGTAG", String.valueOf(count) + " representatives found");

            rep_bios = new ArrayList<RepBio>();
            for (int i = 0; i < count; i++) {
                JSONObject repJSON = repsArray.getJSONObject(i);

                RepBio new_rep = new RepBio(repJSON.getString("bioguide_id"));
                new_rep.rep_name = repJSON.getString("first_name")
                        + " " + repJSON.getString("last_name");
                new_rep.party = partyParser(repJSON.getString("party"));
                new_rep.oc_email = repJSON.getString("oc_email");
                new_rep.website = repJSON.getString("website");
                new_rep.twitter_id = repJSON.getString("twitter_id");
                new_rep.term_ends_date = repJSON.getString("term_end");
                new_rep.house = repJSON.getString("chamber");


                rep_bios.add(new_rep);
                addPanel(new_rep);

                decodeJSONtwitter(getTwitter(new_rep.twitter_id), new_rep);
                decodeJSONBills(getBills(new_rep.bioguide_id), new_rep);
                decodeJSONCommittees(getCommittees(new_rep.bioguide_id), new_rep);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void decodeJSONtwitter(String twit, RepBio rep_bio) {

        try {
            JSONArray twit_list0 = new JSONArray(twit);

//            Log.d("DEBUGTAG", twit.toString());
            JSONObject twit0 = twit_list0.getJSONObject(0);
            rep_bio.last_twit_time = twit0.getString("created_at");
            rep_bio.last_twit = twit0.getString("text");
            rep_bio.twitter_picture_link = twit0.getJSONObject("user").getString("profile_image_url");

            DownloadImageClient imageClient = new DownloadImageClient((ImageView) rep_bio.panel.findViewById(R.id.profile_image));
            rep_bio.twitter_picture = imageClient.execute(rep_bio.twitter_picture_link).get();


            ((TextView) rep_bio.panel.findViewById(R.id.twit_text)).setText(rep_bio.last_twit);
            ((TextView) rep_bio.panel.findViewById(R.id.twit_time)).setText(rep_bio.last_twit_time);




        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void decodeJSONBills(String bills, RepBio rep_bio) {

        try {
            JSONObject bills0  = new JSONObject(bills);

//            Log.d("DEBUGTAG", twit.toString());
            JSONArray results = bills0.getJSONArray("results");
            int count = bills0.getInt("count");
            String billsString = "";

            for (int i = 0; i < count && i < 6; i++) {
                String temp = "[ " + results.getJSONObject(i).getString("introduced_on") + "]"
                        + results.getJSONObject(i).getString("official_title");

                rep_bio.bills.add(temp);
                billsString += "* ";
                billsString += temp;
                billsString += "\n";
            }

            ((TextView) rep_bio.panel.findViewById(R.id.list_of_bills)).setText(billsString);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void decodeJSONCommittees(String committees, RepBio rep_bio) {

        try {
            JSONObject committees0  = new JSONObject(committees);

//            Log.d("DEBUGTAG", twit.toString());
            JSONArray results = committees0.getJSONArray("results");
            int count = committees0.getInt("count");
            String billsString = "";

            for (int i = 0; i < count && i < 6; i++) {
                String temp = results.getJSONObject(i).getString("name");

                rep_bio.committees.add(temp);
                billsString += "* ";
                billsString += temp;
                billsString += "\n";
            }

            ((TextView) rep_bio.panel.findViewById(R.id.list_of_committees)).setText(billsString);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void decodeJSONaddress2(String addressJSON0, String voting_data) {

        try {
            JSONObject addressJSON = new JSONObject(addressJSON0);
            Log.d("DEBUGTAG", "try decoding json with parse object");
            JSONArray address_components = addressJSON.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONArray("address_components");
            int array_length = address_components.length();


            String county = "";
            String state = "";
            String zip = "";

            for (int i = 0; i < array_length; i++) {
                String temp = address_components.getJSONObject(i).
                        getJSONArray("types").
                        toString();
                if (temp.contains("\"administrative_area_level_2\"")) {
                    county = address_components
                            .getJSONObject(i).get("short_name").toString();
                } else if (temp.contains("\"administrative_area_level_1\"")) {
                    state = address_components
                            .getJSONObject(i).get("short_name").toString();
                } else if (temp.contains("\"postal_code\"")) {
                    zip = address_components
                            .getJSONObject(i).get("short_name").toString();
                }
            }

            JSONObject vote2012_JSON_object = null;
            String romney = "";
            String obama = "";




            try {
                vote2012_JSON_object = new JSONObject(voting_data);
                JSONObject result = vote2012_JSON_object.getJSONObject(county + ", " + state);
                romney = String.valueOf((int) (result.getDouble("romney")*10));
                obama = String.valueOf((int) (result.getDouble("obama")*10));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("DEBUGTAG", "romney=" + romney + " obama=" + obama + " " + county + ", " + state);
            ((Represent) this.getApplication()).setAddress(county + ", " + state + zip, zip);

            Toast.makeText(this, "romney=" + romney + " obama=" + obama + " " + county + ", " + state, Toast.LENGTH_LONG);
            sendBiosToWatch(county + ", " + state + " " + zip, romney, obama);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addPanel(RepBio rep_bio) {
        ViewGroup panel = (ViewGroup) inflater.inflate(R.layout.candidate_info, null);
        panels.addView(panel);
        ((TextView) panel.findViewById(R.id.rep_name)).setText(rep_bio.rep_name);
        ((TextView) panel.findViewById(R.id.rep_party)).setText(rep_bio.party);
        ((TextView) panel.findViewById(R.id.rep_email)).setText(rep_bio.oc_email);
        ((TextView) panel.findViewById(R.id.rep_website)).setText(rep_bio.website);
        ((TextView) panel.findViewById(R.id.house)).setText(capitalize(rep_bio.house));

        ((TextView) panel.findViewById(R.id.term_ends_date)).setText(rep_bio.term_ends_date + "\n");

        View back = panel.findViewById(R.id.background);
        rep_bio.panel = panel;

        if (rep_bio.party.equals("Republican")) {
            back.setBackgroundColor(getResources().getColor(R.color.republican));
        } else if (rep_bio.party.equals("Democratic")) {
            back.setBackgroundColor(getResources().getColor(R.color.democratic));
        } else {
            back.setBackgroundColor(getResources().getColor(R.color.independent));
        }
    }


    private String partyParser(String party0) {
        if (party0.equals("D")) {
            return "Democratic";
        } else if (party0.equals("R")) {
            return "Republican";
        } else {
            return "Independent";
        }
    }


    private void sendBiosToWatch(String location_string, String romney, String obama) {
        Log.d("DEBUGTAG", "sendBiosToWatch called");
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);

        String location_info = location_string + ";"
                + romney + ";"
                + obama + ";";

        for (int i = 0; i < num_candidates; i++) {
            location_info = location_info + rep_bios.get(i).rep_name + ";"
                    + rep_bios.get(i).party + ";";
        }
        location_info = location_info.substring(0, location_info.length() - 1);
        sendIntent.putExtra("LOCATIONINFO", location_info);
        startService(sendIntent);
    }


    private String getSampleLocation() {
        try {
            InputStream is = getAssets().open("samplelocation.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }


    class RepBio {
        public String rep_name;
        public String party;
        public String bioguide_id;
        public String oc_email;
        public String website;
        public String term_ends_date;
        public String house;

        public String twitter_id;
        public String last_twit;
        public String last_twit_time;
        public String twitter_picture_link;
        public Bitmap twitter_picture;

        public ArrayList<String> committees;
        public ArrayList<String> bills;

        public View panel;

        public RepBio(String bio_id) {
            bioguide_id = bio_id;
            committees = new ArrayList<String>();
            bills = new ArrayList<String>();
        }
    }
}
