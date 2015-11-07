package fr.enac.lostmyandroid.utilities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import fr.enac.lostmyandroid.views.AlertAlarmActivity;

/**
 * Created by Amine on 01/11/2015.
 */
public class VibrationSensor implements SensorEventListener {

    boolean alarmActivated = false;

    Context myContext;
    private float mAccel = 0.00f; // acceleration apart from gravity
    private float mAccelCurrent = SensorManager.GRAVITY_EARTH;
    ; // current acceleration including gravity
    private float mAccelLast = SensorManager.GRAVITY_EARTH; // last acceleration including gravity

    public VibrationSensor(Context context) {
        myContext = context;
    }

    public void onSensorChanged(SensorEvent se) {

        if (!alarmActivated) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > 1) {
                alarmActivated = true;
                Intent intent = new Intent(myContext, AlertAlarmActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myContext.startActivity(intent);
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public boolean activateAlarm() {
        return true;
    }
}
