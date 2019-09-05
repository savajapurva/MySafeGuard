package com.example.loginscreen;

import android.app.IntentService;
import android.content.Intent;

public class CloseService2 extends IntentService {
    public CloseService2() {
        super("CloseService2");
    }

    protected void onHandleIntent(Intent intent) {
        stopService(new Intent(getApplicationContext(), MainService.class));
    }
}