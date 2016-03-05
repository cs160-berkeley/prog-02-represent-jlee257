package com.example.aspire.election2016;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import java.nio.charset.StandardCharsets;


public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages
//    private static final String FRED_FEED = "/Fred";
//    private static final String LEXY_FEED = "/Lexy";

    private static final String CAND_A = "/CandA";
    private static final String CAND_B = "/CandB";
    private static final String CAND_C = "/CandC";


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        //use the 'path' field in sendmessage to differentiate use cases
        //(here, fred vs lexy)

        if( messageEvent.getPath().equalsIgnoreCase( CAND_A ) ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, CandidatesView.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("CANDIDATE", "A");
            Log.d("T", "about to start watch CandidatesView with CANDIDATE: A");
            startActivity(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase(CAND_B)) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, CandidatesView.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("CANDIDATE", "B");
            Log.d("T", "about to start watch CandidatesView with CANDIDATE: B");
            startActivity(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase( CAND_C )) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, CandidatesView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("CANDIDATE", "C");
            Log.d("T", "about to start watch CandidatesView with CANDIDATE: C");
            startActivity(intent);
        } else if (messageEvent.getPath().startsWith("/zip")) {
            int newzip = Integer.parseInt(messageEvent.getPath().substring(4));
            Log.d("T", "about to set location:" + messageEvent.getPath().substring(4));
            ((Election2016) this.getApplication()).setZip(newzip);
            Log.d("T", "locsetto:" + Integer.toString(newzip));
            Intent intent = new Intent(this, Menu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(this, "NEW AREA " + Integer.toString(newzip), Toast.LENGTH_LONG).show();
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
