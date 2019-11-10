package ru.geekbrains.android.level2.valeryvpetrov;

public interface MainContract {

    interface View {

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

    interface Presenter {

        void registerSensorsListeners();

        void unregisterSensorsListeners();
    }
}
