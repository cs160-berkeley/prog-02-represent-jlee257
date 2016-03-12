package com.example.aspire.election2016;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Aspire on 3/7/2016.
 */
public class RequestClient extends AsyncTask<String, String, String> {

    private static final String USER_AGENT = "OAuthTwitterClient app-only auth";
    final static String twitter_consumer_key = "GhNobwpSYjHLVh6BuHG7Qz658";
    final static String twitter_auth_token = "707994985115705344-PCIbHLJFUgf2EwRSBC9byIwbkJRKCL4";
    final static String twitter_consumer_secret = "tKK8SeJLPC8T6ylmaI2sl8TuJwBKctG9KgONq88mSaqjCf99Sq&h4yg4Cfp2bE1FjHqzMlZ23OjpzKWZwowmLAg1JKemHzt4";



    @Override
    protected String doInBackground(String... params) {
        URL url;
        HttpURLConnection urlConnection = null;
        JSONObject response = new JSONObject();
        String responseString = null;

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

//            if (params[0].contains("oauth2")) {
//                Log.v("CatalogClient", "header oauth2 = " + encodeTwitterKeys());
//                Log.v("CatalogClient", "url = " + params[0]);
//
//                urlConnection.setRequestProperty("Authorization", "Basic " + encodeTwitterKeys());
//                urlConnection.setDoOutput(true);
//                urlConnection.setDoInput(true);
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Host", "api.twitter.com");
//                urlConnection.setRequestProperty("User-Agent", USER_AGENT);
//                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            if (params[0].contains("twitter")) {

                String header2 = twitterHelp(params[1]);
                Log.v("CatalogClient", "header = " + header2);

                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                urlConnection.setRequestProperty("Authorization", header2);
                urlConnection.setRequestProperty("User-Agent", "XXXX");
            }


            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                responseString = readStream(urlConnection.getInputStream());

                Log.v("CatalogClient", responseString);

//                response = new JSONObject(responseString);
            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return responseString;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }




    private String twitterHelp(String username) {
        try {
            /**
             * get the time - note: value below zero
             * the millisecond value is used for oauth_nonce later on
             */
            long millis = System.currentTimeMillis();
//                    currentTimeMillis();
            int time = (int) (millis / 1000);

            /**
             * Listing of all parameters necessary to retrieve a token
             * (sorted lexicographically as demanded)
             */
            String[][] data = {
                    {"oauth_consumer_key", twitter_consumer_key},
                    {"oauth_nonce",  String.valueOf(millis)},
//                    {"oauth_nonce",  "e77f270917961bf724cc787dd1d067bf"},
                    {"oauth_signature", ""},
                    {"oauth_signature_method", "HMAC-SHA1"},
//                    {"oauth_timestamp", "1457670155"},
                    {"oauth_timestamp", String.valueOf(time)},
                    {"oauth_token", twitter_auth_token},
                    {"oauth_version", "1.0"}
            };

            /**
             * Generation of the signature base string
             */
            String signature_base_string =
                    "GET&"+URLEncoder.encode("https://api.twitter.com/1.1/statuses/user_timeline.json", "UTF-8")+"&count%3D1%26include_rts%3Dtrue%26";
            for(int i = 0; i < data.length; i++) {
                // ignore the empty oauth_signature field
                if(i != 2) {
                    signature_base_string +=
                            URLEncoder.encode(data[i][0], "UTF-8") + "%3D" +
                                    URLEncoder.encode(data[i][1], "UTF-8") + "%26";
                }
            }
            // cut the last appended %26
//            signature_base_string = signature_base_string.substring(0,
//                    signature_base_string.length()-3);
            signature_base_string = signature_base_string + "screen_name%3D" + username;

            Log.v("CatalogClient", "base =  " + signature_base_string);


            String base2 =
                    "GET&https%3A%2F%2Fapi.twitter.com%2F1.1%2Fstatuses%2Fuser_timeline.json&count%3D2%26oauth_consumer_key%3DGhNobwpSYjHLVh6BuHG7Qz658%26oauth_nonce%3Decefa5fa7b226cf46194eba096b2398a%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1457665368%26oauth_token%3D707994985115705344-PCIbHLJFUgf2EwRSBC9byIwbkJRKCL4%26oauth_version%3D1.0%26screen_name%3DRepFrenchHill";
            String base = URLEncoder.encode(base2, "UTF-8");
            /**
             * Sign the request
             */

//            m.update(signature_base_string.getBytes());
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret = new SecretKeySpec(twitter_consumer_secret.getBytes(), mac.getAlgorithm());
            mac.init(secret);
            byte[] digest = mac.doFinal(signature_base_string.getBytes());


            String sig5 = Base64.encodeToString(digest, Base64.NO_WRAP);
            Log.v("CatalogClient", "oathsign " + sig5);
            data[2][1] = URLEncoder.encode(sig5, "UTF-8");
            Log.v("CatalogClient", "correct =  ArwjoHzNCLJQoYPcOU3XIL5nQXg%3D");
            /**
             * Create the header for the request
             */
            String header = "OAuth ";
            for(String[] item : data) {
                header += item[0]+"=\""+item[1]+"\", ";
            }
            // cut off last appended comma
            header = header.substring(0, header.length()-2);

            return header;
//            System.out.println("Signature Base String: "+signature_base_string);
//            System.out.println("Authorization Header: "+header);
//            System.out.println("Signature: "+sig);
//
//            HttpURLConnection connection = new URL(url).openConnection();
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//            connection.setRequestProperty("Accept-Charset", "UTF-8");
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
//            connection.setRequestProperty("Authorization", header);
//            connection.setRequestProperty("User-Agent", "XXXX");
//            OutputStream output = connection.getOutputStream();
//            output.write(header.getBytes(charset));
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//            String read;
//            while((read = reader.readLine()) != null) {
//                buffer.append(read);

        }
        catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }



    private String encodeTwitterKeys() {
        try {
            String encodedConsumerKey = URLEncoder.encode(twitter_consumer_key, "UTF-8");
            String encodedConsumerSecret = URLEncoder.encode(twitter_consumer_secret, "UTF-8");

            String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;

            return Base64.encodeToString(fullKey.getBytes(), Base64.NO_WRAP);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new String();
        }
    }
}