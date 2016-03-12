package com.example.aspire.election2016;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Aspire on 3/10/2016.
 */
public class DownloadImageClient extends AsyncTask<String, Void, Bitmap> {

    private ImageView bmImage;

    public DownloadImageClient(ImageView bmImage) {
        this.bmImage = bmImage;
    }


    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        //set image of your imageview
        bmImage.setImageBitmap(result);
    }
}