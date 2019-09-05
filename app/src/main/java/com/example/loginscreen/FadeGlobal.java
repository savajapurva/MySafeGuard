package com.example.loginscreen;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class FadeGlobal extends Application {
    private boolean calling;
    public boolean canceledAlarm;
    public boolean confirmedAlarm;
    public boolean fallTimeExceeded;
    public TelephonyManager mgr;
    public boolean pendingCountDown;
    public PhoneStateListener phoneStateListener;

    public void onCreate() {
        setPendingCountDown(false);
        setCanceledAlarm(false);
        setConfirmedAlarm(false);
        setFallTimeExceeded(false);
        this.calling = false;
        super.onCreate();
    }

    @SuppressLint("WrongConstant")
    public void registerOutgoingCallBroadcast(Context c) {
        @SuppressLint("WrongConstant") final AudioManager am = (AudioManager) getSystemService("audio");
        this.phoneStateListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == 0 && FadeGlobal.this.calling) {
                    am.setSpeakerphoneOn(false);
                    FadeGlobal.this.unregisterOutgoingCallBroadcast();
                }
                if (state == 2) {
                    am.setSpeakerphoneOn(true);
                    FadeGlobal.this.calling = true;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        this.mgr = (TelephonyManager) c.getSystemService("phone");
        if (this.mgr != null) {
            this.mgr.listen(this.phoneStateListener, 32);
        }
    }

    public void unregisterOutgoingCallBroadcast() {
        if (this.mgr != null) {
            this.mgr.listen(this.phoneStateListener, 0);
            this.calling = false;
        }
    }

    public boolean getCanceledAlarm() {
        return this.canceledAlarm;
    }

    public boolean getConfirmedAlarm() {
        return this.confirmedAlarm;
    }

    public boolean getFallTimeExceeded() {
        return this.fallTimeExceeded;
    }

    public boolean getPendingCountDown() {
        return this.pendingCountDown;
    }

    public void setCanceledAlarm(boolean b) {
        this.canceledAlarm = b;
    }

    public void setConfirmedAlarm(boolean b) {
        this.confirmedAlarm = b;
    }

    public void setFallTimeExceeded(boolean b) {
        this.fallTimeExceeded = b;
    }

    public void setPendingCountDown(boolean b) {
        this.pendingCountDown = b;
    }
}
