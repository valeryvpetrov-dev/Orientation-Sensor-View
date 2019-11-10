package ru.geekbrains.android.level2.valeryvpetrov;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.orientation_x_axis) TextView textOrientationXAxis;
    @BindView(R.id.orientation_y_axis) TextView textOrientationYAxis;
    @BindView(R.id.orientation_z_axis) TextView textOrientationZAxis;
    @BindView(R.id.gyro_x_axis) TextView textGyroXAxis;
    @BindView(R.id.gyro_y_axis) TextView textGyroYAxis;
    @BindView(R.id.gyro_z_axis) TextView textGyroZAxis;
    @BindView(R.id.acceleration_x_axis) TextView textAccelerationXAxis;
    @BindView(R.id.acceleration_y_axis) TextView textAccelerationYAxis;
    @BindView(R.id.acceleration_z_axis) TextView textAccelerationZAxis;

    @BindView(R.id.view_orientation) CoordinateFrameView coordinateFrameView;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
        textOrientationXAxis.setText(String.valueOf(xAngle));
        textOrientationYAxis.setText(String.valueOf(yAngle));
        textOrientationZAxis.setText(String.valueOf(zAngle));

        coordinateFrameView.setAngleXAxis(xAngle);
    }

    @Override
    public void updateGyroSensorDataChanged(float xRotationRate, float yRotationRate, float zRotationRate) {
        textGyroXAxis.setText(String.valueOf(xRotationRate));
        textGyroYAxis.setText(String.valueOf(yRotationRate));
        textGyroZAxis.setText(String.valueOf(zRotationRate));
    }

    @Override
    public void updateAccelerationSensorDataChanged(float xAcceleration, float yAcceleration, float zAcceleration) {
        textAccelerationXAxis.setText(String.valueOf(xAcceleration));
        textAccelerationYAxis.setText(String.valueOf(yAcceleration));
        textAccelerationZAxis.setText(String.valueOf(zAcceleration));
    }
}
