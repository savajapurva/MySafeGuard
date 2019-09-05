package com.example.loginscreen.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.app.NotificationChannel;
import android.app.TaskStackBuilder;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.loginscreen.R;
import com.example.loginscreen.activity.HomeActivity;
import com.example.loginscreen.activity.NotificationActivity;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //getNotification(context);
        String packageName = context.getPackageName();
        Intent intent1 = new Intent();
        intent1.setClassName(packageName, packageName + ".activity.HomeActivity");
        if(intent != null){
            intent1.putExtra("alarmId", intent.getIntExtra("alarmId", 0));
        }
        intent1.setAction(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_LAUNCHER);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent openPending = PendingIntent.getActivity(context, 0,
                intent1, 0);

        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setColor(context.getResources().getColor(R.color.colorPrimaryy));

            //TODO Change Notification Icon
            builder.setSmallIcon(R.drawable.p_firbase_icon);
            builder.setContentTitle("Medications");
            builder.setContentText("Please take your medicine");
            builder.setContentIntent(openPending);
            builder.setVibrate(new long[]{100, 250});

            //Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
            //     + "://" + context.getPackageName() + "/raw/sound_alarm_prompt_reminder.mp3");
            Uri alarmSound=Uri.parse("android.resource://com.example.loginscreen/" + R.raw.sound_alarm_prompt_reminder);

            builder.setSound(alarmSound);
            //builder.setDefaults(Notification.DEFAULT_SOUND);
            builder.setAutoCancel(true);

            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            // Builds the notification and issues it.
            if (mNotifyMgr != null) {
                mNotifyMgr.notify(1, builder.build());
            }

        } catch (SecurityException se) {
            se.printStackTrace();
            context.startActivity(new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "notify_001");
        Intent ii = new Intent(context.getApplicationContext(), HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("hello");
        bigText.setBigContentTitle("Today's Bible Verse");
        bigText.setSummaryText("Text in detail");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }

}