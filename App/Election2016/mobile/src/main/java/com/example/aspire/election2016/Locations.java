package com.example.aspire.election2016;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;


public class Locations extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener, MapFragment.OnFragmentInteractionListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
//    private String auth_key = "AIzaSyD9wn0uOIxLQg9jVbuEAP7uAar8myvYH_o";
    private String auth_key = "AIzaSyCQvutc1xrmbVWQ759edbD8q8nGaRHI1WU";
    private boolean use_sample = false;
    private LocationRequest mLocationRequest;

    private String locationString;
    private String zipCode;

    private TextView current_location;

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private MapFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //map fragment
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        fragment = new MapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();




        //fetch global address
        String[] locInfo = ((Represent) this.getApplication()).getAddress();
        locationString = locInfo[0];
        zipCode = locInfo[1];
        current_location = (TextView) findViewById(R.id.current_location);
        if (locationString == null || locationString.equals("")) {
            current_location.setText("Unable to find location");
            Toast.makeText(this, "Unable to find location", Toast.LENGTH_LONG);
        } else {
            current_location.setText(locationString);
        }


        //Google Play Location Service
        if (mGoogleApiClient == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }


        //Location Update
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 15 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 3 second, in milliseconds



        //Set "useCurrentLocation" Button
        final Activity thisActivity = this;
        Button useCurrentLocation = (Button) findViewById(R.id.button2);
        useCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
//                sendIntent.putExtra("CANDIDATE", "zip12345");
//                startService(sendIntent);
                Intent i = new Intent(Locations.this, Candidates.class);
                i.putExtra("LAT", mLastLocation.getLatitude());
                i.putExtra("LONG", mLastLocation.getLongitude());
                i.putExtra("ZIPCODE", zipCode);
                ((Represent) thisActivity.getApplication()).setAddress(locationString, zipCode);
                startActivity(i);
            }
        });




        Button useZipcode = (Button) findViewById(R.id.button3);
        useZipcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
//                sendIntent.putExtra("CANDIDATE", "zip" + editText.getText().toString());
//                startService(sendIntent);

                Intent i = new Intent(Locations.this, Candidates.class);
                EditText editText = (EditText) ((ViewGroup) view.getParent()).findViewById(R.id.editText);
                i.putExtra("ZIPCODE", editText.getText().toString());
                String[] l = null;
                try {
                    l = decodeJSONaddress2(getLocation(editText.getText().toString()));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(i);
            }
        });
    }



//    //Set "useZipcode" Button
//    public void use_zipcode(View view) throws ExecutionException, InterruptedException {
//
////                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
////                sendIntent.putExtra("CANDIDATE", "zip" + editText.getText().toString());
////                startService(sendIntent);
//
//        Intent i = new Intent(Locations.this, Candidates.class);
//        EditText editText = (EditText) ((ViewGroup) view.getParent()).findViewById(R.id.editText);
//        i.putExtra("ZIPCODE", editText.getText().toString());
//        String[] l = decodeJSONaddress2(getLocation(editText.getText().toString()));
//        ((Represent) this.getApplication()).setAddress(l[0], l[1]);
//        startActivity(i);
//    }





    @Override
    protected void onStart() {
        Log.d("DEBUGTAG", "Locations: onStart");
        mGoogleApiClient.connect();
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Locations Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.aspire.election2016/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }


    @Override
    protected void onStop() {
        Log.d("DEBUGTAG", "Locations: onStop");
        mGoogleApiClient.disconnect();
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Locations Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.aspire.election2016/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
    }


    @Override
    protected void onResume() {
        Log.d("DEBUGTAG", "Locations: onResume");
        super.onResume();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    protected void onPause() {
        Log.d("DEBUGTAG", "Locations: onPause");
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }




    private void decodeJSONaddress(String addressJSON0) {

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
            if (zip.equals(zipCode)) {
                Log.d("DEBUGTAG", "location did not change: " + zip);
            } else if (zip != null && !zip.equals("")) {

                //update activity address
                locationString = county + ", " + state;
                zipCode = zip;

                //update activity
                Toast.makeText(this, "NEW LOCATION: " + zipCode, Toast.LENGTH_LONG).show();
                current_location.setText(locationString);

                //update map
                fragment.updatePosition(mLastLocation.getLatitude(), mLastLocation.getLongitude());

//                // create marker
//                MarkerOptions marker = new MarkerOptions().position(
//                        new LatLng(latitude, longitude)).title("Hello Maps");
//
//                // Changing marker icon
//                marker.icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
//
//                // adding marker
//                googleMap.addMarker(marker);
//                CameraPosition cameraPosition = new CameraPosition.Builder()
//                        .target(new LatLng(17.385044, 78.486671)).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory
//                        .newCameraPosition(cameraPosition));

                Log.d("DEBUGTAG", "location changed: " + zip);
            } else {
                zipCode = null;
            }


            Log.d("DEBUGTAG", "finally got address: " + county + ", " + state + " " + zip);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private String[] decodeJSONaddress2(String addressJSON0) {

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


            Log.d("DEBUGTAG", "finally got address: " + county + ", " + state);

            ((Represent) this.getApplication()).setAddress(county + ", " + state, zip);
            return new String[]{county + ", " + state, zip};

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }



    public String getLocation(Location loc) throws ExecutionException, InterruptedException {

        if (use_sample) {
            return getSampleLocation();
        }

        Log.d("DEBUGTAG", String.valueOf(loc.getLatitude()) + "; " + String.valueOf(loc.getLongitude()));

        RequestClient client = new RequestClient();
        return client.execute("https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + String.valueOf(loc.getLatitude()) + ","
                + String.valueOf(loc.getLongitude()) + "&key=" + auth_key).get();

    }



    public String getLocation(String zip) throws ExecutionException, InterruptedException {

        if (use_sample) {
            return getSampleLocation();
        }

        Log.d("DEBUGTAG", "getLocation: " + zip);
        RequestClient client = new RequestClient();
        return client.execute("https://maps.googleapis.com/maps/api/geocode/json?address="
                + zip + ",united+states&key=" + auth_key).get();
    }





    @Override
    public void onConnected(Bundle onCunnectionhit) {
        Log.d("DEBUGTAG", "start onConnected");

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.d("DEBUGTAG", "ACCESS_COARSE_LOCATION no requires permission");
        } else {
            Log.d("DEBUGTAG", "ACCESS_COARSE_LOCATION requires permission");
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        if (mLastLocation == null) {
            Log.d("DEBUGTAG", "Locations: mLastLocation is null! requesting update");
        } else {
            Log.d("DEBUGTAG", "Locations: mLastLocation is not null");
            try {
                decodeJSONaddress(getLocation(mLastLocation));
                Log.d("DEBUGTAG", "Locations: onConnected successful");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onConnectionSuspended(int i) {
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("DEBUGTAG", "start onLocationChanged");
        mLastLocation = location;
        try {
            decodeJSONaddress(getLocation(location));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
}
