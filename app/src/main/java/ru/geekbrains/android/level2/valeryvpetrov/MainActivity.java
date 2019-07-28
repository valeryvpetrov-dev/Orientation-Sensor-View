package ru.geekbrains.android.level2.valeryvpetrov;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainView {

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        presenter.initSensors(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.registerSensorsListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unregisterSensorsListeners();
    }

    @Override
    public void updateOrientationSensorDataChanged(float xAngle, float yAngle, float zAngle) {
        ((TextView) findViewById(R.id.orientation_x_axis)).setText(String.valueOf(xAngle));
        ((TextView) findViewById(R.id.orientation_y_axis)).setText(String.valueOf(yAngle));
        ((TextView) findViewById(R.id.orientation_z_axis)).setText(String.valueOf(zAngle));
    }

    @Override
    public void updateGyroSensorDataChanged(float xRotationRate, float yRotationRate, float zRotationRate) {
        ((TextView) findViewById(R.id.gyro_x_axis)).setText(String.valueOf(xRotationRate));
        ((TextView) findViewById(R.id.gyro_y_axis)).setText(String.valueOf(yRotationRate));
        ((TextView) findViewById(R.id.gyro_z_axis)).setText(String.valueOf(zRotationRate));
    }

    @Override
    public void updateAccelerationSensorDataChanged(float xAcceleration, float yAcceleration, float zAcceleration) {
        ((TextView) findViewById(R.id.acceleration_x_axis)).setText(String.valueOf(xAcceleration));
        ((TextView) findViewById(R.id.acceleration_y_axis)).setText(String.valueOf(yAcceleration));
        ((TextView) findViewById(R.id.acceleration_z_axis)).setText(String.valueOf(zAcceleration));
    }
}
