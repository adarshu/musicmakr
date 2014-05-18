package io.musicmakr;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by rayho on 5/17/14.
 */
public class Gestures {
    private static Gestures instance;
    public Gestures getInstance() {
        if (instance == null) {
            instance = new Gestures();
        }
        return instance;
    }

    private SensorManager sensorManager;
    private Sensor sensor;

    private Gestures() {
        sensorManager = (SensorManager) MyApplication.getInstance()
                .getSystemService(Context.SENSOR_SERVICE);
    }

    public void start() {
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
}
