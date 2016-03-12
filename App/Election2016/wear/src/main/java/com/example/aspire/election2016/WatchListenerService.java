package com.example.aspire.election2016;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import java.nio.charset.StandardCharsets;




//adb -s 192.168.56.101:5555 -d forward tcp:5601 tcp:5601




public class WatchListenerService extends WearableListenerService {


    private static final String CAND_A = "/CandA";


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("DEBUGTAG", "in WatchListenerService, got: " + messageEvent.getPath());


        if (messageEvent.getPath().equals("/rep_select")) {

            int value = Integer.parseInt(new String(messageEvent.getData(), StandardCharsets.UTF_8));

            Intent intent = new Intent(this, CandidatesView.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("REPSELECT", value);
            Log.d("T", "about to start watch CandidatesView with CANDIDATE: " + value);

            startActivity(intent);

        } else if (messageEvent.getPath().equals("/LOCATIONINFO")) {



            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d("DEBUGTAG", "data=" + value);

            Intent intent = new Intent(this, Menu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("LOCATIONINFO", value);
            startActivity(intent);

        } else {
            super.onMessageReceived(messageEvent);

        }






//        if( messageEvent.getPath().equalsIgnoreCase( CAND_A ) ) {
//            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
//            Intent intent = new Intent(this, CandidatesView.class );
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //you need to add this flag since you're starting a new activity from a service
//            intent.putExtra("CANDIDATE", "A");
//            Log.d("T", "about to start watch CandidatesView with CANDIDATE: A");
//            startActivity(intent);
//        } else if (messageEvent.getPath().equalsIgnoreCase(CAND_B)) {
//            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
//            Intent intent = new Intent(this, CandidatesView.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //you need to add this flag since you're starting a new activity from a service
//            intent.putExtra("CANDIDATE", "B");
//            Log.d("T", "about to start watch CandidatesView with CANDIDATE: B");
//            startActivity(intent);
//        } else if (messageEvent.getPath().equalsIgnoreCase( CAND_C )) {
//            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
//            Intent intent = new Intent(this, CandidatesView.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //you need to add this flag since you're starting a new activity from a service
//            intent.putExtra("CANDIDATE", "C");
//            Log.d("T", "about to start watch CandidatesView with CANDIDATE: C");
//            startActivity(intent);
//        } else if (messageEvent.getPath().startsWith("/zip")) {
//            int newzip = Integer.parseInt(messageEvent.getPath().substring(4));
//            Log.d("T", "about to set location:" + messageEvent.getPath().substring(4));
//            ((Election2016) this.getApplication()).setZip(newzip);
//            Log.d("T", "locsetto:" + Integer.toString(newzip));
//            Intent intent = new Intent(this, Menu.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            Toast.makeText(this, "NEW AREA " + Integer.toString(newzip), Toast.LENGTH_LONG).show();
//        } else {
//            super.onMessageReceived( messageEvent );
//        }

    }

}
