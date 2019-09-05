package com.example.loginscreen;

import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.SimpleXYSeries.ArrayFormat;
import com.androidplot.xy.XYPlot;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

public class FallMainActivity extends Activity {
    private static final int CALIBRATE_TAB = 0;
    private static final int CONTACT_TAB = 1;
    private static final int GRAPHIC_SIZE = 25;
    private static final double GRAVITY = 9.8d;
    private static final int LONG_LENGHT = 22;
    private static final int MAX_NUMBER_PROCESS = 200;
    private static final int MODE_HIGH_SENSIBILITY = 0;
    private static final int MODE_LOW_SENSIBILITY = 2;
    private static final int MODE_NORMAL_SENSIBILITY = 1;
    private static final int MODE_TYPE = 2;
    private static final int SHORT_LENGHT = 12;
    private static final int SIZE = 128;
    private LinkedList<Number> accelerometer;
    private Dialog advice;
    private String contactNameString;
    private TextView contactNameView;
    private ImageButton emailButton;
    private Typeface fadeFont;
    private FadeGlobal globals;
    private XYPlot graphic;
    private ImageView grid;
    private ImageView handleLeft;
    private ImageView handleRight;
    private boolean isBroadcastRegistered;
    private boolean isRunning;
    private LocationManager locationMgr;
    private BroadcastReceiver mReceiver;
    private ImageButton motoButton;
    private TextView move;
    private ImageButton normalButton;
    private ImageButton oldButton;
    private ImageButton phoneCallButton;
    private SharedPreferences prefs;
    private ImageButton runButton;
    private SimpleXYSeries serie;
    private LineAndPointFormatter series1Format;
    private Intent service;
    private RelativeLayout sliderLeft;
    private RelativeLayout sliderRight;
    private ImageButton smsButton;
    private ImageView statusEmail;
    private ImageView test;
    private ImageView statusPhonecall;
    private ImageView statusSensibility;
    private ImageView statusSms;
    private TextView statusTest;
    private Button subButtonLeft;
    private Button subButtonRight;
    private Toast toastMessage;
    // private Tracker tracker;


    class C00711 extends BroadcastReceiver {
        C00711() {
        }

        public void onReceive(Context context, Intent intent) {
            int color = intent.getIntExtra("Color", -16711936);
            switch (color) {
                case -16711936:
                    color = FallMainActivity.this.getResources().getColor(R.color.FadeGreen);
                    FallMainActivity.this.setGrid(R.drawable.grid_green, color, intent.getStringExtra("Move"));
                    break;
                case -7829368:
                    color = FallMainActivity.this.getResources().getColor(R.color.FadeGray);
                    FallMainActivity.this.setGrid(R.drawable.grid_white, color, intent.getStringExtra("Move"));
                    break;
                case -65536:
                    color = FallMainActivity.this.getResources().getColor(R.color.FadeRed);
                    FallMainActivity.this.setGrid(R.drawable.grid_pink, color, intent.getStringExtra("Move"));
                    break;
                case -256:
                    color = FallMainActivity.this.getResources().getColor(R.color.FadeYellow);
                    FallMainActivity.this.setGrid(R.drawable.grid_yellow, color, intent.getStringExtra("Move"));
                    break;
            }
            if (FallMainActivity.this.accelerometer.size() >= FallMainActivity.GRAPHIC_SIZE) {
                FallMainActivity.this.accelerometer.removeFirst();
            }
            FallMainActivity.this.accelerometer.addLast(Double.valueOf(intent.getDoubleExtra("Value", FallMainActivity.GRAVITY)));
            FallMainActivity.this.serie.setModel(FallMainActivity.this.accelerometer, ArrayFormat.Y_VALS_ONLY);
            FallMainActivity.this.graphic.clear();
            FallMainActivity.this.series1Format = new LineAndPointFormatter(Integer.valueOf(color), null, null, new PointLabelFormatter(0));
            FallMainActivity.this.graphic.addSeries(FallMainActivity.this.serie, FallMainActivity.this.series1Format);
            FallMainActivity.this.graphic.redraw();
        }
    }

