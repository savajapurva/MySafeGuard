package com.example.loginscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.Iterator;

public class AlarmActivity extends Activity {
    private static final int COUNTDOWN_10SECS = 0;
    private static final int COUNTDOWN_20SECS = 1;
    private static final int COUNTDOWN_30SECS = 2;
    private static final int COUNTDOWN_60SECS = 3;
    private static final int COUNTDOWN_90SECS = 4;
    private static final int DASH = 500;
    private static final int DOT = 200;
    private static final int LONG_GAP = 2000;
    private static final int MAX_LENGHT = 20;
    private static final int MEDIUM_GAP = 500;
    private static final int MILISEC = 1000;
    private static final long[] PATTERN;
    private static final int SHORT_GAP = 200;
    private static final int SOUND_ALERT = 1;
    private static final int VIBSOUND_ALERT = 2;
    private int ID;
    private TextView alarmText;
    private Animation alphaAnimation;
    private boolean canceledAlarm;
    private TextView count;
    private long countdownTime;
    private boolean counting;
    private Typeface fadeFont;
    private boolean finished;
    private FadeGlobal globals;
    private ImageView handleLeft;
    private ImageView handleRight;
    private NotificationManager mNotificationManager;
    private SharedPreferences prefs;
    private RelativeLayout sliderLeft;
    private RelativeLayout sliderRight;
    private Button subButtonLeft;
    private Button subButtonRight;
    private CountDownTimer temp;
    private TextView textLeft;
    private TextView titleLeft;
    private Vibrator vib;
    private WakeLock wakeLock;


    class C00622 implements OnClickListener {
        C00622() {
        }

        public void onClick(View v) {
            AlarmActivity.this.openLeft();
        }
    }


    class C00633 implements OnClickListener {
        C00633() {
        }

        public void onClick(View v) {
            AlarmActivity.this.openRight();
        }
    }

    static {
        long[] jArr = new long[19];
        jArr[1] = 200;
        jArr[2] = 200;
        jArr[3] = 200;
        jArr[4] = 200;
        jArr[5] = 200;
        jArr[6] = 500;
        jArr[7] = 500;
        jArr[8] = 200;
        jArr[9] = 500;
        jArr[10] = 200;
        jArr[11] = 500;
        jArr[12] = 500;
        jArr[13] = 200;
        jArr[14] = 200;
        jArr[15] = 200;
        jArr[16] = 200;
        jArr[17] = 200;
        jArr[18] = 2000;
        PATTERN = jArr;
    }

