package com.example.loginscreen.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.loginscreen.R;
import com.example.loginscreen.util.AppUtil;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BaseActivity";

    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(AppUtil.APP_PREFERENCES, MODE_PRIVATE);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default: {
                break;
            }
        }
    }

    protected void openDrawer(DrawerLayout mDrawerLayout) {
        if (mDrawerLayout != null && !mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        AppUtil.log(TAG, "mDrawerLayout >> openDrawer");
    }

    protected void closeDrawer(DrawerLayout mDrawerLayout) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        AppUtil.log(TAG, "mDrawerLayout >> closeDrawer");
    }
}
