package edu.wgu.zamzow.medalert.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = intent.getParcelableExtra(Vars.NOTIFICATION);

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(Vars.Channel_ID, Vars.NotificationName, importance);
        channel.setDescription(Vars.NotificationDescription);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        int id = intent.getIntExtra(Vars.NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }
}