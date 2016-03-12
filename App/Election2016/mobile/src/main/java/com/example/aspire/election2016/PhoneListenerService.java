package com.example.aspire.election2016;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    private LocalBroadcastManager broadcaster;


    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);

    }



    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String REPSELECT = "/REPSELECT";
    private static final String RANDOMLOCATION = "/RANDOMLOCATION";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("DEBUGTAG", "in PhoneListenerService, got: " + messageEvent.getPath());

        if( messageEvent.getPath().equalsIgnoreCase( REPSELECT ) ) {

            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
//            Intent intent = new Intent(this, Candidates.class );
            Intent intent = new Intent("REPSELECT");
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("REPSELECT", value);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("DEBUGTAG", "about to start watch CandidatesView with REPSELECT:" + value);
//            startActivity(intent);
            broadcaster.sendBroadcast(intent);

        } else if (messageEvent.getPath().equalsIgnoreCase( RANDOMLOCATION )) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            String random_zip = "99950";
            try {
                InputStream is = getAssets().open("listofzips.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                JSONArray zips = new JSONArray(new String(buffer, "UTF-8"));
                int random_integer = (int)(Math.random() * zips.length());
                random_zip = zips.getString(random_integer);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, Candidates.class);
            intent.putExtra("ZIPCODE", random_zip);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Log.d("DEBUGTAG", "about to start watch CandidatesView with random zipcode");

        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
