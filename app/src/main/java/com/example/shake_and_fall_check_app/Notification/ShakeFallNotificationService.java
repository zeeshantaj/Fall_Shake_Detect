package com.example.shake_and_fall_check_app.Notification;

import static com.example.shake_and_fall_check_app.Shake_and_Fall_Activity.FALL_THRESHOLD;
import static com.example.shake_and_fall_check_app.Shake_and_Fall_Activity.SHAKE_SLOP_TIME_MS;
import static com.example.shake_and_fall_check_app.Shake_and_Fall_Activity.SHAKE_THRESHOLD;
import static com.example.shake_and_fall_check_app.Shake_and_Fall_Activity.lastShakeTime;
import static com.example.shake_and_fall_check_app.Shake_and_Fall_Activity.lastX;
import static com.example.shake_and_fall_check_app.Shake_and_Fall_Activity.lastY;
import static com.example.shake_and_fall_check_app.Shake_and_Fall_Activity.lastZ;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.shake_and_fall_check_app.R;

public class ShakeFallNotificationService extends Service implements SensorEventListener {
    private static final int NOTIFICATION_ID = 1;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(NOTIFICATION_ID, new Notification());
//        }

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     //   sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

            if (isShake(acceleration, x, y, z)) {
                if (acceleration <= 60) {
                    //handleShakeDetected();
                    showNotification("Shake ","shaking detected "+acceleration);
                    Log.e("MyApp", "shake acceleration " + acceleration);
                }

            } else if (acceleration > FALL_THRESHOLD) {
                //handleFallDetected();
                showNotification("Fall ","Fall detected "+acceleration);
                Log.e("MyApp", "fall acceleration " + acceleration);
            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    private boolean isShake(float acceleration, float x, float y, float z) {
        // Implement your shake detection logic here
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
    private void showNotification(String title, String message) {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id",
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.cancel(NOTIFICATION_ID);

        startForeground(NOTIFICATION_ID, builder.build());

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
