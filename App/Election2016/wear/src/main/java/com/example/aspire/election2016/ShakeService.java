package com.example.aspire.election2016;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.WearableListenerService;

import java.util.Random;


/**
 * Created by Aspire on 3/4/2016.
 */
public class ShakeService extends WearableListenerService implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private Context context;
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


//      registering the listener
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
        Log.d("T", "sensor created");
        context = ShakeService.this.getApplicationContext();

        // TODO Auto-generated method stub;
    }


    @Override
    public void onDestroy() {

        // unregistering the listener
        sensorManager.unregisterListener((SensorEventListener) this, sensor);
        super.onDestroy();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("T", "sensored");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                float speed = Math.abs(x+y+z - last_x - last_y - last_z)
                        / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    // yes, this is a shake action! Do something about it!
                    Random rand = new Random();
                    int newzip = 10000 + rand.nextInt(90000);
//                    ((Election2016) this.getApplication()).setZip(randint);
//                    Log.d("T", "locsetto:" + Integer.toString((((Election2016) this.getApplication()).getZip())));
//                    Intent intent = new Intent(this, Menu.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    Context context = getApplicationContext();
//                    Toast.makeText(context, "NEW RANDOM AREA " + Integer.toString(randint), Toast.LENGTH_LONG).show();

                    Log.d("T", "locsetto:" + Integer.toString(newzip));

                    Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                    sendIntent.putExtra("RANDOMLOCATION", "RANDOMLOCATION");
                    startService(sendIntent);
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
