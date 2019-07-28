package ru.geekbrains.android.level2.valeryvpetrov;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainPresenter {

    private MainView view;

    private SensorManager sensorManager;
    private Sensor sensorOrientation;
    private Sensor sensorGyroscope;
    private Sensor sensorAccelerometer;

    private SensorEventListener sensorOrientationEvenListener;
    private SensorEventListener sensorGyroscopeEvenListener;
    private SensorEventListener sensorAccelerometerEvenListener;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void initSensors(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        initSensorsListeners();
    }

    public void registerSensorsListeners() {
        sensorManager.registerListener(sensorOrientationEvenListener,
                sensorOrientation,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorGyroscopeEvenListener,
                sensorGyroscope,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorAccelerometerEvenListener,
                sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensorsListeners() {
        sensorManager.unregisterListener(sensorOrientationEvenListener);
        sensorManager.unregisterListener(sensorGyroscopeEvenListener);
        sensorManager.unregisterListener(sensorAccelerometerEvenListener);
    }

    private void initSensorsListeners() {
        if (sensorOrientationEvenListener == null) {
            sensorOrientationEvenListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    // round value to 2 decimal points
                    float xAngle = (float) Math.round(sensorEvent.values[0] * 100) / 100;
                    float yAngle = (float) Math.round(sensorEvent.values[1] * 100) / 100;
                    float zAngle = (float) Math.round(sensorEvent.values[2] * 100) / 100;

                    view.updateOrientationSensorDataChanged(xAngle, yAngle, zAngle);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
        }
        if (sensorGyroscopeEvenListener == null) {
            sensorGyroscopeEvenListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    // round value to 2 decimal points
                    float xRotationRate = (float) Math.round(sensorEvent.values[0] * 100) / 100;
                    float yRotationRate = (float) Math.round(sensorEvent.values[1] * 100) / 100;
                    float zRotationRate = (float) Math.round(sensorEvent.values[2] * 100) / 100;

                    view.updateGyroSensorDataChanged(xRotationRate, yRotationRate, zRotationRate);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
        }
        if (sensorAccelerometerEvenListener == null) {
            sensorAccelerometerEvenListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    // round value to 2 decimal points
                    float xAcceleration = (float) Math.round(sensorEvent.values[0] * 100) / 100;
                    float yAcceleration = (float) Math.round(sensorEvent.values[1] * 100) / 100;
                    float zAcceleration = (float) Math.round(sensorEvent.values[2] * 100) / 100;

                    view.updateAccelerationSensorDataChanged(xAcceleration, yAcceleration, zAcceleration);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
        }
    }
}