    class C00722 implements Runnable {
        C00722() {
        }

        public void run() {
            FallMainActivity.this.emailButton.setImageResource(R.drawable.email_off);
            FallMainActivity.this.phoneCallButton.setImageResource(R.drawable.telephone_off);
            FallMainActivity.this.smsButton.setImageResource(R.drawable.sms_off);
            FallMainActivity.this.emailButton.setEnabled(true);
            FallMainActivity.this.phoneCallButton.setEnabled(true);
            FallMainActivity.this.smsButton.setEnabled(true);
            FallMainActivity.this.runButton.setEnabled(true);
        }
    }

    class C00733 implements OnClickListener {
        C00733() {
        }

        public void onClick(View v) {
            FallMainActivity.this.advice.dismiss();
        }
    }

    public void finish() {
        unregisterFeedback();
        super.finish();
    }

    @SuppressLint("WrongConstant")
    public void onBackPressed() {
        if (this.handleRight.getTag().equals("opened")) {
            closeRight();
        } else if (this.handleLeft.getTag().equals("opened")) {
            closeLeft();
        } else {
            this.toastMessage.cancel();
            if (this.isRunning && this.prefs.getBoolean("helpDialogs", true)) {
                Toast.makeText(this, getString(R.string.exitAppServiceRunning), 1).show();
            }
            super.onBackPressed();
        }
    }

