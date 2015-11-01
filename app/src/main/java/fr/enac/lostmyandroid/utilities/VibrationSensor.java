package fr.enac.lostmyandroid.utilities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import fr.enac.lostmyandroid.view.AlertAlarmActivity;

/**
 * Created by Amine on 01/11/2015.
 */
public class VibrationSensor implements SensorEventListener {
    private SensorManager mySensorManager;

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean activateAlarm() {
        return true;
    }
}
