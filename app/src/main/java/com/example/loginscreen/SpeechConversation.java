package com.example.loginscreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SpeechConversation extends AppCompatActivity implements VoiceView.OnRecordListener {

    private static String TAG = "SpeechConversation";

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;

    private TextView  mSpeechRecogText;
    private TextView demotxt;
    private TextView mUserSpeechText;
    private VoiceView mStartStopBtn;

    private CloudSpeechService mCloudSpeechService;
    private VoiceRecorder mVoiceRecorder;
    private boolean mIsRecording = false;

    // Resource caches
    private int mColorHearing;
    private int mColorNotHearing;
    private TextView mStatus;

    private String mSavedText,mSavedText2,mSavedText3,mSavedText4,mSavedText5,mSavedText6,mSavedText7,mSavedText10,mSavedText11,mSavedText12,mSavedText13,mSavedText14,mSavedText15,mSavedText16;
    private Handler mHandler;

    /////////////

    Button btnsos;
    TextView txtmsg;
    TextView locationUpdate;
    long then = 0;
    SharedPreferences prefs;
    long start = 0;
    CustomProgressBar pb;
    int btnpress= 0;
    private final Handler handler = new Handler();
    Boolean mBooleanIsPressed=false;

    ////////////

    //**************************************************//
    //SMS Sending Portion

    //Location
    private static final String TAG1 = SpeechConversation.class.getSimpleName();


    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 900000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 900000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    // location last updated time
    private String mLastUpdateTime;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    //

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    Button btnStopUpdates;

    Button send;
    Double lat=0.0;
    Double lon=0.0;


    //*************************************************//

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, CloudSpeechService.class));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_speech_conversation);
        ButterKnife.bind(this);
        initViews();
        init();
        restoreValuesFromBundle(savedInstanceState);
    }

    protected void onPause(){
        super.onPause();
        Log.e("Application background", "Background");
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    protected void onResume() {
        super.onResume();
    }


    private void startAnimation() {
        SpeechConversation.ProgressBarAnimation localProgressBarAnimation = new SpeechConversation.ProgressBarAnimation(0.0F, 75.0F);
        localProgressBarAnimation.setInterpolator(new OvershootInterpolator(0.5F));
        localProgressBarAnimation.setDuration(4000L);
        pb.startAnimation(localProgressBarAnimation);
    }

    private void clearAnimation() {
        SpeechConversation.ProgressBarAnimation localProgressBarAnimation = new SpeechConversation.ProgressBarAnimation(0.0F, 0.0F);
        localProgressBarAnimation.setInterpolator(new OvershootInterpolator(0.0F));
        pb.clearAnimation();
        pb.setProgress(0);


    }

    private class ProgressBarAnimation extends Animation {
        private float from;
        private float to;

        public ProgressBarAnimation(float from, float to) {
            this.from = from;
            this.to = to;
        }

        protected void applyTransformation(float paramFloat, Transformation paramTransformation) {
            super.applyTransformation(paramFloat, paramTransformation);
            float f = this.from + paramFloat * (this.to - this.from);
            pb.setProgress((int) f);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Prepare Cloud Speech API
        bindService(new Intent(this, CloudSpeechService.class), mServiceConnection,
                BIND_AUTO_CREATE);

        // Start listening to voices
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecorder();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            showPermissionMessageDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }



    @Override
    public void onStop() {
        super.onStop();
    }

    public void onDestroy(){

        super.onDestroy();
    }

    public void stopLocationButtonClick() {

    }


    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                        toggleButtons();
                    }
                });
    }

    private void toggleButtons() {
        if (mRequestingLocationUpdates) {
            btnStopUpdates.setEnabled(true);
        } else {
            btnStopUpdates.setEnabled(false);
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(SpeechConversation.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(SpeechConversation.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }



    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            lat = mCurrentLocation.getLatitude();
            lon = mCurrentLocation.getLongitude();
//            String str = String.valueOf(lat);
//            String str2 = String.valueOf(lon);

            ////
            this.prefs = getSharedPreferences("FallDetector", 0);

            String phoneNumber = this.prefs.getString("phoneNumber", "2267592112");
            String link = "http://maps.google.com/maps?q=loc:" + String.format("%f,%f", lat, lon);
            String smsMessage = "MySafeGuard Alert: \n"+ "This is an emergency. I need urgent help. My last known location is: " +link;



            if(phoneNumber == null || phoneNumber.length() == 0 ||
                    smsMessage == null || smsMessage.length() == 0){
                return;
            }

            if(checkPermission(Manifest.permission.SEND_SMS)){
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
                Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }


        }

    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void initViews() {
        btnStopUpdates = findViewById(R.id.btn_stop_location_updates);
        btnsos = findViewById(R.id.buttonsos);
        txtmsg = findViewById(R.id.txtmsg);
        pb = findViewById(R.id.pb);
        demotxt=findViewById(R.id.demotxt);
        locationUpdate = findViewById(R.id.locationUpdate);

        btnStopUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestingLocationUpdates = false;
                stopLocationUpdates();
                btnStopUpdates.setVisibility(View.INVISIBLE);
                locationUpdate.setVisibility(View.INVISIBLE);
            }
        });

        //********************************//
        //SMS Sending
