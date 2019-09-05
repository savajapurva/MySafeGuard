package com.example.loginscreen;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.util.Iterator;

public class WidgetProvider extends AppWidgetProvider {
    @SuppressLint("WrongConstant")
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        Iterator<RunningServiceInfo> j = am.getRunningServices(50).iterator();
        while (j.hasNext() && !serviceRunning) {
            if (((RunningServiceInfo) j.next()).service.getClassName().equals("com.iter.falldetector.MainService")) {
                serviceRunning = true;
            }
        }
        RemoteViews miWidget = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        SharedPreferences prefs = context.getSharedPreferences("FallDetector", 0);
        boolean found = false;
        Iterator<RunningTaskInfo> i = am.getRunningTasks(50).iterator();
        while (i.hasNext() && !found) {
            if (((RunningTaskInfo) i.next()).baseActivity.getClassName().equals("com.iter.falldetector.AlarmActivity")) {
                found = true;
            }
        }
        if (found) {
            miWidget.setImageViewResource(R.id.widgetImageButton, R.drawable.widget);
        } else if (serviceRunning) {
            miWidget.setOnClickPendingIntent(R.id.widgetImageButton, PendingIntent.getService(context, 0, new Intent(context.getApplicationContext(), CloseService2.class), 268435456));
            miWidget.setImageViewResource(R.id.widgetImageButton, R.drawable.widget);
        } else {
            PendingIntent pendingintent;
            if (prefs.getBoolean("calibrated", false)) {
                pendingintent = PendingIntent.getService(context, 0, new Intent(context.getApplicationContext(), MainService.class), 268435456);
            } else {
                pendingintent = PendingIntent.getActivity(context, 0, new Intent(context.getApplicationContext().getApplicationContext(), FallDetector.class), 268435456);
            }
            miWidget.setOnClickPendingIntent(R.id.widgetImageButton, pendingintent);
            miWidget.setImageViewResource(R.id.widgetImageButton, R.drawable.widget_inactive);
        }
        for (int updateAppWidget : appWidgetIds) {
            appWidgetManager.updateAppWidget(updateAppWidget, miWidget);
        }
    }
}
