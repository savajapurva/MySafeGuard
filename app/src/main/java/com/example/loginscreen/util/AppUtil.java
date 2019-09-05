package com.example.loginscreen.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.loginscreen.receiver.NotificationReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AppUtil {

    public static final String APP_PREFERENCES = "MySafePreference";

    private static final String TAG = "AppUtil";
    private PendingIntent pendingIntent;

    public static void log(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void log(String tag, String msg, Throwable throwable) {
        Log.e(tag, msg, throwable);
    }

    public static String getDateWithoutTime(long dateTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(dateTime);
        return formatter.format(date);
    }

    public static String getOnlyTime(long dateTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date = new Date(dateTime);
        return formatter.format(date);
    }

    public static void start(Context mContext, ArrayList<Long> futureDates, int alarmId) {
        /*Intent alarmIntent = new Intent(mContext, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (futureDates != null && futureDates.size() > 0) {
            int cc;

            for (long futureDate : futureDates) {
                cc = (new Random()).nextInt(900) + 100;
                Intent alarmIntent = new Intent(mContext, NotificationReceiver.class);
                alarmIntent.putExtra("alarmId", alarmId);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, cc, alarmIntent, 0);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, futureDate, 0, pendingIntent);
            }
        }
    }
}
