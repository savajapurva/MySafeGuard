package com.example.loginscreen;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MotionEventCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MainService extends Service implements SensorEventListener {
    private static final double FEEDBACK_PERIOD = 0.1d;
    private static final int ID_NOTIF_CAIDA = 1;
    private static final int ID_NOTIF_SERVICE = 4;
    private static final int SIZE = 128;
    private static final int SOUND_ALERT = 1;
    private static final int TO_SECS = 1000000000;
    private static final int VIBRATION_ALERT = 0;
    private static final int VIBSOUND_ALERT = 2;
    private double acc;
    private Double calibrateAverage;
    private int color;
    private int delaySamples;
    private Control f1;
    private Detection f2;
    private Control f3;
    private Analysis f4;
    private boolean fallAlert;
    private Intent feedback;
    private FadeGlobal globals;
    private boolean isAlarmLaunched;
    private boolean isBroadcastRegistered;
    private BroadcastReceiver mReceiver;
    private double module;
    private String move;
    private NotificationManager notifMgr;
    private Notification notification2;
    private Point pPrev;
    private int phase;
    private Queue<Point> post;
    private int postFallCount;
    SharedPreferences prefs;
    private Queue<Point> prev;
    private SensorManager sensormgr;
    private long timeStart = 0;
    private WakeLock wakeLock;

    class C00751 extends BroadcastReceiver {

        class C00741 implements Runnable {
            C00741() {
            }

            public void run() {
                MainService.this.unregisterListener();
                MainService.this.registerListener();
            }
        }

        C00751() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                new Handler().postDelayed(new C00741(), 500);
            }
        }
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    public void onCreate() {
        this.isBroadcastRegistered = false;
        this.isAlarmLaunched = false;
        this.pPrev = new Point(0.0d, 0);
        this.acc = 0.0d;
        this.sensormgr = (SensorManager) getSystemService("sensor");
        this.notifMgr = (NotificationManager) getSystemService("notification");
        this.wakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, MainService.class.getName());
        this.mReceiver = new C00751();
        this.prev = new LinkedList();
        this.post = new LinkedList();
        this.prev.clear();
        this.post.clear();
        this.phase = 0;
        this.postFallCount = 0;
        this.fallAlert = false;
        this.globals = (FadeGlobal) getApplicationContext();
        this.feedback = new Intent();
        this.feedback.setAction(getResources().getString(R.string.feedback));
        super.onCreate();
    }

    public void onDestroy() {
        if (!this.isAlarmLaunched) {
            updateWidget(MainService.class, R.drawable.widget_inactive);
        }
        if (this.isBroadcastRegistered) {
            unregisterReceiver(this.mReceiver);
        }
        this.sensormgr.unregisterListener(this);
        stopForeground(true);
        this.wakeLock.release();
        super.onDestroy();
    }

    public void onSensorChanged(SensorEvent event) {
        this.module = Math.sqrt((Math.pow((double) event.values[0], 2.0d) + Math.pow((double) event.values[1], 2.0d)) + Math.pow((double) event.values[2], 2.0d));
        long time = System.nanoTime();
        Point p = new Point(this.module, time);
        switch (this.phase) {
            case 0:
                this.prev.offer(p);
                if (this.prev.size() == 128) {
                    this.phase = 1;
                    this.f2 = new Detection(getApplicationContext(), this.prev, this.prefs);
                    setFeedBack(-16711936, getString(R.string.feedback_working));
                    break;
                }
                break;
            case 1:
                if (this.f2.fallDetect(p, this.pPrev)) {
                    if (!this.globals.getFallTimeExceeded()) {
                        this.phase = 2;
                        setFeedBack(-256, getResources().getString(R.string.feedback_fall_detection));
                        break;
                    }
                    stopSelf();
                    break;
                }
                break;
            case 2:
                this.post.offer(p);
                if (this.post.size() == 128) {
                    this.f1 = new Control(getApplicationContext(), this.prev);
                    this.f3 = new Control(getApplicationContext(), this.post);
                    this.f4 = new Analysis(this.f1, this.f2, this.f3);
                    switch (this.f4.isFall()) {
                        case 0:
                            if (!checkPost()) {
                                this.phase = 1;
                                if (this.f2.getFalling()) {
                                    this.post.clear();
                                } else {
                                    reset();
                                }
                                setFeedBack(-16711936, getString(R.string.feedback_moving));
                                break;
                            }
                            this.phase = 2;
                            break;
                        case 1:
                            this.phase = 1;
                            reset();
                            setFeedBack(-16711936, getString(R.string.feedback_moving));
                            break;
                        case 2:
                            if (!checkPost()) {
                                this.phase = 1;
                                if (this.f2.getFalling()) {
                                    this.post.clear();
                                } else {
                                    reset();
                                }
                                setFeedBack(-16711936, getString(R.string.feedback_starting));
                                break;
                            }
                            this.phase = 2;
                            break;
                        case 3:
                            this.phase = 3;
                            reset();
                            this.fallAlert = true;
                            setFeedBack(-65536, getString(R.string.feedback_waiting_for_recovery));
                            break;
                        case 4:
                            this.phase = 3;
                            reset();
                            this.fallAlert = true;
                            setFeedBack(-65536, getString(R.string.feedback_waiting_for_recovery));
                            break;
                        case 5:
                            this.phase = 3;
                            reset();
                            setFeedBack(-256, getString(R.string.feedback_changing_movement));
                            break;
                        case 6:
                            this.phase = 3;
                            reset();
                            setFeedBack(-256, getString(R.string.feedback_recovering));
                            break;
                        case 7:
                            this.phase = 1;
                            reset();
                            setFeedBack(-16711936, getString(R.string.feedback_moving));
                            break;
                        case 8:
                            this.phase = 3;
                            reset();
                            setFeedBack(-256, getString(R.string.feedback_moving));
                            break;
                        case 9:
                            this.phase = 3;
                            reset();
                            setFeedBack(-256, getString(R.string.feedback_recovering));
                            break;
                        case MotionEventCompat.ACTION_HOVER_EXIT /*10*/:
                            Double x = (Math.log(this.calibrateAverage) - this.f3.f100A) / this.f3.f101B;
                            if (x > 128.0d) {
                                this.delaySamples = x.intValue() - 128;
                                this.phase = 5;
                            } else {
                                this.phase = 3;
                                reset();
                            }
                            setFeedBack(-256, getString(R.string.feedback_damped_fall));
                            break;
                        default:
                            break;
                    }
                }
                break;
            case 3:
                if (this.postFallCount >= 128) {
                    this.f1 = new Control(getApplicationContext(), this.prev);
                    if (!this.f1.death()) {
                        if (!this.fallAlert) {
                            this.phase = 1;
                            this.postFallCount = 0;
                            this.prev.poll();
                            this.prev.offer(p);
                            setFeedBack(-16711936, getResources().getString(R.string.feedback_recuperation));
                            break;
                        }
                        this.fallAlert = false;
                        this.postFallCount = 1;
                        this.prev.poll();
                        this.prev.offer(p);
                        setFeedBack(-256, getString(R.string.feedback_recovering));
                        break;
                    } else if (!this.fallAlert) {
                        this.fallAlert = true;
                        this.postFallCount = 1;
                        this.prev.poll();
                        this.prev.offer(p);
                        setFeedBack(-65536, getString(R.string.feedback_waiting_for_recovery));
                        break;
                    } else {
                        this.phase = 4;
                        setFeedBack(-65536, getResources().getString(R.string.feedback_falled));
                        this.globals.setPendingCountDown(true);
                        launchAlarm();
                        break;
                    }
                }
                if (this.f2.fallDetect(p, this.pPrev)) {
                    this.phase = 2;
                    this.fallAlert = false;
                    this.postFallCount = 0;
                    setFeedBack(-256, getString(R.string.feedback_new_fall));
                }
                this.postFallCount++;
                break;
            case 5:
                this.delaySamples--;
                if (this.delaySamples == 0) {
                    this.phase = 3;
                    reset();
                    break;
                }
                break;
        }
        long delay = p.getTime() - this.pPrev.getTime();
        if (delay != time) {
            this.acc += ((double) delay) / 1.0E9d;
            if (this.acc >= FEEDBACK_PERIOD) {
                sendFeedBack();
                this.acc = 0.0d;
            }
        }
        this.pPrev = p;
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.prefs = getSharedPreferences("FallDetector", 0);
        this.calibrateAverage = Double.parseDouble(this.prefs.getString("average", "9.81"));
        registerListener();
        this.module = 9.8d;
        setFeedBack(-7829368, getResources().getString(R.string.feedback_initializing));
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        registerReceiver(this.mReceiver, intentFilter);
        this.wakeLock.acquire(10*60*1000L /*10 minutes*/);
        this.timeStart = System.currentTimeMillis();
        this.isBroadcastRegistered = true;
        CharSequence tickerText = getString(R.string.title_activity_main);
        long when = System.currentTimeMillis();
        // Notification notification = new Notification(R.drawable.ic_launcher, tickerText, when);
        CharSequence contentText = getString(R.string.active_service);
        Intent i = new Intent(this, FallDetector.class);
        Context context = getApplicationContext();




        //notification.setLatestEventInfo(context, tickerText, contentText, PendingIntent.getActivity(context, 0, i, 134217728));



        updateWidget(CloseService2.class, R.drawable.widget);
        // this.mTracker.sendEvent("MainService", "StartService", "", Long.valueOf(0));
        return 1;
    }

    private boolean checkPost() {
        ArrayList<Point> points = this.f2.getFallArray();
        int n = points.size() - 128;
        if (n < 0) {
            n = 0;
        }
        for (int i = n; i < points.size(); i++) {
            this.prev.poll();
            this.prev.offer((Point) points.get(i));
        }
        this.f2 = new Detection(getApplicationContext(), this.prev, this.prefs);
        boolean fallFounded = false;
        Queue<Point> postWC = new LinkedList();
        postWC.addAll(this.post);
        Point p = (Point) postWC.poll();
        while (!postWC.isEmpty() && !fallFounded) {
            Point pNext = (Point) postWC.poll();
            if (this.f2.fallDetect(pNext, p)) {
                fallFounded = true;
            }
            p = pNext;
        }
        if (!fallFounded) {
            return false;
        }
        this.post.clear();
        this.post.addAll(postWC);
        return true;
    }

    @SuppressLint("WrongConstant")
    private void launchAlarm() {
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmActivity.class);
        alarmIntent.putExtra("ID_NOTIF_CAIDA", 1);
        alarmIntent.addFlags(536870912);
        PendingIntent pi2 = PendingIntent.getActivity(getApplicationContext(), 0, alarmIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My Notification")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getResources().getString(R.string.notification_title)))
                .setContentText(getResources().getString(R.string.notification_message)).setAutoCancel(true);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mBuilder.setSound(soundUri);
        // mBuilder.setSound(alarmSound);
        mBuilder.setContentIntent(pi2);
        notifMgr.notify(1, mBuilder.build());

        // this.notification2.setLatestEventInfo(getApplicationContext(), getResources().getString(R.string.notification_title), getResources().getString(R.string.notification_message), pi2);
        // this.notifMgr.notify(1, this.notification2);
        try {
            pi2.send();
        } catch (CanceledException e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_notification), 0).show();
            e.printStackTrace();
        }
        this.isAlarmLaunched = true;
        stopSelf();
    }

    private void registerListener() {
        this.sensormgr.registerListener(this, this.sensormgr.getDefaultSensor(1), 1);
    }

    private void reset() {
        updateArrays();
        this.f2 = new Detection(getApplicationContext(), this.prev, this.prefs);
    }

    private void sendFeedBack() {
        this.feedback.putExtra("Color", this.color);
        this.feedback.putExtra("Move", this.move);
        this.feedback.putExtra("Value", this.module);
        LocalBroadcastManager.getInstance(this).sendBroadcast(this.feedback);
    }

    private void setFeedBack(int col, String mov) {
        this.color = col;
        this.move = mov;
    }

    private void unregisterListener() {
        this.sensormgr.unregisterListener(this);
    }

    private void updateArrays() {
        this.prev.clear();
        for (Point offer : this.post) {
            this.prev.offer(offer);
        }
        this.post.clear();
    }

    private void updateWidget(Class<?> cls, int widget) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));
        Intent action = new Intent(getApplicationContext(), cls);
        action.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        action.putExtra("appWidgetIds", appWidgetIds);
        @SuppressLint("WrongConstant") PendingIntent pendingintent = PendingIntent.getService(getApplicationContext(), 0, action, 268435456);
        RemoteViews miWidget = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_layout);
        miWidget.setOnClickPendingIntent(R.id.widgetImageButton, pendingintent);
        miWidget.setImageViewResource(R.id.widgetImageButton, widget);
        for (int updateAppWidget : appWidgetIds) {
            appWidgetManager.updateAppWidget(updateAppWidget, miWidget);
        }
    }
}
