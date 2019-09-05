package com.example.loginscreen;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginscreen.activity.HomeActivity;
import com.facebook.accountkit.ui.AccountKitActivity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Dashboard extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    ImageView bgapp, clover, pillreminder, safezone, falldetector, sos;
    LinearLayout textSplash, testhome, menus;
    Animation frombottom;
    Button about;
    private static final int PICK_CONTACT = 1001;
    private Uri contact;
    private boolean hasPhone;
    private SharedPreferences prefs;
    private Cursor nameCursor;
    private String newContactId;
    private Cursor phoneCursor;
    private TextView ContactName, addcontact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        per();
        this.prefs = getSharedPreferences("FallDetector", 0);
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        bgapp = findViewById(R.id.bgapp);
        ContactName =findViewById(R.id.ContactName);
        clover = findViewById(R.id.clover);
        textSplash = findViewById(R.id.testSplash);
        testhome = findViewById(R.id.testhome);
        menus = findViewById(R.id.menus);
        addcontact=findViewById(R.id.addcontact);
        pillreminder = (ImageView)findViewById(R.id.pillreminder);
        safezone = (ImageView)findViewById(R.id.safezone);
        falldetector = (ImageView)findViewById(R.id.falldetector);
        sos = (ImageView)findViewById(R.id.sos);
        about=findViewById(R.id.aboutButton);


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            }
        });

        safezone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, SafezoneActivity.class);
                startActivity(intent);
            }
        });

        pillreminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        falldetector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, FallDetector.class);
                startActivity(intent);
            }
        });

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, SpeechConversation.class);
                startActivity(intent);
            }
        });

//        addcontact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Dashboard.this, ContactPicker.class);
//                startActivity(intent);
//            }
//        });

        bgapp.animate().translationY(-1900).setDuration(800).setStartDelay(300);
        clover.animate().alpha(0).setDuration(800).setStartDelay(300);
        textSplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(200);

        testhome.startAnimation(frombottom);
        menus.startAnimation(frombottom);
    }

    @AfterPermissionGranted(123)
    private void per(){
        String[] perms = {Manifest.permission.SEND_SMS,Manifest.permission.RECORD_AUDIO,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(EasyPermissions.hasPermissions(this,perms)) {
            Toast.makeText(this,"Open SMS",Toast.LENGTH_SHORT).show();

        } else {
            EasyPermissions.requestPermissions(this,"We need Permission to send SMS and access contacts",123,perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }


    @Override
    public void onBackPressed(){

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

}

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    public void toContactPick(View v) {
        try {
            startActivityForResult
                    (new Intent("android.intent.action.PICK", ContactsContract.Contacts.CONTENT_URI), PICK_CONTACT);
        } catch (Exception e) {
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_CONTACT: //10001:
                if (resultCode == -1) {
                    this.contact = data.getData();
                    //Give Location permission and run again to solve error
                    this.newContactId = this.contact.getLastPathSegment();
                    String id = this.prefs.getString("contactId", this.newContactId);
                    try {
                        this.nameCursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, "contact_id=?", new String[]{id}, null);
                        this.phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id=?", new String[]{id}, null);

                        if (this.nameCursor.moveToFirst()) {
                            ContactName.setText(this.nameCursor.getString(this.nameCursor.getColumnIndex("display_name")));
                            Toast.makeText(getApplicationContext(), this.nameCursor.getString(this.nameCursor.getColumnIndex("display_name")), Toast.LENGTH_SHORT).show();

                        }

                        if (this.phoneCursor.moveToFirst()) {
                            SharedPreferences.Editor editor = this.prefs.edit();
                            editor.putString("contactId", this.newContactId);
                            editor.putString("phoneNumber", this.phoneCursor.getString(this.phoneCursor.getColumnIndex("data1")));
                            editor.putString("contactName", this.nameCursor.getString(this.nameCursor.getColumnIndex("display_name")));
                            editor.apply();
                            Toast.makeText(getApplicationContext(), this.nameCursor.getString(this.nameCursor.getColumnIndex("display_name")), Toast.LENGTH_SHORT).show();
                        }
                        return;

                    } catch (Exception e) {

                    }
                }
            default:
                return;
        }
    }
}