    @SuppressLint("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_main);
        this.globals = (FadeGlobal) getApplicationContext();
        this.fadeFont = Typeface.createFromAsset(getAssets(), "fonts/WalkwayBold.ttf");
        this.runButton = (ImageButton) findViewById(R.id.activateServiceButton);
        this.sliderLeft = (RelativeLayout) findViewById(R.id.contentLeft);
        this.sliderRight = (RelativeLayout) findViewById(R.id.contentRight);
        this.subButtonLeft = (Button) findViewById(R.id.subButtonLeft);
        this.test=findViewById(R.id.test);
        this.subButtonRight = (Button) findViewById(R.id.subButtonRight);
        this.handleLeft = (ImageView) findViewById(R.id.handleLeft);
        this.handleRight = (ImageView) findViewById(R.id.handleRight);
        this.smsButton = (ImageButton) findViewById(R.id.smsButton);
        this.emailButton = (ImageButton) findViewById(R.id.emailButton);
        this.phoneCallButton = (ImageButton) findViewById(R.id.phoneCallButton);
        this.oldButton = (ImageButton) findViewById(R.id.oldModeButton);
        this.normalButton = (ImageButton) findViewById(R.id.normalModeButton);
        this.motoButton = (ImageButton) findViewById(R.id.motorbikeModeButton);
        this.contactNameView = (TextView) findViewById(R.id.textLeft);
        this.grid = (ImageView) findViewById(R.id.gridImage);
        this.graphic = (XYPlot) findViewById(R.id.graphic);
        this.move = (TextView) findViewById(R.id.moveInformation);
        this.statusTest = (TextView) findViewById(R.id.statusTest);
        this.statusEmail = (ImageView) findViewById(R.id.statusEmailIcon);
        this.statusPhonecall = (ImageView) findViewById(R.id.statusPhonecallIcon);
        this.statusSms = (ImageView) findViewById(R.id.statusSmsIcon);
        this.statusSensibility = (ImageView) findViewById(R.id.statusSensibilityIcon);
        this.contactNameView.setTypeface(this.fadeFont);
        ((TextView) findViewById(R.id.textRight)).setTypeface(this.fadeFont);
        this.move.setTypeface(this.fadeFont);
        this.toastMessage = Toast.makeText(this, "", 1);
        LinearLayout l = (LinearLayout) this.toastMessage.getView();
        l.setBackgroundColor(0);
        TextView t = (TextView) l.getChildAt(0);
        t.setTypeface(this.fadeFont);
        t.setTextSize(16.0f);
        this.toastMessage.setView(l);
        this.toastMessage.setGravity(49, 0, (int) TypedValue.applyDimension(1, 50.0f, getResources().getDisplayMetrics()));
        @SuppressLint("ResourceType") TextView toastText = (TextView) this.toastMessage.getView().findViewById(16908299);
        toastText.setMaxWidth(400);
        toastText.setGravity(1);
        toastText.setBackgroundColor(getResources().getColor(R.color.Black));
        this.handleLeft.setTag("closed");
        this.handleRight.setTag("closed");
        this.isRunning = false;
        this.serie = new SimpleXYSeries("m/s2");
        this.accelerometer = new LinkedList();
        this.grid.setImageResource(R.drawable.grid_white);
        this.graphic.setRangeBoundaries(Integer.valueOf(0), Integer.valueOf(20), BoundaryMode.FIXED);
        this.graphic.setDomainBoundaries(Integer.valueOf(0), Integer.valueOf(GRAPHIC_SIZE), BoundaryMode.FIXED);
        this.graphic.setTicksPerRangeLabel(20);
        this.graphic.setTicksPerDomainLabel(GRAPHIC_SIZE);
        this.graphic.getGraphWidget().getDomainOriginLabelPaint().setColor(0);
        this.graphic.getGraphWidget().getBackgroundPaint().setColor(0);
        this.graphic.getGraphWidget().getGridBackgroundPaint().setColor(0);
        // this.graphic.getGraphWidget().getGridLinePaint().setColor(0);
        this.graphic.getGraphWidget().getDomainOriginLinePaint().setColor(0);
        this.graphic.getGraphWidget().getRangeOriginLabelPaint().setColor(0);
        this.graphic.getDomainLabelWidget().setVisible(false);
        this.graphic.getGraphWidget().getRangeOriginLinePaint().setColor(0);
        this.graphic.getBorderPaint().setColor(0);
        this.graphic.getBackgroundPaint().setColor(0);
        this.graphic.getRangeLabelWidget().setVisible(false);
        this.graphic.getLegendWidget().setVisible(false);
        this.graphic.getGraphWidget().getDomainOriginLabelPaint().setColor(0);
        this.graphic.getTitleWidget().setVisible(false);
        this.graphic.setPlotPaddingRight(35.0f);
        this.service = new Intent(getApplicationContext(), MainService.class);
        this.mReceiver = new C00711();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                shareContent();
                return true;
            case R.id.menu_settings:
                toSettingsActivity(null);
                return true;
            case R.id.menu_help:
                item.setIcon(R.drawable.help_on);
                printHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPause() {
        unregisterFeedback();
        if (this.advice != null && this.advice.isShowing()) {
            this.advice.cancel();
        }
        super.onPause();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.activity_main, menu);
        menu.getItem(2).setIcon(R.drawable.help);
        MenuItem settings = menu.getItem(1);
        if (this.isRunning) {
            settings.setEnabled(false);
        } else {
            settings.setEnabled(true);
        }
        return true;
    }

    public void onResume() {
        if (this.isRunning) {
            registerFeedback();
        }
        super.onResume();
    }

