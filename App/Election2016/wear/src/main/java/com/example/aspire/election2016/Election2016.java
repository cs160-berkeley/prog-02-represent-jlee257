package com.example.aspire.election2016;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Aspire on 3/4/2016.
 */
public class Election2016 extends Application {
    private String[] locationInfo = {"00000", "645", "334", "Ronald Johnson", "Democratic"};
    private int numRep = 1;


    // ((Election2016) this.getApplication()).getZip()
    public String getZip() {
        return locationInfo[0];
    }

    public int getRomneyVote() {
        return Integer.parseInt(locationInfo[1]);
    }

    public int getObamaVote() {
        return Integer.parseInt(locationInfo[2]);
    }

    public String getCandName(int index) {
        return locationInfo[index * 2 + 3];
    }

    public String getCandParty(int index) {
        return locationInfo[index * 2 + 4];
    }

    public String[] getLocInfo() {
        return locationInfo;
    }

    public int getNumRep() {
        return numRep;
    }

    public Boolean setLocationInfo(String info) {
        String[] temp_info = info.split(";");
        if (temp_info.length > 2 && temp_info.length % 2 == 1) {
            locationInfo = temp_info;
            numRep = (temp_info.length - 3) % 2;
            Log.d("DEBUGTAG", "Election2016:setLocation " + locationInfo[0]);
            Toast.makeText(this, "NEW AREA " + locationInfo[0], Toast.LENGTH_LONG).show();
            return true;
        } else {
            Log.d("DEBUGTAG", "Election2016:setLocation failed");
            return false;
        }

    }
}
