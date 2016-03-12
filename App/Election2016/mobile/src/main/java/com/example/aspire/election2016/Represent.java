package com.example.aspire.election2016;

import android.app.Application;

/**
 * Created by Aspire on 3/6/2016.
 */
public class Represent extends Application {

    public String zipCode0;
    public String locationString0;

    public void setAddress(String locationString, String zipCode) {
        zipCode0 = zipCode;
        locationString0 = locationString;
    }

    public String[] getAddress() {
        return new String[]{locationString0, zipCode0};
    }




}
