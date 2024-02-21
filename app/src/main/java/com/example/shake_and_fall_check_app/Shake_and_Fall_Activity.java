package com.example.shake_and_fall_check_app;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shake_and_fall_check_app.DetailsActivity.Details_Activity;
import com.example.shake_and_fall_check_app.Notification.ShakeFallNotificationService;
import com.google.android.material.materialswitch.MaterialSwitch;

public class Shake_and_Fall_Activity extends AppCompatActivity {


    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private MaterialSwitch detectSwitchBtn;
    private SharedPreferences sharedPreferences;
    private LottieAnimationView animationView;
    private LinearLayout shakeHistoryBtn, fallHistoryBtn;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_and_fall);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        detectSwitchBtn = findViewById(R.id.detectSwitch);
        textView = findViewById(R.id.textView);
        shakeHistoryBtn = findViewById(R.id.shakeHistory);
        fallHistoryBtn = findViewById(R.id.fallHistory);

         animationView = findViewById(R.id.animation_view);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        detectSwitchBtn.setChecked(sharedPreferences.getBoolean("switchState", false));
        boolean isCheckedInitial = sharedPreferences.getBoolean("switchState", false);
        if (isCheckedInitial) {
            Toast.makeText(this, "Active", Toast.LENGTH_SHORT).show();
            textView.setText(R.string.detection_started);
            animationView.playAnimation();
            startService(new Intent(this, ShakeFallNotificationService.class));
        } else {
            Toast.makeText(this, "InActive", Toast.LENGTH_SHORT).show();
            animationView.cancelAnimation();
            textView.setText(R.string.turn_on_the_button_to_detect_nfall_or_shake);
            textView.setVisibility(View.VISIBLE);
            stopService(new Intent(this, ShakeFallNotificationService.class));
        }


        detectSwitchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Toast.makeText(this, "Active", Toast.LENGTH_SHORT).show();
                textView.setText(R.string.detection_started);
                animationView.playAnimation();
                startService(new Intent(this, ShakeFallNotificationService.class));
            } else {
                Toast.makeText(this, "InActive", Toast.LENGTH_SHORT).show();
                stopService(new Intent(this, ShakeFallNotificationService.class));
                animationView.cancelAnimation();
                textView.setText(R.string.turn_on_the_button_to_detect_nfall_or_shake);
            }
            sharedPreferences.edit().putBoolean("switchState", isChecked).apply();

        });

        shakeHistoryBtn.setOnClickListener(v -> {
            setIntent(false);
        });
        fallHistoryBtn.setOnClickListener(v -> {
            setIntent(true);
        });
    }
    private void setIntent(boolean isTrue) {
        Intent intent = new Intent(this, Details_Activity.class);
        intent.putExtra("isHistory", isTrue);
        startActivity(intent);
    }
}