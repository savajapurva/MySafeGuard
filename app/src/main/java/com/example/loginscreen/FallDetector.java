package com.example.loginscreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class FallDetector extends Activity {
    private static final int COUNTDOWN = 3000;
    private static final int MILISEC = 1000;
    private Typeface fadeFont;
    private FadeGlobal globals;
    private SharedPreferences prefs;
    private CountDownTimer timer;

    public void onBackPressed() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_detector);
        this.fadeFont = Typeface.createFromAsset(getAssets(), "fonts/WalkwayBold.ttf");
        ((TextView) findViewById(R.id.splashText)).setTypeface(this.fadeFont);
//        this.timer = new CountDownTimer(3000, 1000) {
//            public void onFinish() {
//                FallDetector.this.startApp(null);
//            }
//
//            public void onTick(long millisUntilFinished) {
//            }
//        }.start();
        FallDetector.this.startApp(null);
    }

    @SuppressLint("WrongConstant")
    public void startApp(View v) {
       // this.timer.cancel();
        this.prefs = getSharedPreferences("FallDetector", 0);
        if (this.prefs.getBoolean("calibrated", false)) {
            Intent main = new Intent(getApplicationContext(), FallMainActivity.class);
            main.addFlags(67108864);
            startActivity(main);
        } else {
            Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
            settings.putExtra("first", true);
            startActivity(settings);
        }
        finish();
    }
}

