package com.example.shake_and_fall_check_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.materialswitch.MaterialSwitch;

public class Shake_and_Fall_Activity extends AppCompatActivity {
    private static final float FALL_THRESHOLD = 40.8f * 2; // 2g threshold for fall detection
    private static final int SHAKE_SLOP_TIME_MS = 500; // minimum time between two shake events
    private static final float SHAKE_THRESHOLD = 29; // acceleration threshold for shake detection

    private long lastShakeTime = 0;
    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private MaterialSwitch detectSwitchBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_and_fall);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        detectSwitchBtn = findViewById(R.id.detectSwitch);
        detectSwitchBtn.setChecked(false);
        unregisterSensorListener();

        detectSwitchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "Active", Toast.LENGTH_SHORT).show();
                registerSensorListener();
            } else {
                Toast.makeText(this, "InActive", Toast.LENGTH_SHORT).show();
                unregisterSensorListener();
            }
        });
    }


    private void registerSensorListener() {
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unregisterSensorListener() {
        sensorManager.unregisterListener(sensorEventListener);
    }
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

                if (isShake(acceleration, x, y, z)) {
                    if (acceleration <= 60) {
                        handleShakeDetected();
                        Log.e("MyApp", "shake acceleration " + acceleration);
                    }

                } else if (acceleration > FALL_THRESHOLD) {
                    handleFallDetected();
                    Log.e("MyApp", "fall acceleration " + acceleration);
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not used, but required to implement SensorEventListener
        }

        private boolean isShake(float acceleration, float x, float y, float z) {
            long now = System.currentTimeMillis();
            if (acceleration > SHAKE_THRESHOLD && now - lastShakeTime > SHAKE_SLOP_TIME_MS) {
                float deltaX = Math.abs(x - lastX);
                float deltaY = Math.abs(y - lastY);
                float deltaZ = Math.abs(z - lastZ);

                if ((deltaX > 5 && deltaY > 5) || (deltaX > 5 && deltaZ > 5) || (deltaY > 5 && deltaZ > 5)) {
                    if (acceleration >= 41) {
                        lastShakeTime = now;
                        return true;
                    }
                }
            }
            return false;
        }
    };

    private void handleFallDetected() {
        // Notify that a fall has been detected
        Toast.makeText(this, "Fall detected!", Toast.LENGTH_SHORT).show();
    }

    private void handleShakeDetected() {
        // Notify that a shake has been detected
        Toast.makeText(this, "Shake detected!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }
}