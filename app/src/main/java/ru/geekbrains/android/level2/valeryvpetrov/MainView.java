package ru.geekbrains.android.level2.valeryvpetrov;

public interface MainView {
    void updateOrientationSensorDataChanged(float xAngle,
                                            float yAngle,
                                            float zAngle);
    void updateGyroSensorDataChanged(float xRotationRate,
                                     float yRotationRate,
                                     float zRotationRate);
    void updateAccelerationSensorDataChanged(float xAcceleration,
                                             float yAcceleration,
                                             float zAcceleration);
}
