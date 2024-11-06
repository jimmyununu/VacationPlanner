package com.example.VacationPlanner.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.VacationPlanner.receivers.VacationAlarmReceiver;

import java.util.Date;

public class AlarmScheduler {

    public static void scheduleAlarm(Context context, Date date, String title, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, VacationAlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        long alarmTime = date.getTime();
        Log.d("AlarmScheduler", "Scheduling alarm for: " + alarmTime + " (" + title + ", " + message + ")");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        Log.d("AlarmScheduler", "Alarm scheduled successfully for: " + new Date(alarmTime));
    }
}

