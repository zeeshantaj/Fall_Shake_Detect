package com.example.shake_and_fall_check_app;

import static androidx.core.app.ServiceCompat.stopForeground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.shake_and_fall_check_app.Notification.ShakeFallNotificationService;

public class RestartServiceReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_PACKAGE_RESTARTED)) {
            // Restart your service here
            context.startService(new Intent(context, ShakeFallNotificationService.class));
        }
    }
}