    @SuppressLint("WrongConstant")
    public void onStart() {
        super.onStart();
        this.prefs = getSharedPreferences("FallDetector", 0);
        closeLeft();
        closeRight();
        this.contactNameString = this.prefs.getString("contactName", getResources().getString(R.string.no_contact));
        this.contactNameView.setText(checkLenght(this.contactNameString, 12));
        this.contactNameView.setTypeface(this.fadeFont);
        initSlidersButtons();
        this.isBroadcastRegistered = false;
        this.isRunning = isServiceRunning("com.example.loginscreen.MainService");
        if (this.isRunning) {
            initConfig(true);
        } else {
            initConfig(false);
        }
        if (this.prefs.getBoolean("testMode", true)) {
            this.statusTest.setVisibility(0);
            this.test.setVisibility(0);
            if (this.globals.getCanceledAlarm()) {
                this.toastMessage.setText(R.string.test_canceled_alarm);
                this.toastMessage.show();
                this.globals.setCanceledAlarm(false);
            } else if (this.globals.getConfirmedAlarm()) {
                this.toastMessage.setText(R.string.test_confirmed_alarm);
                this.toastMessage.show();
                this.globals.setConfirmedAlarm(false);
            }
        } else {
            LayoutInflater li = (LayoutInflater) getSystemService("layout_inflater");
            View view;
            if (this.globals.getCanceledAlarm()) {
                if (this.prefs.getBoolean("helpDialogs", true)) {
                    boolean z;
                    view = li.inflate(R.layout.dialog_activity_fall_main_canceled, null);
                    this.advice = new Dialog(view.getContext(), R.style.DialogTheme);
                    this.advice.setContentView(view);
                    CheckBox box = (CheckBox) this.advice.findViewById(R.id.mainActivityCanceledAdviceCheckBox);
                    z = !this.prefs.getBoolean("helpDialogs", true);
                    box.setChecked(z);
                    ((TextView) this.advice.findViewById(R.id.mainActivityCanceledAdviceDialogTitle)).setTypeface(this.fadeFont);
                    ((TextView) this.advice.findViewById(R.id.mainActivityCanceledAdviceDialogText)).setTypeface(this.fadeFont);
                    box.setTypeface(this.fadeFont);
                    ((Button) this.advice.findViewById(R.id.mainActivityCanceledAdviceDialogButton)).setTypeface(this.fadeFont);
                    this.advice.setCanceledOnTouchOutside(true);
                    this.advice.show();
                } else {
                    //this.toastMessage.setText(R.string.resetService);
                    //this.toastMessage.show();
                    //startService((Intent) null);
                }
                this.globals.setCanceledAlarm(false);
            } else if (this.globals.getConfirmedAlarm()) {
                setGrid(R.drawable.grid_pink, getResources().getColor(R.color.FadeRed), getResources().getString(R.string.fall_confirmed));
                this.graphic.clear();
                this.accelerometer.clear();
                this.graphic.redraw();
                view = li.inflate(R.layout.dialog_activity_fall_main_confirmed, null);
                this.advice = new Dialog(view.getContext(), R.style.DialogTheme);
                this.advice.setContentView(view);
                ((TextView) this.advice.findViewById(R.id.mainActivityConfirmedAdviceDialogTitle)).setTypeface(this.fadeFont);
                ((TextView) this.advice.findViewById(R.id.mainActivityConfirmedAdviceDialogText)).setTypeface(this.fadeFont);
                ((TextView) this.advice.findViewById(R.id.mainActivityConfirmedAdviceDialogNameText)).setText(this.prefs.getString("contactName", "---"));
                String contact = "";
                boolean phonePrinted = false;
                if ((this.prefs.getBoolean("contactSmsMode", false) | this.prefs.getBoolean("contactPhonecallMode", false))) {
                    contact = String.valueOf(contact) + this.prefs.getString("phoneNumber", "");
                    phonePrinted = true;
                }
                if (this.prefs.getBoolean("contactEmailMode", true)) {
                    if (phonePrinted) {
                        contact = String.valueOf(contact) + "\n";
                    }
                    contact = String.valueOf(contact) + this.prefs.getString("eMail", "");
                }
                Date t = new Date(System.currentTimeMillis());
                ((TextView) this.advice.findViewById(R.id.mainActivityConfirmedAdviceDialogContactText)).setText(contact);
                ((TextView) this.advice.findViewById(R.id.mainActivityConfirmedAdviceDialogDateText)).setText(getString(R.string.on) + " " + DateFormat.format("dd/MM/yyyy", t) + " " + getString(R.string.at) + " " + DateFormat.format("kk:mm", t));
                ((Button) this.advice.findViewById(R.id.mainActivityConfirmedAdviceDialogButton)).setTypeface(this.fadeFont);
                this.advice.show();
                this.globals.setConfirmedAlarm(false);
            }
        }

    }