//        btnsos.setEnabled(true);
//        if(checkPermission(Manifest.permission.SEND_SMS)){
//            send.setEnabled(true);
//        }else{
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
//        }

        //********************************//

        mSavedText = "Help me";
        mSavedText2="Alchemy";
        mSavedText3="send me";
        mSavedText4="take me";
        mSavedText5="acme";
        mSavedText6="poke me";
        mSavedText7="love me";
        mSavedText10="Stop Updates";
        mSavedText11="Stop Update";
        mSavedText12="Update";
        mSavedText13="Stop";
        mSavedText14="Updates";
        mSavedText15="Anthony";
        mSavedText16="let me";

        mStartStopBtn = (VoiceView) findViewById(R.id.recordButton);
        mStartStopBtn.setOnRecordListener(this);

        mUserSpeechText = (TextView) findViewById(R.id.userSpeechText);
        mSpeechRecogText = (TextView) findViewById(R.id.speechRecogText);
        mStatus = (TextView) findViewById(R.id.status);

        final Resources resources = getResources();
        final Resources.Theme theme = getTheme();
        mColorHearing = ResourcesCompat.getColor(resources, R.color.status_hearing, theme);
        mColorNotHearing = ResourcesCompat.getColor(resources, R.color.status_not_hearing, theme);

        mUserSpeechText.setText(mSavedText);
        mHandler = new Handler(Looper.getMainLooper());

        btnsos.setOnTouchListener(new View.OnTouchListener() {

            private final Handler handler = new Handler();
            private final Runnable runnable = new Runnable() {
                public void run() {
                    if(mBooleanIsPressed==true)
                    {

                        btnStopUpdates.setEnabled(true);
                        locationUpdate.setVisibility(View.VISIBLE);
                        btnStopUpdates.setVisibility(View.VISIBLE);
                        // SMS Code
                        //******************************//
                        // Requesting ACCESS_FINE_LOCATION using Dexter library
                        Dexter.withActivity(SpeechConversation.this)
                                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                        mRequestingLocationUpdates = true;
                                        startLocationUpdates();
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {
                                        if (response.isPermanentlyDenied()) {
                                            // open device settings when the permission is
                                            // denied permanently
                                            openSettings();
                                        }
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).check();

                        //***********************************//
                        Toast.makeText(getBaseContext(), "SMS Sent", Toast.LENGTH_SHORT).show();
                    }
                }
            };


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed(runnable, 3000);
                    mBooleanIsPressed = true;
                    startAnimation();

                    btnpress = 1;

                    if (btnpress == 1) {
                        txtmsg.setEnabled(true);
                        txtmsg.setVisibility(View.VISIBLE);
                    } else {
                        txtmsg.setVisibility(View.VISIBLE);
                    }

                }


                else if (event.getAction() == MotionEvent.ACTION_UP) {

                    txtmsg.setVisibility(View.GONE);
                    handler.removeCallbacks(runnable);
                    mBooleanIsPressed=false;
                    if (((Long) System.currentTimeMillis() - then) > 1500) {
                        clearAnimation();
                        //Toast.makeText(getBaseContext(), "Button Unpressed", Toast.LENGTH_SHORT).show();
                        txtmsg.setVisibility(View.GONE);
                        return false;
                    } else {
                        clearAnimation();
                        txtmsg.setVisibility(View.GONE);
                    }
                }

                return false;

            }


        });
    }

    private final CloudSpeechService.Listener mCloudSpeechServiceListener = new CloudSpeechService.Listener() {
        @Override
        public void onSpeechRecognized(final String text, final boolean isFinal) {
            if (isFinal) {
                mVoiceRecorder.dismiss();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (isFinal) {
                        Log.d(TAG, "Final Response : " + text);
                        if (mSavedText.equalsIgnoreCase(text)||mSavedText2.equalsIgnoreCase(text)||mSavedText3.equalsIgnoreCase(text)||mSavedText4.equalsIgnoreCase(text)||mSavedText5.equalsIgnoreCase(text)||mSavedText6.equalsIgnoreCase(text)||mSavedText7.equalsIgnoreCase(text)||mSavedText15.equalsIgnoreCase(text)||mSavedText16.equalsIgnoreCase(text)) {

                            btnStopUpdates.setEnabled(true);
                            locationUpdate.setVisibility(View.VISIBLE);
                            btnStopUpdates.setVisibility(View.VISIBLE);
                            //
                            mSpeechRecogText.setVisibility(View.VISIBLE);
                            mSpeechRecogText.setTextColor(Color.GREEN);
                            mSpeechRecogText.setText("Help Detected");
                            mSpeechRecogText.postDelayed(new Runnable() {
                                public void run() {
                                    mSpeechRecogText.setVisibility(View.INVISIBLE);
                                }
                            }, 3000);
                            // SMS Code
                            //******************************//
                            // Requesting ACCESS_FINE_LOCATION using Dexter library
                            Dexter.withActivity(SpeechConversation.this)
                                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                    .withListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse response) {
                                            mRequestingLocationUpdates = true;
                                            startLocationUpdates();
                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse response) {
                                            if (response.isPermanentlyDenied()) {
                                                // open device settings when the permission is
                                                // denied permanently
                                                openSettings();
                                            }
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                            token.continuePermissionRequest();
                                        }
                                    }).check();


                            //***********************************//

                        }
                        else {
                            if (mSavedText10.equalsIgnoreCase(text)||mSavedText11.equalsIgnoreCase(text)||mSavedText12.equalsIgnoreCase(text)||mSavedText13.equalsIgnoreCase(text)||mSavedText14.equalsIgnoreCase(text)){
                                btnStopUpdates.setVisibility(View.GONE);
                                locationUpdate.setVisibility(View.GONE);
                                stopLocationUpdates();
                            }
                        }
                    }
                    else {
                        Log.d(TAG, "Non Final Response : " + text);
                        mSpeechRecogText.setTextColor(Color.WHITE);
                        mSpeechRecogText.setText("");
                    }
                }
            });
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mCloudSpeechService = CloudSpeechService.from(binder);
            mCloudSpeechService.addListener(mCloudSpeechServiceListener);
            mStatus.setVisibility(View.VISIBLE);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mCloudSpeechService = null;
        }

    };

    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            showStatus(true);
            if (mCloudSpeechService != null) {
                mCloudSpeechService.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        @Override
        public void onVoice(final byte[] buffer, int size) {
            if (mCloudSpeechService != null) {
                mCloudSpeechService.recognize(buffer, size);
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    int amplitude = (buffer[0] & 0xff) << 8 | buffer[1];
                    double amplitudeDb3 = 20 * Math.log10((double)Math.abs(amplitude) / 32768);
                    float radius2 = (float) Math.log10(Math.max(1, amplitudeDb3)) * dp2px(SpeechConversation.this, 20);
                    Log.d("SUJIT","radius2 : " + radius2);
                    mStartStopBtn.animateRadius(radius2 * 10);
                }
            });
        }

        @Override
        public void onVoiceEnd() {
            showStatus(false);
            if (mCloudSpeechService != null) {
                mCloudSpeechService.finishRecognizing();
            }
        }

    };

    @Override
    public void onRecordStart() {
        startStopRecording();
    }

    @Override
    public void onRecordFinish() {
        startStopRecording();
    }

    private void startStopRecording() {

        Log.d(TAG, "# startStopRecording # : " + mIsRecording);
        if (mIsRecording) {
            mStartStopBtn.changePlayButtonState(VoiceView.STATE_RECORDING);
            startVoiceRecorder();
        } else {
            mStartStopBtn.changePlayButtonState(VoiceView.STATE_RECORDING);
            startVoiceRecorder();
        }
    }

    private void startVoiceRecorder() {
        Log.d(TAG, "# startVoiceRecorder #");
        mIsRecording = true;
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    private void stopVoiceRecorder() {

        Log.d(TAG, "# stopVoiceRecorder #");
        mIsRecording = false;
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (permissions.length == 1 && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecorder();
            } else {
                showPermissionMessageDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showPermissionMessageDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This app needs to record audio and recognize your speech")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        }).create();

        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void showStatus(final boolean hearingVoice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatus.setTextColor(hearingVoice ? mColorHearing : mColorNotHearing);
            }
        });
    }

    public static int dp2px(Context context, int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context
                .getResources().getDisplayMetrics());
        return px;
    }
}
