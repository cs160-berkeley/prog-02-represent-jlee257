package com.example.aspire.election2016;

import android.app.Application;
import android.widget.Toast;

/**
 * Created by Aspire on 3/4/2016.
 */
public class Election2016 extends Application {
    private int zipcode;


    public int getZip() {
        return zipcode;
    }

    public void setZip(int zip) {
        zipcode = zip;
    }
}
