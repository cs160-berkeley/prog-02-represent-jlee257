package com.example.aspire.election2016;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String CAND_A = "/CandA";
    private static final String CAND_B = "/CandB";
    private static final String CAND_C = "/CandC";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());

        if( messageEvent.getPath().equalsIgnoreCase( CAND_A ) ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, Candidates.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("CANDIDATE", "A");
            Log.d("T", "about to start watch CandidatesView with CANDIDATE: A");
            startActivity(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase( CAND_B )) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, Candidates.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("CANDIDATE", "B");
            Log.d("T", "about to start watch CandidatesView with CANDIDATE: B");
            startActivity(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase( CAND_C )) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, Candidates.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("CANDIDATE", "C");
            Log.d("T", "about to start watch CandidatesView with CANDIDATE: C");
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
