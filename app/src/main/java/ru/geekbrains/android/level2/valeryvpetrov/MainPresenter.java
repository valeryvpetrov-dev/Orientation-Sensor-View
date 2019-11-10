package ru.geekbrains.android.level2.valeryvpetrov;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    private SensorManager sensorManager;
    private Sensor sensorOrientation;
    private Sensor sensorGyroscope;
    private Sensor sensorAccelerometer;

    private SensorEventListener sensorOrientationEvenListener;
    private SensorEventListener sensorGyroscopeEvenListener;
    private SensorEventListener sensorAccelerometerEvenListener;

    public MainPresenter(MainContract.View view, SensorManager sensorManager) {
        this.view = view;
        this.sensorManager = sensorManager;
        initSensors();
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

    private void initSensors() {
        sensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        initSensorsListeners();
    }

    private void initSensorsListeners() {
        if (sensorOrientationEvenListener == null) {
            sensorOrientationEvenListener = new SensorEventListener() {
                /**
                 * Sensor.TYPE_ORIENTATION event description:
                 * https://developer.android.com/reference/android/hardware/SensorEvent#sensor.type_orientation-:
                 *
                 * @param sensorEvent: Sensor.TYPE_ORIENTATION event
                 */
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    // Azimuth, angle between the magnetic north direction and the y-axis,
                    // around the z-axis (0 to 359). 0=North, 90=East, 180=South, 270=West
                    float zAngle = roundFloat(sensorEvent.values[0]);
                    // Pitch, rotation around x-axis (-180 to 180), with positive values
                    // when the z-axis moves toward the y-axis.
                    float xAngle = roundFloat(sensorEvent.values[1]);
                    // Roll, rotation around the y-axis (-90 to 90)
                    // increasing as the device moves clockwise.
                    float yAngle = roundFloat(sensorEvent.values[2]);

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
                    float xRotationRate = roundFloat(sensorEvent.values[0]);
                    float yRotationRate = roundFloat(sensorEvent.values[1]);
                    float zRotationRate = roundFloat(sensorEvent.values[2]);

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
                    float xAcceleration = roundFloat(sensorEvent.values[0]);
                    float yAcceleration = roundFloat(sensorEvent.values[1]);
                    float zAcceleration = roundFloat(sensorEvent.values[2]);

                    view.updateAccelerationSensorDataChanged(xAcceleration, yAcceleration, zAcceleration);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
        }
    }

    /**
     * Rounds value to 2 decimal points
     *
     * @param value: float to round
     * @return float rounded to 2 decimal points
     */
    private float roundFloat(float value) {
        return (float) Math.round(value * 100) / 100;
    }
}