    @SuppressLint("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        this.globals = (FadeGlobal) getApplicationContext();
        this.fadeFont = Typeface.createFromAsset(getAssets(), "fonts/WalkwayBold.ttf");
        this.ID = getIntent().getIntExtra("ID_NOTIF_CAIDA", 1);
        this.prefs = getSharedPreferences("FallDetector", 0);
        this.finished = false;
        this.canceledAlarm = false;
        this.vib = (Vibrator) getSystemService("vibrator");
        this.wakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, AlarmActivity.class.getName());
        switch (this.prefs.getInt("countdown", 1)) {
            case 0:
                this.countdownTime = 10000;
                break;
            case 1:
                this.countdownTime = 20000;
                break;
            case 2:
                this.countdownTime = 30000;
                break;
            case 3:
                this.countdownTime = 60000;
                break;
            case 4:
                this.countdownTime = 90000;
                break;
        }
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        this.alarmText = (TextView) findViewById(R.id.alarmActivityAlarmText);
        this.alarmText.setTypeface(this.fadeFont);
        this.count = (TextView) findViewById(R.id.alarmActivityCountdown);
        this.sliderLeft = (RelativeLayout) findViewById(R.id.alarmActivityContentLeft);
        this.sliderRight = (RelativeLayout) findViewById(R.id.alarmActivityContentRight);
        this.subButtonRight = (Button) findViewById(R.id.alarmActivitySubButtonRight);
        this.subButtonLeft = (Button) findViewById(R.id.alarmActivitySubButtonLeft);
        this.handleLeft = (ImageView) findViewById(R.id.alarmActivityHandleLeft);
        this.handleRight = (ImageView) findViewById(R.id.alarmActivityHandleRight);
        this.titleLeft = (TextView) findViewById(R.id.alarmActivityTitleLeft);
        this.titleLeft.setTypeface(this.fadeFont);
        this.textLeft = (TextView) findViewById(R.id.alarmActivityTextLeft);
        this.textLeft.setTypeface(this.fadeFont);
        this.alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        this.titleLeft.setText(R.string.alert_to);
        if (this.prefs.getBoolean("testMode", false)) {
            this.textLeft.setText(R.string.alert_to_test_mode);
        } else {
            this.textLeft.setText(checkLenght(this.prefs.getString("contactName", getResources().getString(R.string.no_contact))));
        }
        initSliders();
        this.temp = new CountDownTimer(this.countdownTime, 1000) {
            public void onFinish() {
                AlarmActivity.this.counting = false;
                AlarmActivity.this.finished = true;
                AlarmActivity.this.mNotificationManager.cancel(AlarmActivity.this.ID);
                if (!AlarmActivity.this.prefs.getBoolean("testMode", false)) {
                    AlarmActivity.this.globals.setConfirmedAlarm(true);
                    AlarmActivity.this.launchSOS("Fin_cuenta");
                    if (!AlarmActivity.this.isAppOpen()) {
                        AlarmActivity.this.startActivity(new Intent(AlarmActivity.this.getApplicationContext(), FallMainActivity.class));
                    }
                }
                AlarmActivity.this.finish();
            }

            public void onTick(long millisUntilFinished) {
                AlarmActivity.this.count.setText(String.valueOf(((int) millisUntilFinished) / 1000));
            }
        }.start();
        this.counting = true;
        Editor editor = this.prefs.edit();
        editor.putBoolean("pendingCountdown", true);
        editor.apply();
        if (this.prefs.getInt("toneConfiguration", 2) != 1) {
            this.vib.vibrate(PATTERN, 0);
        }
    }

    public void onStart() {
        super.onStart();
        this.wakeLock.acquire(10*60*1000L /*10 minutes*/);
    }

    public void onDestroy() {
        this.wakeLock.release();
        if (this.prefs.getInt("toneConfiguration", 2) != 1) {
            this.vib.cancel();
        }
        if (this.finished) {
            if (this.counting) {
                this.temp.cancel();
            }
            if (!this.canceledAlarm) {
                updateWidget(R.drawable.widget_inactive);
            }
        } else {
            updateWidget(R.drawable.widget);
        }
        super.onDestroy();
    }

    public void onBackPressed() {
        if (this.handleLeft.getTag().equals("opened") || this.handleRight.getTag().equals("opened")) {
            closeRight();
            closeLeft();
            return;
        }
        openRight();
    }

    private String checkLenght(String str) {
        if (str.length() > AlarmActivity.MAX_LENGHT) {
            return str.substring(0, AlarmActivity.MAX_LENGHT) + "...";
        }
        return str;
    }

    public void closeAll(View v) {
        if (this.handleLeft.getTag().equals("opened")) {
            closeLeft();
        } else if (this.handleRight.getTag().equals("opened")) {
            closeRight();
        }
    }

    @SuppressLint("WrongConstant")
    private void closeLeft() {
        this.subButtonRight.clearAnimation();
        this.subButtonRight.setVisibility(8);
        this.handleRight.setVisibility(0);
        this.handleLeft.setVisibility(0);
        this.sliderLeft.setVisibility(8);
        this.handleLeft.setTag("closed");
        this.handleRight.setClickable(true);
    }

    @SuppressLint("WrongConstant")
    private void closeRight() {
        this.subButtonLeft.clearAnimation();
        this.subButtonLeft.setVisibility(8);
        this.handleLeft.setVisibility(0);
        this.handleRight.setVisibility(0);
        this.sliderRight.setVisibility(8);
        this.handleRight.setTag("closed");
        this.handleLeft.setClickable(true);
    }

    private String getSensibility(int value) {
        switch (value) {
            case 0:
                return "Sensibilidad_Alta";
            case 1:
                return "Sensibilidad_Media";
            default:
                return "Sensibilidad_Baja";
        }
    }

    void initSliders() {
        this.handleLeft.setTag("closed");
        this.handleRight.setTag("closed");
        this.handleLeft.setOnClickListener(new C00622());
        this.handleRight.setOnClickListener(new C00633());
    }

    public boolean isAppOpen() {
        boolean appOpen = false;
        @SuppressLint("WrongConstant") Iterator<RunningTaskInfo> i = ((ActivityManager) getSystemService("activity")).getRunningTasks(50).iterator();
        while (i.hasNext() && !appOpen) {
            if (((RunningTaskInfo) i.next()).baseActivity.getClassName().equals("com.example.loginscreen.FallMainActivity")) {
                appOpen = true;
            }
        }
        return appOpen;
    }

    @SuppressLint("WrongConstant")
    public void launchSOS(String accion) {
        if ((this.prefs.getBoolean("contactSmsMode", false) | this.prefs.getBoolean("contactEmailMode", true))) {
            if (this.prefs.getBoolean("contactSmsMode", false)) {
                //  this.tracker.sendEvent("Aviso", "SMS", null, null);
            }
            if (this.prefs.getBoolean("contactEmailMode", true)) {
                // this.tracker.sendEvent("Aviso", "Email", null, null);
            }
            startService(new Intent(getApplicationContext(), Communicator.class));
        }
        if (this.prefs.getBoolean("contactPhonecallMode", false)) {
            this.globals.registerOutgoingCallBroadcast(this);
            Intent call = new Intent("android.intent.action.CALL");
            call.setFlags(268435456);
            call.setData(Uri.parse("tel:" + this.prefs.getString("phoneNumber", "")));
            startActivity(call);
        }
    }

    @SuppressLint("WrongConstant")
    private void openRight() {
        this.handleLeft.setVisibility(8);
        this.subButtonLeft.setVisibility(0);
        this.subButtonLeft.startAnimation(this.alphaAnimation);
        this.handleRight.setVisibility(8);
        this.sliderRight.setVisibility(0);
        this.handleRight.setTag("opened");
    }

    @SuppressLint("WrongConstant")
    private void openLeft() {
        this.handleRight.setVisibility(8);
        this.subButtonRight.setVisibility(0);
        this.subButtonRight.startAnimation(this.alphaAnimation);
        this.handleLeft.setVisibility(8);
        this.sliderLeft.setVisibility(0);
        this.handleLeft.setTag("opened");
    }

    public void toDiscriminateChoice(View v) {
        this.temp.cancel();
        this.finished = true;
        this.mNotificationManager.cancel(this.ID);
        this.globals.setPendingCountDown(false);
        if (v.getId() == R.id.alarmActivitySubButtonLeft) {
            this.canceledAlarm = true;
            this.globals.setCanceledAlarm(true);
            // this.tracker.sendEvent("Caida", "Cancelada", getSensibility(this.prefs.getInt("sensibility", 0)), null);
        } else {
            this.globals.setConfirmedAlarm(true);
            if (!this.prefs.getBoolean("testMode", true)) {
                launchSOS("Confirmada");
            }
        }
        if (!isAppOpen()) {
            startActivity(new Intent(getApplicationContext(), FallMainActivity.class));
        }
        finish();
    }

    private void updateWidget(int widget) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));
        Intent action = new Intent(getApplicationContext(), MainService.class);
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
