package com.example.loginscreen;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Typeface fadeFont = Typeface.createFromAsset(getAssets(), "fonts/WalkwayBold.ttf");
        TextView text = (TextView) findViewById(R.id.aboutActivityText);
        TextView version = (TextView) findViewById(R.id.aboutActivityVersionText);
        ((TextView) findViewById(R.id.aboutActivityTitle)).setTypeface(fadeFont);
        text.setTypeface(fadeFont);
        version.setTypeface(fadeFont);
    }



    public void exit(View v) {
        super.finish();
    }
}

