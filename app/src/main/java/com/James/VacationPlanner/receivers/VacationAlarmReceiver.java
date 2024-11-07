package com.James.VacationPlanner.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.util.Log;

import com.James.VacationPlanner.R;
import com.James.VacationPlanner.ui.MainActivity;

public class VacationAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        //debugging logs
        Log.d("VacationAlarmReceiver", "onReceive triggered");
        Log.d("VacationAlarmReceiver", "Received alarm for: " + title + " with message: " + message);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "vacation_alerts";
        NotificationChannel channel = new NotificationChannel(channelId, "Vacation Alerts", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new Notification.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Show the notification
        notificationManager.notify((int) System.currentTimeMillis(), notification);
        Log.d("VacationAlarmReceiver", "Notification shown: " + title);
    }
}


