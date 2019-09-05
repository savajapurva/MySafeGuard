package com.example.loginscreen;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Date;

public class Communicator extends Service {
    private static final int INTERVAL = 2000;
    private static final int MILISECS = 1000;
    private static final int SMS_MAX_LENGHT = 160;
    private static final int TIME = 30000;
    private static Handler handlerAlert;
    private static Handler handlerGetPos;
    private double acc = 0.0d;
    private String address;
    private String fallDate;
    private String from;
    private double lat = 0.0d;
    private LocationListener locListener;
    private Date locationDate;
    private LocationManager locationMgr;
    private double lon = 0.0d;
    private String map;
    private boolean noLocationFound;
    private String radius;
    private boolean secondIter;
    private CountDownTimer temp;

    /* renamed from: com.iter.falldetector.Communicator$1 */
    class C00651 extends Handler {

        /* renamed from: com.iter.falldetector.Communicator$1$1 */
        class C00641 implements Runnable {
            C00641() {
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r15 = this;
                r8 = new java.text.DecimalFormat;
                r1 = "###.000000";
                r8.<init>(r1);
                r1 = com.iter.falldetector.Communicator.C00651.this;
                r1 = com.iter.falldetector.Communicator.this;
                r1 = r1.lat;
                r13 = r8.format(r1);
                r1 = com.iter.falldetector.Communicator.C00651.this;
                r1 = com.iter.falldetector.Communicator.this;
                r1 = r1.lon;
                r14 = r8.format(r1);
                r0 = new android.location.Geocoder;
                r1 = com.iter.falldetector.Communicator.C00651.this;
                r1 = com.iter.falldetector.Communicator.this;
                r1 = r1.getApplicationContext();
                r2 = java.util.Locale.getDefault();
                r0.<init>(r1, r2);
                r1 = com.iter.falldetector.Communicator.C00651.this;	 Catch:{ IOException -> 0x00db }
                r1 = com.iter.falldetector.Communicator.this;	 Catch:{ IOException -> 0x00db }
                r1 = r1.lat;	 Catch:{ IOException -> 0x00db }
                r3 = com.iter.falldetector.Communicator.C00651.this;	 Catch:{ IOException -> 0x00db }
                r3 = com.iter.falldetector.Communicator.this;	 Catch:{ IOException -> 0x00db }
                r3 = r3.lon;	 Catch:{ IOException -> 0x00db }
                r5 = 1;
                r7 = r0.getFromLocation(r1, r3, r5);	 Catch:{ IOException -> 0x00db }
                r1 = r7.isEmpty();	 Catch:{ IOException -> 0x00db }
                if (r1 != 0) goto L_0x0099;
            L_0x0055:
                r1 = 0;
                r6 = r7.get(r1);	 Catch:{ IOException -> 0x00db }
                r6 = (android.location.Address) r6;	 Catch:{ IOException -> 0x00db }
                r12 = r6.getMaxAddressLineIndex();	 Catch:{ IOException -> 0x00db }
                r10 = 0;
            L_0x0061:
                if (r10 < r12) goto L_0x006c;
            L_0x0063:
                r1 = com.iter.falldetector.Communicator.handlerAlert;
                r2 = 1;
                r1.sendEmptyMessage(r2);
            L_0x006b:
                return;
            L_0x006c:
                r11 = r6.getAddressLine(r10);	 Catch:{ IOException -> 0x00db }
                if (r11 == 0) goto L_0x0096;
            L_0x0072:
                r1 = com.iter.falldetector.Communicator.C00651.this;	 Catch:{ IOException -> 0x00db }
                r1 = com.iter.falldetector.Communicator.this;	 Catch:{ IOException -> 0x00db }
                r2 = r1.address;	 Catch:{ IOException -> 0x00db }
                r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00db }
                r2 = java.lang.String.valueOf(r2);	 Catch:{ IOException -> 0x00db }
                r3.<init>(r2);	 Catch:{ IOException -> 0x00db }
                r2 = r3.append(r11);	 Catch:{ IOException -> 0x00db }
                r3 = ",";
                r2 = r2.append(r3);	 Catch:{ IOException -> 0x00db }
                r2 = r2.toString();	 Catch:{ IOException -> 0x00db }
                r1.address = r2;	 Catch:{ IOException -> 0x00db }
            L_0x0096:
                r10 = r10 + 1;
                goto L_0x0061;
            L_0x0099:
                r1 = com.iter.falldetector.Communicator.C00651.this;	 Catch:{ IOException -> 0x00db }
                r1 = com.iter.falldetector.Communicator.this;	 Catch:{ IOException -> 0x00db }
                r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00db }
                r3 = com.iter.falldetector.Communicator.C00651.this;	 Catch:{ IOException -> 0x00db }
                r3 = com.iter.falldetector.Communicator.this;	 Catch:{ IOException -> 0x00db }
                r3 = r3.getResources();	 Catch:{ IOException -> 0x00db }
                r4 = 2131099769; // 0x7f060079 float:1.78119E38 double:1.0529031837E-314;
                r3 = r3.getString(r4);	 Catch:{ IOException -> 0x00db }
                r3 = java.lang.String.valueOf(r3);	 Catch:{ IOException -> 0x00db }
                r2.<init>(r3);	 Catch:{ IOException -> 0x00db }
                r3 = ": (";
                r2 = r2.append(r3);	 Catch:{ IOException -> 0x00db }
                r2 = r2.append(r13);	 Catch:{ IOException -> 0x00db }
                r3 = ", ";
                r2 = r2.append(r3);	 Catch:{ IOException -> 0x00db }
                r2 = r2.append(r14);	 Catch:{ IOException -> 0x00db }
                r3 = ")";
                r2 = r2.append(r3);	 Catch:{ IOException -> 0x00db }
                r2 = r2.toString();	 Catch:{ IOException -> 0x00db }
                r1.address = r2;	 Catch:{ IOException -> 0x00db }
                goto L_0x0063;
            L_0x00db:
                r9 = move-exception;
                r1 = com.iter.falldetector.Communicator.C00651.this;	 Catch:{ all -> 0x0127 }
                r1 = com.iter.falldetector.Communicator.this;	 Catch:{ all -> 0x0127 }
                r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0127 }
                r3 = com.iter.falldetector.Communicator.C00651.this;	 Catch:{ all -> 0x0127 }
                r3 = com.iter.falldetector.Communicator.this;	 Catch:{ all -> 0x0127 }
                r3 = r3.getResources();	 Catch:{ all -> 0x0127 }
                r4 = 2131099769; // 0x7f060079 float:1.78119E38 double:1.0529031837E-314;
                r3 = r3.getString(r4);	 Catch:{ all -> 0x0127 }
                r3 = java.lang.String.valueOf(r3);	 Catch:{ all -> 0x0127 }
                r2.<init>(r3);	 Catch:{ all -> 0x0127 }
                r3 = ": (";
                r2 = r2.append(r3);	 Catch:{ all -> 0x0127 }
                r2 = r2.append(r13);	 Catch:{ all -> 0x0127 }
                r3 = ", ";
                r2 = r2.append(r3);	 Catch:{ all -> 0x0127 }
                r2 = r2.append(r14);	 Catch:{ all -> 0x0127 }
                r3 = ")";
                r2 = r2.append(r3);	 Catch:{ all -> 0x0127 }
                r2 = r2.toString();	 Catch:{ all -> 0x0127 }
                r1.address = r2;	 Catch:{ all -> 0x0127 }
                r1 = com.iter.falldetector.Communicator.handlerAlert;
                r2 = 1;
                r1.sendEmptyMessage(r2);
                goto L_0x006b;
            L_0x0127:
                r1 = move-exception;
                r2 = com.iter.falldetector.Communicator.handlerAlert;
                r3 = 1;
                r2.sendEmptyMessage(r3);
                throw r1;
                */
                //     throw new UnsupportedOperationException("Method not decompiled: com.iter.falldetector.Communicator.1.1.run():void");
            }
        }

        C00651() {
        }

        public void handleMessage(Message msg) {
            Communicator.this.temp.cancel();
            new Thread(new C00641()).start();
            Communicator.this.getAlert();
                Communicator.this.stopSelf();
        }
    }

    class C00662 extends Handler {
        C00662() {
        }

        public void handleMessage(Message msg) {
//            if (!Communicator.this.address.equals("")) {
//                DecimalFormat df = new DecimalFormat("###.000000");
//                String sortLat = df.format(Communicator.this.lat).replace(",", ".");
//                Communicator.this.map = "http://maps.google.com/maps?z=16&t=h&q=loc:" + sortLat + "+" + df.format(Communicator.this.lon).replace(",", ".");
//                Communicator.this.radius = "(" + Communicator.this.getResources().getString(R.string.radius) + " " + String.valueOf((int) Communicator.this.acc) + " " + Communicator.this.getResources().getString(R.string.meters) + ")";
//                Communicator.this.getAlert();
//                Communicator.this.stopSelf();
//            }
//            else
          //  {

                Communicator.this.getAlert();
                Communicator.this.stopSelf();}


    }


    class C00673 implements LocationListener {
        C00673() {
        }

        public void onLocationChanged(Location location) {
            Communicator.this.acc = (double) location.getAccuracy();
            Communicator.this.lat = location.getLatitude();
            Communicator.this.lon = location.getLongitude();
            Communicator.this.locationDate = new Date(location.getTime());
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @SuppressLint("WrongConstant")
    public void onCreate() {
        super.onCreate();
        this.address = "";
        this.map = "";
        this.radius = "";
        this.noLocationFound = false;
        Date t = new Date(System.currentTimeMillis());
        this.fallDate = String.valueOf(getResources().getString(R.string.on)) + " " + DateFormat.format("dd/MM/yyyy", t) + " " + getResources().getString(R.string.at) + " " + DateFormat.format("kk:mm", t);
        this.from = "";
        Account[] accounts = AccountManager.get(getApplicationContext()).getAccountsByType("com.google");
        if (accounts.length > 0 && accounts[0] != null) {
            this.from = accounts[0].name;
        }
        handlerGetPos = new C00651();
        handlerAlert = new C00662();
        this.locationMgr = (LocationManager) getSystemService("location");
        this.locListener = new C00673();
        this.temp = new CountDownTimer(30000, 2000) {
            public void onFinish() {
                if (!Communicator.this.locationMgr.isProviderEnabled("gps") || Communicator.this.secondIter) {
                    Communicator.this.locationMgr.removeUpdates(Communicator.this.locListener);
                    Location loc = null;
                    if (Communicator.this.locationMgr.isProviderEnabled("network")) {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        loc = Communicator.this.locationMgr.getLastKnownLocation("network");
                    }
                    if (loc != null) {
                        Communicator.this.lat = loc.getLatitude();
                        Communicator.this.lon = loc.getLongitude();
                        Communicator.this.acc = (double) loc.getAccuracy();
                        Communicator.this.locationDate = new Date(loc.getTime());
                        Communicator.handlerGetPos.sendEmptyMessage(1);
                        return;
                    }
                    Communicator.this.noLocationFound = true;
                    Communicator.handlerAlert.sendEmptyMessage(1);
                    return;
                }
                Communicator.this.secondIter = true;
                Communicator.this.locationMgr.removeUpdates(Communicator.this.locListener);
                Communicator.this.locationMgr.requestLocationUpdates("network", 2000, 0.0f, Communicator.this.locListener);
                Communicator.this.temp.start();
            }

            public void onTick(long arg0) {
                if (Communicator.this.lat != 0.0d && Communicator.this.lon != 0.0d) {
                    Communicator.this.locationMgr.removeUpdates(Communicator.this.locListener);
                    Communicator.handlerGetPos.sendEmptyMessage(1);
                }
            }
        };
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.secondIter = false;
        if (this.locationMgr.isProviderEnabled("gps")) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                int TODO =1 ;
                return TODO;
            }
            this.locationMgr.requestLocationUpdates("gps", 2000, 0.0f, this.locListener);
            this.temp.start();
        } else if (this.locationMgr.isProviderEnabled("network")) {
            this.locationMgr.requestLocationUpdates("network", 2000, 0.0f, this.locListener);
            this.temp.start();
        } else {
            this.noLocationFound = true;
            handlerAlert.sendEmptyMessage(0);
        }
        return 1;
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void getAlert() {
        SharedPreferences prefs = getSharedPreferences("FallDetector", 0);
        if (prefs.getBoolean("contactSmsMode", false)) {
            String smsText = "";

                smsText = "MySafeGuard Alert(Fall): \n"+ "This is an emergency. I need urgent help";

            sendSms(prefs.getString("phoneNumber", ""), smsText);
        }
        if (prefs.getBoolean("contactEmailMode", true)) {
            String mailText = "";
            if (this.noLocationFound) {
                mailText = "MySafeGuard Alert(Fall): \n"+ "This is an emergency. I need urgent help";
            } else {
                mailText = "MySafeGuard Alert(Fall): \n"+ "This is an emergency. I need urgent help";
            }
           // sendEmail(prefs.getString("eMail", ""), mailText);
        }
    }

//    @SuppressLint("WrongConstant")
//    private void sendEmail(String mailTo, String text) {
//        if (mailTo.equals("")) {
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_email_not_indicated), 1).show();
//            return;
//        }
//        new SendEmailTask(getApplicationContext()).execute(this.from, mailTo, text);
//    }

    @SuppressLint("WrongConstant")
    private void sendSms(String phone, String text) {
        if (phone.equals("")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_phone_number_not_indicated), 1).show();
            return;
        }
        new Intent(getApplicationContext(), FallMainActivity.class).addFlags(536870912);
        SmsManager smsMgr = SmsManager.getDefault();
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent("Sms sent!"), 0);
        if (text.length() > SMS_MAX_LENGHT) {
            smsMgr.sendMultipartTextMessage(phone, null, smsMgr.divideMessage(text), null, null);
            return;
        }
        smsMgr.sendTextMessage(phone, null, text, pi, null);
    }
}