    public void checkHelp(View v) {
        CheckBox cbox = (CheckBox) v;
        Editor editor = this.prefs.edit();
        editor.putBoolean("helpDialogs", !cbox.isChecked());
        editor.apply();
    }

    private String checkLenght(String str, int lenght) {
        if (str.length() > lenght) {
            return str.substring(0, lenght) + "...";
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
        this.subButtonRight.setVisibility(8);
        this.handleRight.setVisibility(0);
        this.handleLeft.setBackgroundResource(R.drawable.contact_1);
        this.handleLeft.setVisibility(0);
        this.sliderLeft.setVisibility(8);
        this.handleLeft.setTag("closed");
        this.handleRight.setClickable(true);
    }

    @SuppressLint("WrongConstant")
    private void closeRight() {
        this.subButtonLeft.setVisibility(8);
        this.handleLeft.setVisibility(0);
        this.handleRight.setBackgroundResource(R.drawable.slevel_active);
        this.handleRight.setVisibility(0);
        this.sliderRight.setVisibility(8);
        this.handleRight.setTag("closed");
        this.handleLeft.setClickable(true);
    }

    public void dismissDialog(View v) {
        setGrid(R.drawable.grid_white, getResources().getColor(R.color.FadeGray), getResources().getString(R.string.off));
        this.graphic.clear();
        this.accelerometer.clear();
        this.graphic.redraw();
        this.advice.dismiss();
    }

    @SuppressLint("WrongConstant")
    private void drawContactOption() {
        if (!this.prefs.getBoolean("testMode", true)) {
            this.statusTest.setVisibility(8);
            this.test.setVisibility(8);
        }
        if (this.prefs.getBoolean("contactEmailMode", true)) {
            this.statusEmail.setVisibility(0);
        }
        if (this.prefs.getBoolean("contactPhonecallMode", false)) {
            this.statusPhonecall.setVisibility(0);
        }
        if (this.prefs.getBoolean("contactSmsMode", false)) {
            this.statusSms.setVisibility(0);
        }
    }

    @SuppressLint("WrongConstant")
    private void drawSensibilityOption() {
        switch (this.prefs.getInt("sensibility", 1)) {
            case 0:
                this.statusSensibility.setImageResource(R.drawable.old_status);
                break;
            case 1:
                this.statusSensibility.setImageResource(R.drawable.normal_status);
                break;
            case 2:
                this.statusSensibility.setImageResource(R.drawable.motorbike_status);
                break;
        }
        this.statusSensibility.setVisibility(0);
    }

    @SuppressLint("WrongConstant")
    public void initConfig(boolean running) {
        if (running) {
            this.runButton.setBackgroundResource(R.drawable.crop_alert_power);
            if (this.handleLeft.getTag().equals("opened")) {
                closeLeft();
            }
            if (this.handleRight.getTag().equals("opened")) {
                closeRight();
            }
            this.handleLeft.setBackgroundResource(R.drawable.contact1_inactive);
            this.handleRight.setBackgroundResource(R.drawable.slevel);
            this.handleLeft.setClickable(false);
            this.handleRight.setClickable(false);
            drawContactOption();
            drawSensibilityOption();
            this.locationMgr = (LocationManager) getSystemService("location");
            if (!this.locationMgr.isProviderEnabled("gps")) {
                this.toastMessage.setText(R.string.gps_disabled);
                this.toastMessage.show();
                return;
            }
            return;
        }
        this.runButton.setBackgroundResource(R.drawable.crop_inactive);
        this.handleLeft.setBackgroundResource(R.drawable.contact_1);
        this.handleRight.setBackgroundResource(R.drawable.slevel_active);
        this.handleLeft.setClickable(true);
        this.handleRight.setClickable(true);
        this.statusEmail.setVisibility(8);
        this.statusPhonecall.setVisibility(8);
        this.statusSms.setVisibility(8);
        this.statusSensibility.setVisibility(8);
        if (!this.prefs.getBoolean("testMode", true)) {
            this.statusTest.setVisibility(4);
            this.test.setVisibility(4);
        }
        initGraph();
    }

    private void initGraph() {
        this.graphic.clear();
        this.graphic.redraw();
        setGrid(R.drawable.grid_white, getResources().getColor(R.color.FadeGray), getString(R.string.off));
    }

    void initSlidersButtons() {
        switch (this.prefs.getInt("sensibility", 1)) {
            case 0:
                setModeButtons(true, false, false);
                break;
            case 1:
                setModeButtons(false, true, false);
                break;
            case 2:
                setModeButtons(false, false, true);
                break;
        }
        if (this.prefs.getBoolean("contactEmailMode", true)) {
            this.emailButton.setImageResource(R.drawable.email_on);
            this.emailButton.setSelected(true);
        } else {
            this.emailButton.setImageResource(R.drawable.email_off);
            this.emailButton.setSelected(false);
        }
        if (this.prefs.getBoolean("contactPhonecallMode", false)) {
            this.phoneCallButton.setImageResource(R.drawable.telephone_on);
            this.phoneCallButton.setSelected(true);
        } else {
            this.phoneCallButton.setImageResource(R.drawable.telephone_off);
            this.phoneCallButton.setSelected(false);
        }
        if (this.prefs.getBoolean("contactSmsMode", false)) {
            this.smsButton.setImageResource(R.drawable.sms_on);
            this.smsButton.setSelected(true);
            return;
        }
        this.smsButton.setImageResource(R.drawable.sms_off);
        this.smsButton.setSelected(false);
    }

    private boolean isContactModeSelected() {
        if (((this.prefs.getBoolean("contactSmsMode", false) | this.prefs.getBoolean("contactEmailMode", true)) | this.prefs.getBoolean("contactPhonecallMode", false))) {
            return true;
        }
        return false;
    }

    public boolean isServiceRunning(String serviceName) {
        boolean serviceRunning = false;
        @SuppressLint("WrongConstant") Iterator<RunningServiceInfo> i = ((ActivityManager) getSystemService("activity")).getRunningServices(MAX_NUMBER_PROCESS).iterator();
        while (i.hasNext() && !serviceRunning) {
            if (((RunningServiceInfo) i.next()).service.getClassName().equals(serviceName)) {
                serviceRunning = true;
            }
        }
        return serviceRunning;
    }

    @SuppressLint("WrongConstant")
    public void openLeft(View v) {
        this.handleRight.setVisibility(8);
        this.subButtonRight.setVisibility(0);
        this.handleLeft.setVisibility(8);
        this.sliderLeft.setVisibility(0);
        this.contactNameView.setText(checkLenght(this.contactNameString, LONG_LENGHT));
        this.handleLeft.setTag("opened");
    }

    @SuppressLint("WrongConstant")
    public void openRight(View v) {
        this.handleLeft.setVisibility(8);
        this.subButtonLeft.setVisibility(0);
        this.handleRight.setVisibility(8);
        this.sliderRight.setVisibility(0);
        this.contactNameView.setText(checkLenght(this.contactNameString, LONG_LENGHT));
        this.handleRight.setTag("opened");
    }

    private void playAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.main_activity_anim_alpha);
        this.emailButton.setEnabled(false);
        this.phoneCallButton.setEnabled(false);
        this.smsButton.setEnabled(false);
        this.runButton.setEnabled(false);
        this.emailButton.setImageResource(R.drawable.email_on);
        this.phoneCallButton.setImageResource(R.drawable.telephone_on);
        this.smsButton.setImageResource(R.drawable.sms_on);
        this.emailButton.setAnimation(animation);
        this.phoneCallButton.setAnimation(animation);
        this.smsButton.setAnimation(animation);
        this.emailButton.postDelayed(new C00722(), 1500);
    }

    public void printHelp() {
        @SuppressLint("WrongConstant") View view = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.help_activity_fall_main, null);
        this.advice = new Dialog(view.getContext(), R.style.DialogThemeMain);
        this.advice.setContentView(view);
        ((TextView) this.advice.findViewById(R.id.helpActivityMainTitleText)).setTypeface(this.fadeFont);
        ((TextView) this.advice.findViewById(R.id.helpActivityMainFooterText)).setTypeface(this.fadeFont);
        ((RelativeLayout) this.advice.findViewById(R.id.helpActivityMainFooter)).setOnClickListener(new C00733());
        ((TextView) this.advice.findViewById(R.id.helpActivityMainGridText)).setTypeface(this.fadeFont);
        ((TextView) this.advice.findViewById(R.id.helpActivityMainServiceText)).setTypeface(this.fadeFont);
        ((TextView) this.advice.findViewById(R.id.helpActivityMainContactText)).setTypeface(this.fadeFont);
        ((TextView) this.advice.findViewById(R.id.helpActivityMainSensibilityText)).setTypeface(this.fadeFont);
        this.advice.show();
    }

    public void registerFeedback() {
        if (!this.isBroadcastRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(this.mReceiver, new IntentFilter(getResources().getString(R.string.feedback)));
            this.isBroadcastRegistered = true;
        }
    }

    public void selectMotoMode(View moto) {
        if (!moto.isSelected()) {
            setModeButtons(false, false, true);
            Editor editor = this.prefs.edit();
            editor.putInt("sensibility", 2);
            editor.apply();
            this.toastMessage.setText(R.string.main_moto_selected);
            this.toastMessage.show();
        }
    }

    public void selectNormalMode(View normal) {
        if (!normal.isSelected()) {
            setModeButtons(false, true, false);
            Editor editor = this.prefs.edit();
            editor.putInt("sensibility", 1);
            editor.apply();
            this.toastMessage.setText(R.string.main_normal_selected);
            this.toastMessage.show();
        }
    }

    public void selectOldMode(View old) {
        if (!old.isSelected()) {
            setModeButtons(true, false, false);
            Editor editor = this.prefs.edit();
            editor.putInt("sensibility", 0);
            editor.apply();
            this.toastMessage.setText(R.string.main_old_selected);
            this.toastMessage.show();
        }
    }

    public void selectContactOption(View v) {
        if (this.prefs.getBoolean("testMode", true)) {
            this.toastMessage.setText(R.string.error_test_mode);
            this.toastMessage.show();
            return;
        }
        if (((this.prefs.getString("phoneNumber", "").equals("")) | (this.prefs.getString("eMail", "").equals("") )) ) {
            {
                this.toastMessage.setText(R.string.no_phone);
            }
            this.toastMessage.show();
            return;
        }
        Editor editor = this.prefs.edit();
        switch (v.getId()) {
            case R.id.phoneCallButton:
                if (!v.isSelected()) {
                    editor.putBoolean("contactPhonecallMode", true);
                    this.phoneCallButton.setImageResource(R.drawable.telephone_on);
                    this.phoneCallButton.setSelected(true);
                    this.toastMessage.setText(R.string.main_phone_call_selected);
                    this.toastMessage.show();
                    break;
                }
                this.phoneCallButton.setImageResource(R.drawable.telephone_off);
                this.phoneCallButton.setSelected(false);
                editor.putBoolean("contactPhonecallMode", false);
                break;
            case R.id.emailButton:
                if (!v.isSelected()) {
                    editor.putBoolean("contactEmailMode", true);
                    this.emailButton.setImageResource(R.drawable.email_on);
                    this.emailButton.setSelected(true);
                    this.toastMessage.setText(R.string.main_email_selected);
                    this.toastMessage.show();
                    break;
                }
                this.emailButton.setImageResource(R.drawable.email_off);
                this.emailButton.setSelected(false);
                editor.putBoolean("contactEmailMode", false);
                break;
            case R.id.smsButton:
                if (!v.isSelected()) {
                    editor.putBoolean("contactSmsMode", true);
                    this.smsButton.setImageResource(R.drawable.sms_on);
                    this.smsButton.setSelected(true);
                    this.toastMessage.setText(R.string.main_sms_selected);
                    this.toastMessage.show();
                    break;
                }
                this.smsButton.setImageResource(R.drawable.sms_off);
                this.smsButton.setSelected(false);
                editor.putBoolean("contactSmsMode", false);
                break;
        }
        editor.apply();
    }

    private void setGrid(int draw, int col, String mov) {
        this.grid.setImageResource(draw);
        this.move.setTextColor(col);
        this.move.setText(mov);
    }

    public void setModeButtons(boolean old, boolean normal, boolean moto) {
        if (old) {
            this.oldButton.setImageResource(R.drawable.mode_old_on);
            this.oldButton.setSelected(true);
            this.normalButton.setImageResource(R.drawable.mode_normal_off);
            this.normalButton.setSelected(false);
            this.motoButton.setImageResource(R.drawable.mode_motorbike_off);
            this.motoButton.setSelected(false);
        } else if (normal) {
            this.oldButton.setImageResource(R.drawable.mode_old_off);
            this.oldButton.setSelected(false);
            this.normalButton.setImageResource(R.drawable.mode_normal_on);
            this.normalButton.setSelected(true);
            this.motoButton.setImageResource(R.drawable.mode_motorbike_off);
            this.motoButton.setSelected(false);
        } else {
            this.oldButton.setImageResource(R.drawable.mode_old_off);
            this.oldButton.setSelected(false);
            this.normalButton.setImageResource(R.drawable.mode_normal_off);
            this.normalButton.setSelected(false);
            this.motoButton.setImageResource(R.drawable.mode_motorbike_on);
            this.motoButton.setSelected(true);
        }
    }

    private void shareContent() {
        // this.tracker.sendEvent("Share", "Aplicacion compartida", "", Long.valueOf(0));
        Intent sendIntent = new Intent();
        sendIntent.setAction("android.intent.action.SEND");
        sendIntent.putExtra("android.intent.extra.TEXT", getResources().getText(R.string.share_content));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    public void startService(View v) {
        boolean z = false;
        boolean z2 = true;
        if (this.isRunning) {
            if (this.isRunning) {
                z2 = false;
            }
            this.isRunning = z2;
            initConfig(false);
            stopService(this.service);
            unregisterFeedback();
            setGrid(R.drawable.grid_white, getResources().getColor(R.color.FadeGray), getString(R.string.off));
            return;
        }
        if (((this.prefs.getBoolean("testMode", true) ? 0 : 1) & (isContactModeSelected() ? 0 : 1)) != 0) {
            this.toastMessage.setText(R.string.error_contact_mode_not_selected);
            closeRight();
            openLeft(new View(this));
            playAnimation();
            this.toastMessage.show();
            return;
        }
        if (!this.isRunning) {
            z = true;
        }
        this.isRunning = z;
        initConfig(true);
        this.service.putExtra("size", 128);
        startService(this.service);
        registerFeedback();
    }

    public void toAboutActivity(View v) {
        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
    }

    public void toCancelAlarm(View v) {
        toSettingsActivity(v);
        this.advice.dismiss();
    }

    public void toSettingsActivity(View v) {
        Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
        if (v == null) {
            settings.putExtra("tab", 0);
        } else if (v.getId() == R.id.subButtonRight) {
            settings.putExtra("tab", 1);
        } else {
            settings.putExtra("tab", 2);
        }
        startActivity(settings);
    }

    public void unregisterFeedback() {
        this.accelerometer.clear();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mReceiver);
        this.isBroadcastRegistered = false;
    }
}

