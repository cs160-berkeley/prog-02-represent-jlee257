package com.example.aspire.election2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.util.Random;


public class PresidentialView extends Activity { // implements SensorListener

//    private SensorManager sensorMgr;
//    private long lastUpdate = -1;
//    private float x, y, z;
//    private float last_x, last_y, last_z;
//    private static final int SHAKE_THRESHOLD = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presidential_view);
        final RelativeLayout stub = (RelativeLayout) findViewById(R.id.relative);

        TextView locText = (TextView) findViewById(R.id.textView10);
        Log.d("T", "about to set location:" + Integer.toString((((Election2016) this.getApplication()).getZip())));
        locText.setText(Integer.toString((((Election2016) this.getApplication()).getZip())));


//        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
//        boolean accelSupported = sensorMgr.registerListener(this,
//                SensorManager.SENSOR_ACCELEROMETER,
//                SensorManager.SENSOR_DELAY_GAME);
//
//        if (!accelSupported) {
//            // on accelerometer on this device
//            sensorMgr.unregisterListener(this,
//                    SensorManager.SENSOR_ACCELEROMETER);
//        }

    }

//    protected void onPause() {
//        if (sensorMgr != null) {
//            sensorMgr.unregisterListener(this,
//                    SensorManager.SENSOR_ACCELEROMETER);
//            sensorMgr = null;
//        }
//        super.onPause();
//    }
//
//    @Override
//    public void onSensorChanged(int sensor, float[] values) {
//        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
//            long curTime = System.currentTimeMillis();
//            // only allow one update every 100ms.
//            if ((curTime - lastUpdate) > 100) {
//                long diffTime = (curTime - lastUpdate);
//                lastUpdate = curTime;
//                x = values[SensorManager.DATA_X];
//                y = values[SensorManager.DATA_Y];
//                z = values[SensorManager.DATA_Z];
//
//                float speed = Math.abs(x+y+z - last_x - last_y - last_z)
//                        / diffTime * 10000;
//                if (speed > SHAKE_THRESHOLD) {
//                    // yes, this is a shake action! Do something about it!
//                    TextView locText = (TextView) findViewById(R.id.textView10);
//                    Random rand = new Random();
//                    String z = Integer.toString(10000 + rand.nextInt(90000));
//                    locText.setText(z);
//                    Toast.makeText(this, "NEW RANDOM AREA" + z, Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(this, Menu.class);
//                    startActivity(i);
//                }
//                last_x = x;
//                last_y = y;
//                last_z = z;
//            }
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(int sensor, int accuracy) {
//
//    }
}
