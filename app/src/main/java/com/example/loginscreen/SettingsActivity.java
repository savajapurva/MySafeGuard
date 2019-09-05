package com.example.loginscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import jsc.descriptive.MeanVar;

public class SettingsActivity extends Activity implements SensorEventListener {
    private static final int CALIBRATE_TAB = 0;
    private static final int CONTACT_TAB = 1;
    private static final int COUNTDOWN_TAB = 4;
    private static final int EMAIL_CONTACT = 2;
    private static final double MAX_AVERAGE = 11.0d;
    private static final double MAX_DEVIATION = 0.2d;
    private static final int MAX_LENGHT = 12;
    private static final double MIN_AVERAGE = 8.5d;
    private static final int MIN_CALIBRATION_SAMPLES = 40;
    private static final double MIN_DEVIATION = 0.03d;
    private static final int MODE_HIGH_SENSIBILITY = 0;
    private static final int MODE_LOW_SENSIBILITY = 2;
    private static final int MODE_NORMAL_SENSIBILITY = 1;
    private static final int MODE_TAB = 2;
    private static final int PHONECALL_CONTACT = 1;
    private static final int PICK_AUDIO = 1002;
    private static final int PICK_CONTACT = 1001;
    private static final int SIZE = 128;
    private static final int SMS_CONTACT = 0;
    private static final int TONE_TAB = 3;
    private Animation alphaAnimation;
    private RelativeLayout calibrateLayout;
    private RelativeLayout calibrateTab;
    private ImageView calibratingImageView;
    private TextView calibratingTextView;
    private Uri contact;
    private MyListAdapter contactAdapter;
    private Dialog contactForm;
    private RelativeLayout contactTab;
    private TextView contactTabAddButton;
    private TextView contactTabDataButton;
    private RelativeLayout countdownTab;
    private Cursor eMailCursor;
    private Dialog error;
    private Typeface fadeFont;
    private boolean firstCalibration;
    private boolean hasEmail;
    private boolean hasPhone;
    private boolean hasSms;
    private Dialog help;
    private RelativeLayout modeTab;
    private TextView modeTabText;
    private TextView modeTabTitle;
    private Cursor nameCursor;
    private String newContactId;
    private Cursor phoneCursor;
    private SharedPreferences prefs;
    private Animation rotateAnimation;
    private double[] samples = new double[128];
    private int samplesCont;
    private Sensor sensor;
    private SensorManager sensormgr;
    private RelativeLayout settings;
    private TextView settingsTitle;
    private int tab;
    private CountDownTimer temp;
    private Dialog test;
    private Toast toastMessage;
    private Button toneButton;
    private RelativeLayout toneTab;


    class C00793 implements OnLongClickListener {
        C00793() {
        }

        public boolean onLongClick(View v) {
            if (SettingsActivity.this.prefs.getBoolean("testMode", true)) {
                SettingsActivity.this.toastMessage.setText(SettingsActivity.this.getResources().getString(R.string.error_already_test_mode));
                SettingsActivity.this.toastMessage.show();
            } else {
                @SuppressLint("WrongConstant") View view = ((LayoutInflater) SettingsActivity.this.getSystemService("layout_inflater")).inflate(R.layout.dialog_activity_test, null);
                SettingsActivity.this.test = new Dialog(view.getContext(), R.style.DialogTheme);
                SettingsActivity.this.test.setContentView(view);
                SettingsActivity.this.test.setCanceledOnTouchOutside(true);
                ((TextView) SettingsActivity.this.test.findViewById(R.id.testDialogTitle)).setTypeface(SettingsActivity.this.fadeFont);
                ((TextView) SettingsActivity.this.test.findViewById(R.id.testDialogText)).setTypeface(SettingsActivity.this.fadeFont);
                ((Button) SettingsActivity.this.test.findViewById(R.id.testDialogButton)).setTypeface(SettingsActivity.this.fadeFont);
                SettingsActivity.this.test.show();
            }
            return true;
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Integer[] MTHUMBS = new Integer[]{R.drawable.calibrate, R.drawable.contact, R.drawable.mode, R.drawable.ringtone, R.drawable.wait};
        private Context mContext;
        private Integer[] mThumbs;

        public ImageAdapter(Context c) {
            this.mContext = c;
            clear();
        }

        public int getItemIdInt(int position) {
            return this.mThumbs[position];
        }

        public int getItemWidth(int position) {
            return SettingsActivity.this.getResources().getDrawable(this.mThumbs[position]).getMinimumWidth();
        }

        public void clear() {
            this.mThumbs = (Integer[]) this.MTHUMBS.clone();
        }

        public void setImage(int position, int drawable) {
            this.mThumbs[position] = drawable;
        }

        public void setSelected(int position) {
            clear();
            switch (position) {
                case 0:
                    setImage(position, R.drawable.calibrate_on);
                    return;
                case 1:
                    setImage(position, R.drawable.contact_on);
                    return;
                case 2:
                    setImage(position, R.drawable.mode_on);
                    return;
                case 3:
                    setImage(position, R.drawable.ringtone_on);
                    return;
                case 4:
                    setImage(position, R.drawable.wait_on);
                    return;
                default:
                    return;
            }
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(this.mContext);
                imageView.setLayoutParams(new LayoutParams(-2, -2));
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(this.mThumbs[position]);
            return imageView;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public int getCount() {
            return this.mThumbs.length;
        }
    }

    public class MyListAdapter extends BaseAdapter {
        private String[] contactOptions;
        private String[] countdownOptions;
        private Typeface font;
        private LayoutInflater inflater = null;
        private String[] modeOptions;
        private boolean[] multiple_selection;
        private int selection;
        private String[] toneOptions;
        private int type;

        private class RowView {
            TextView rowText;

            RowView() {
                this.rowText = new TextView(SettingsActivity.this.getApplicationContext());
                this.rowText.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            }
        }

        public MyListAdapter(Context c, int s, int t, Typeface f) {
            this.countdownOptions = SettingsActivity.this.getResources().getStringArray(R.array.countdown_options);
            this.contactOptions = SettingsActivity.this.getResources().getStringArray(R.array.contact_options);
            this.modeOptions = SettingsActivity.this.getResources().getStringArray(R.array.mode_options);
            this.toneOptions = SettingsActivity.this.getResources().getStringArray(R.array.tone_options);
            this.selection = s;
            this.type = t;
            this.inflater = LayoutInflater.from(c);
            this.font = f;
        }

        public MyListAdapter(Context c, boolean[] ms, int t, Typeface f) {
            this.countdownOptions = SettingsActivity.this.getResources().getStringArray(R.array.countdown_options);
            this.contactOptions = SettingsActivity.this.getResources().getStringArray(R.array.contact_options);
            this.modeOptions = SettingsActivity.this.getResources().getStringArray(R.array.mode_options);
            this.toneOptions = SettingsActivity.this.getResources().getStringArray(R.array.tone_options);
            this.multiple_selection = ms;
            this.type = t;
            this.inflater = LayoutInflater.from(c);
            this.font = f;
        }

        public boolean isNoOptionSelected() {
            for (boolean z : this.multiple_selection) {
                if (z) {
                    return false;
                }
            }
            return true;
        }

        public boolean setMultipleSelection(int s) {
            this.multiple_selection[s] = !this.multiple_selection[s];
            return this.multiple_selection[s];
        }

        public void setSelection(int s) {
            this.selection = s;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public int getCount() {
            switch (this.type) {
                case 1:
                    return this.contactOptions.length;
                case 2:
                    return this.modeOptions.length;
                case 3:
                    return this.toneOptions.length;
                case 4:
                    return this.countdownOptions.length;
                default:
                    return 0;
            }
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RowView item;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.option_row, null);
                item = new RowView();
                item.rowText = (TextView) convertView.findViewById(R.id.optionRowText);
                item.rowText.setTypeface(this.font);
                convertView.setTag(item);
            } else {
                item = (RowView) convertView.getTag();
            }
            switch (this.type) {
                case 1:
                    item.rowText.setText(this.contactOptions[position]);
                    if (position != this.selection) {
                        item.rowText.setBackgroundResource(R.drawable.magic);
                        break;
                    }
                    item.rowText.setBackgroundResource(R.drawable.magic2);
                    break;
                case 2:
                    item.rowText.setText(this.modeOptions[position]);
                    if (position != this.selection) {
                        item.rowText.setBackgroundResource(R.drawable.magic);
                        break;
                    }
                    item.rowText.setBackgroundResource(R.drawable.magic2);
                    break;

                case 3:
                    item.rowText.setText(this.toneOptions[position]);
                    if (position != this.selection) {
                        item.rowText.setBackgroundResource(R.drawable.magic);
                        break;

                    }
                    item.rowText.setBackgroundResource(R.drawable.magic2);
                    break;
                case 4:
                    item.rowText.setText(this.countdownOptions[position]);
                    if (position != this.selection) {
                        item.rowText.setBackgroundResource(R.drawable.magic);
                        break;
                    }
                    item.rowText.setBackgroundResource(R.drawable.magic2);
                    break;
            }
            return convertView;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_CONTACT /*1001*/:
                if (resultCode == -1) {
                    this.contact = data.getData();
                    //Give Location permission and run again to solve error
                    this.newContactId = this.contact.getLastPathSegment();
                    getContactData(this.newContactId, R.string.contact_tab_new_contact_title);
                    return;
                }
                this.toastMessage.setText(R.string.error_pick_contact);
                this.toastMessage.show();
                return;
            case PICK_AUDIO /*1002*/:
                if (resultCode == -1) {
                    Uri tone = (Uri) data.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
                    Editor audioEditor = this.prefs.edit();
                    if (tone != null) {
                        audioEditor.putString("toneUri", tone.toString());
                        audioEditor.putString("toneTitle", RingtoneManager.getRingtone(getApplicationContext(), tone).getTitle(getApplicationContext()));
                    } else {
                        audioEditor.putString("toneUri", tone.toString());
                        audioEditor.putString("toneTitle", RingtoneManager.getRingtone(getApplicationContext(), tone).getTitle(getApplicationContext()));
                    }
                    audioEditor.apply();
                    setToneTitle();
                    return;
                }
                this.toastMessage.setText(R.string.error_pick_tone);
                this.toastMessage.show();
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        if (this.firstCalibration && this.prefs.getBoolean("calibrated", false)) {
            startActivity(new Intent(getApplicationContext(), FallMainActivity.class));
        }
        super.onBackPressed();
    }

    @SuppressLint("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.fadeFont = Typeface.createFromAsset(getAssets(), "fonts/WalkwayBold.ttf");
        Intent getterIntent = getIntent();
        this.tab = getterIntent.getIntExtra("tab", 0);
        this.firstCalibration = getterIntent.getBooleanExtra("first", false);
        this.settings = (RelativeLayout) findViewById(R.id.settingsLayout);
        this.calibrateLayout = (RelativeLayout) findViewById(R.id.calibrateLayout);
        this.calibrateTab = (RelativeLayout) findViewById(R.id.calibrateTabLayout);
        this.contactTab = (RelativeLayout) findViewById(R.id.contactTabLayout);
        this.modeTab = (RelativeLayout) findViewById(R.id.modeTabLayout);
        this.toneTab = (RelativeLayout) findViewById(R.id.toneTabLayout);
        this.countdownTab = (RelativeLayout) findViewById(R.id.countdownTabLayout);
        this.settingsTitle = (TextView) findViewById(R.id.settingsTitle);
        this.settingsTitle.setTypeface(this.fadeFont);
        this.prefs = getSharedPreferences("FallDetector", 0);
        GridView gridview = (GridView) findViewById(R.id.settingsGridview);
        final ImageAdapter adapter = new ImageAdapter(this);
        adapter.setSelected(this.tab);
        gridview.setAdapter(adapter);
        gridview.getLayoutParams().width = getWidestView(this, adapter);
        final GridView gridView = gridview;
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                adapter.clear();
                adapter.setSelected(position);
                gridView.setAdapter(adapter);
                SettingsActivity.this.setSettingsLayout(position);
            }
        });
        this.calibratingImageView = (ImageView) findViewById(R.id.calibratingProcessImageView);
        this.calibratingTextView = (TextView) findViewById(R.id.calibratingProcessTextView);
        this.calibratingTextView.setTypeface(this.fadeFont);
        this.rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        this.alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        ((TextView) findViewById(R.id.calibrateTabText)).setTypeface(this.fadeFont);
        this.sensormgr = (SensorManager) getSystemService("sensor");
        this.sensor = this.sensormgr.getDefaultSensor(1);
        this.temp = new CountDownTimer(3000, 250) {
            public void onFinish() {
                SettingsActivity.this.sensormgr.unregisterListener(SettingsActivity.this);
                if (SettingsActivity.this.samplesCont < SettingsActivity.MIN_CALIBRATION_SAMPLES) {
                    SettingsActivity.this.printError(SettingsActivity.this.getResources().getString(R.string.calibrate_average_error));
                    SettingsActivity.this.calibrateFinish();
                    return;
                }
                MeanVar calibration = new MeanVar(SettingsActivity.this.checkArray(SettingsActivity.this.samples));
                double av = calibration.getMean();
                if (av > SettingsActivity.MAX_AVERAGE || av < SettingsActivity.MIN_AVERAGE) {
                    SettingsActivity.this.printError(SettingsActivity.this.getResources().getString(R.string.calibrate_average_error));
                    SettingsActivity.this.calibrateFinish();
                    return;
                }
                double dev = calibration.getSd();
                if (dev > SettingsActivity.MAX_DEVIATION) {
                    SettingsActivity.this.printError(SettingsActivity.this.getResources().getString(R.string.calibrate_deviation_error));
                    SettingsActivity.this.calibrateFinish();
                    return;
                }
                if (dev < SettingsActivity.MIN_DEVIATION) {
                    dev = SettingsActivity.MIN_DEVIATION;
                }
                SettingsActivity.this.storeCalibrate(dev, av);
                SettingsActivity.this.calibrateFinish();
            }

            public void onTick(long millisUntilFinished) {
            }
        };
        this.toastMessage = Toast.makeText(this, "", 0);
        LinearLayout l = (LinearLayout) this.toastMessage.getView();
        l.setBackgroundColor(0);
        TextView t = (TextView) l.getChildAt(0);
        t.setTypeface(this.fadeFont);
        t.setTextAppearance(getApplicationContext(), R.style.ToastMessageText);
        this.toastMessage.setView(l);
        this.toastMessage.setGravity(81, 0, MIN_CALIBRATION_SAMPLES);
        this.contactTabDataButton = (TextView) findViewById(R.id.contactTabChangeDataButton);
        this.contactTabDataButton.setTypeface(this.fadeFont);
        if (this.prefs.getBoolean("testMode", true)) {
            this.contactTabDataButton.setVisibility(8);
        }
        this.contactTabAddButton = (TextView) findViewById(R.id.contactTabAddContactButton);
        this.contactTabAddButton.setTypeface(this.fadeFont);
        this.contactTabAddButton.setOnLongClickListener(new C00793());
        this.contactAdapter = new MyListAdapter((Context) this, new boolean[]{this.prefs.getBoolean("contactSmsMode", false), this.prefs.getBoolean("contactPhonecallMode", false), this.prefs.getBoolean("contactEmailMode", true)}, 1, this.fadeFont);
        setContactData(this.prefs.getString("contactName", ""), this.prefs.getString("phoneNumber", ""), this.prefs.getString("eMail", ""));

        this.modeTabText = (TextView) findViewById(R.id.modeTabText);
        this.modeTabTitle = (TextView) findViewById(R.id.modeTabTitle);
        this.modeTabText.setTypeface(this.fadeFont);
        this.modeTabTitle.setTypeface(this.fadeFont);
        setModeText();
        ListView modeTabList = (ListView) findViewById(R.id.modeTabList);
        final MyListAdapter modeAdapter = new MyListAdapter((Context) this, this.prefs.getInt("sensibility", 1), 2, this.fadeFont);
        modeTabList.setAdapter(modeAdapter);
        ListView listView = modeTabList;
        final ListView finalListView = listView;
        modeTabList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                modeAdapter.setSelection(position);
                finalListView.setAdapter(modeAdapter);
                Editor editor = SettingsActivity.this.prefs.edit();
                editor.putInt("sensibility", position);
                editor.apply();
                SettingsActivity.this.setModeText();
            }
        });
        this.toneButton = (Button) findViewById(R.id.toneTabAddToneButton);
        this.toneButton.setTypeface(this.fadeFont);
        setToneTitle();
        ListView toneTabList = (ListView) findViewById(R.id.toneTabList);
        final MyListAdapter toneAdapter = new MyListAdapter((Context) this, this.prefs.getInt("toneConfiguration", 2), 3, this.fadeFont);
        toneTabList.setAdapter(toneAdapter);
        listView = toneTabList;
        final ListView finalListView1 = listView;
        toneTabList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                toneAdapter.setSelection(position);
                finalListView1.setAdapter(toneAdapter);
                Editor editor = SettingsActivity.this.prefs.edit();
                editor.putInt("toneConfiguration", position);
                editor.apply();
            }
        });
        final ListView countdownTabList = (ListView) findViewById(R.id.countdownTabList);
        final MyListAdapter countdownAdapter = new MyListAdapter((Context) this, this.prefs.getInt("countdown", 1), 4, this.fadeFont);
        countdownTabList.setAdapter(countdownAdapter);
        countdownTabList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                countdownAdapter.setSelection(position);
                countdownTabList.setAdapter(countdownAdapter);
                Editor editor = SettingsActivity.this.prefs.edit();
                editor.putInt("countdown", position);
                editor.apply();
            }
        });
        setSettingsLayout(this.tab);
    }

    @SuppressLint("WrongConstant")
    public void setTestMode(View v) {
        Editor editor = this.prefs.edit();
        editor.putString("phoneNumber", "");
        editor.putString("eMail", "");
        editor.putString("contactName", getResources().getString(R.string.no_contact));
        editor.putBoolean("contactSmsMode", false);
        editor.putBoolean("contactPhonecallMode", false);
        editor.putBoolean("contactEmailMode", false);
        editor.putBoolean("testMode", true);
        editor.apply();
        this.contactAdapter = new MyListAdapter((Context) this, new boolean[3], 1, this.fadeFont);
        setContactData(getResources().getString(R.string.no_contact), "", "");
        this.contactTabDataButton.setVisibility(8);
        this.test.dismiss();
    }

    public void onDestroy() {
        if (this.nameCursor != null) {
            this.nameCursor.close();
        }
        if (this.eMailCursor != null) {
            this.eMailCursor.close();
        }
        if (this.phoneCursor != null) {
            this.phoneCursor.close();
        }
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                shareContent();
                return true;
            case R.id.menu_help:
                item.setIcon(R.drawable.help_on);
                printHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.activity_main, menu);
        menu.getItem(2).setIcon(R.drawable.help);
        menu.getItem(1).setEnabled(false);
        return true;
    }


    public void onSensorChanged(SensorEvent event) {
        if (this.samplesCont < 128) {
            this.samples[this.samplesCont] = Math.sqrt((Math.pow((double) event.values[0], 2.0d) + Math.pow((double) event.values[1], 2.0d)) + Math.pow((double) event.values[2], 2.0d));
            this.samplesCont++;
        }
    }

    public void checkHelp(View v) {
        CheckBox cbox = (CheckBox) v;
        Editor editor = this.prefs.edit();
        if (cbox.isChecked()) {
            editor.putBoolean("helpDialogs", false);
        } else {
            editor.putBoolean("helpDialogs", true);
        }
        editor.apply();
    }

    private String checkLenght(String str) {
        if (str.length() > 12) {
            return str.substring(0, 12) + "...";
        }
        return str;
    }

    @SuppressLint("WrongConstant")
    private void clearLayout() {
        this.calibrateTab.setVisibility(8);
        this.contactTab.setVisibility(8);
        this.modeTab.setVisibility(8);
        this.toneTab.setVisibility(8);
        this.countdownTab.setVisibility(8);
    }

    public void dismissDialog(View v) {
        this.help.dismiss();
    }

    public static int getWidestView(Context context, ImageAdapter adapter) {
        int maxWidth = 0;
        View view = null;
        FrameLayout fakeParent = new FrameLayout(context);
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            view = adapter.getView(i, view, fakeParent);
            view.measure(0, 0);
            int width = view.getMeasuredWidth();
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

    public void printHelp() {
        boolean z;
        ((TextView) this.help.findViewById(R.id.settingsActivityAssistDialogTitle)).setTypeface(this.fadeFont);
        ((TextView) this.help.findViewById(R.id.settingsActivityAssistDialogText)).setTypeface(this.fadeFont);
        ((Button) this.help.findViewById(R.id.settingsActivityAssistDialogButton)).setTypeface(this.fadeFont);
        CheckBox box = (CheckBox) this.help.findViewById(R.id.settingsActivityAssistDialogCheckBox);
        if (this.prefs.getBoolean("helpDialogs", true)) {
            z = false;
        } else {
            z = true;
        }
        box.setChecked(z);
        box.setTypeface(this.fadeFont);
        this.help.setCanceledOnTouchOutside(true);
        this.help.show();
    }

    private void shareContent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction("android.intent.action.SEND");
        sendIntent.putExtra("android.intent.extra.TEXT", getResources().getText(R.string.share_content));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    @SuppressLint("WrongConstant")
    private void setSettingsLayout(int op) {
        clearLayout();
        View view = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_activity_settings, null);
        this.help = new Dialog(view.getContext(), R.style.DialogTheme);
        this.help.setContentView(view);
        switch (op) {
            case 0:
                if (this.prefs.getBoolean("calibrated", false)) {
                    this.settingsTitle.setText(getResources().getString(R.string.calibrate_tab_title_calibrated));
                } else {
                    this.settingsTitle.setText(getResources().getString(R.string.calibrate_tab_title_no_calibrated));
                }
                this.calibrateTab.setVisibility(0);
                ((TextView) this.help.findViewById(R.id.settingsActivityAssistDialogText)).setText(R.string.settings_activity_calibrate_assist_message);
                break;
            case 1:
                this.settingsTitle.setText(R.string.contact_tab_title);
                this.contactTab.setVisibility(0);
                ((TextView) this.help.findViewById(R.id.settingsActivityAssistDialogText)).setText(R.string.settings_activity_contact_assist_message);
                break;
            case 2:
                this.settingsTitle.setText(R.string.mode_tab_title);
                this.modeTab.setVisibility(0);
                ((TextView) this.help.findViewById(R.id.settingsActivityAssistDialogText)).setText(R.string.settings_activity_mode_assist_message);
                break;
            case 3:
                this.settingsTitle.setText(R.string.tone_tab_title);
                this.toneTab.setVisibility(0);
                ((TextView) this.help.findViewById(R.id.settingsActivityAssistDialogText)).setText(R.string.settings_activity_tone_assist_message);
                break;
            case 4:
                this.settingsTitle.setText(R.string.countdown_tab_title);
                this.countdownTab.setVisibility(0);
                ((TextView) this.help.findViewById(R.id.settingsActivityAssistDialogText)).setText(R.string.settings_activity_countdown_assist_message);
                break;
        }
        if (this.prefs.getBoolean("helpDialogs", true)) {
            printHelp();
        }
    }

    public void toAboutActivity(View v) {
        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
    }

    @SuppressLint("WrongConstant")
    private void calibrateFinish() {
        this.calibrateLayout.setVisibility(8);
        this.settings.setVisibility(0);
        this.calibratingImageView.clearAnimation();
        this.calibratingTextView.clearAnimation();
    }

    @SuppressLint("WrongConstant")
    public void calibrateStart(View v) {
        this.settings.setVisibility(8);
        this.calibrateLayout.setVisibility(0);
        this.samplesCont = 0;
        this.sensormgr.registerListener(this, this.sensor, 1);
        this.temp.start();
        this.calibratingImageView.setAnimation(this.rotateAnimation);
        this.calibratingTextView.setAnimation(this.alphaAnimation);
    }

    private double[] checkArray(double[] array) {
        if (this.samplesCont == 128) {
            return array;
        }
        double[] checkedArray = new double[this.samplesCont];
        System.arraycopy(array, 0, checkedArray, 0, this.samplesCont);
        return checkedArray;
    }

    public void printError(String str) {
        @SuppressLint("WrongConstant") View view = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_activity_settings_error, null);
        this.error = new Dialog(view.getContext(), R.style.DialogTheme);
        this.error.setContentView(view);
        ((TextView) this.error.findViewById(R.id.settingsActivityErrorDialogTitle)).setTypeface(this.fadeFont);
        TextView msg = (TextView) this.error.findViewById(R.id.settingsActivityErrorDialogText);
        msg.setTypeface(this.fadeFont);
        msg.setText(str);
        ((Button) this.error.findViewById(R.id.settingsActivityErrorDialogButton)).setTypeface(this.fadeFont);
        this.error.setCanceledOnTouchOutside(true);
        this.error.show();
    }

    private void storeCalibrate(Double dev, Double av) {
        Editor editor = this.prefs.edit();
        editor.putBoolean("calibrated", true);
        editor.putString("average", av.toString());
        editor.putString("deviation", dev.toString());
        editor.putString("restauration", Double.valueOf(av - (0.1d * av)).toString());
        editor.apply();
        this.settingsTitle.setText(getResources().getString(R.string.calibrate_tab_title_calibrated));
        this.toastMessage.setText(R.string.calibration_succesfully);
        this.toastMessage.show();
    }

    public void toCancelError(View v) {
        this.error.dismiss();
    }

    @SuppressLint("WrongConstant")
    public void getContactData(String id, int t) {
        boolean found;
        RadioButton check;
        getContactQueries(id);
        View view = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_activity_settings_contact_form, null);
        this.contactForm = new Dialog(view.getContext(), R.style.DialogThemeMain);
        this.contactForm.setContentView(view);
        this.contactForm.getWindow().setSoftInputMode(3);
        TextView title = (TextView) this.contactForm.findViewById(R.id.contactFormTitleText);
        title.setText(t);
        title.setTypeface(this.fadeFont);
        ((TextView) this.contactForm.findViewById(R.id.contactFormLabelName)).setTypeface(this.fadeFont);
        ((TextView) this.contactForm.findViewById(R.id.contactFormLabelPhone)).setTypeface(this.fadeFont);
        ((TextView) this.contactForm.findViewById(R.id.contactFormLabelEmail)).setTypeface(this.fadeFont);
        RadioGroup phones = (RadioGroup) this.contactForm.findViewById(R.id.contactFormPhoneGroup);
        RadioGroup mails = (RadioGroup) this.contactForm.findViewById(R.id.contactFormMailGroup);
        EditText name = (EditText) this.contactForm.findViewById(R.id.contactFormName);
        name.setTypeface(this.fadeFont);
        if (t == R.string.contact_tab_edit_contact_title) {
            name.setText(this.prefs.getString("contactName", ""));
        } else if (this.nameCursor.moveToFirst()) {
            name.setText(this.nameCursor.getString(this.nameCursor.getColumnIndex("display_name")));
        }
        String storedPhone = this.prefs.getString("phoneNumber", "");
        if (this.phoneCursor.moveToFirst()) {
            found = false;
            while (!this.phoneCursor.isAfterLast()) {
                check = new RadioButton(getApplicationContext());
                check.setTypeface(this.fadeFont);
                check.setButtonDrawable(R.drawable.check_box);
                check.setTextColor(Color.parseColor("#bdbdbd"));
                check.setText(this.phoneCursor.getString(this.phoneCursor.getColumnIndex("data1")));
                phones.addView(check);
                if (check.getText().equals(storedPhone)) {
                    check.setChecked(true);
                    found = true;
                }
                this.phoneCursor.moveToNext();
            }
            if (!found) {
                ((RadioButton) phones.getChildAt(0)).setChecked(true);
            }
        } else {
            TextView noPhone = (TextView) this.contactForm.findViewById(R.id.contactFormNoPhoneText);
            noPhone.setVisibility(0);
            noPhone.setTypeface(this.fadeFont);
        }
        String storedEmail = this.prefs.getString("eMail", "");
        if (this.eMailCursor.moveToFirst()) {
            found = false;
            while (!this.eMailCursor.isAfterLast()) {
                check = new RadioButton(getApplicationContext());
                check.setTypeface(this.fadeFont);
                check.setButtonDrawable(R.drawable.check_box);
                check.setText(this.eMailCursor.getString(this.eMailCursor.getColumnIndex("data1")));
                check.setTextColor(Color.parseColor("#bdbdbd"));
                mails.addView(check);

                if (check.getText().equals(storedEmail)) {
                    check.setChecked(true);
                    found = true;
                    check.setTextColor(Color.parseColor("#bdbdbd"));
                }
                this.eMailCursor.moveToNext();
            }
            if (!found) {
                ((RadioButton) mails.getChildAt(0)).setChecked(true);
            }
        } else {
            TextView noEmail = (TextView) this.contactForm.findViewById(R.id.contactFormNoEmailText);
            noEmail.setVisibility(0);
            noEmail.setTypeface(this.fadeFont);
        }
        this.contactForm.show();
    }

    private void getContactQueries(String id) {
        try {
            this.nameCursor = getContentResolver().query(Data.CONTENT_URI, null, "contact_id=?", new String[]{id}, null);
            this.eMailCursor = getContentResolver().query(Email.CONTENT_URI, null, "contact_id=?", new String[]{id}, null);
            this.phoneCursor = getContentResolver().query(Phone.CONTENT_URI, null, "contact_id=?", new String[]{id}, null);
        } catch (Exception e) {
            // EasyTracker.getTracker().sendException(e.getMessage(), false);
            this.toastMessage.setText(R.string.error_unasigned_contact);
            this.toastMessage.show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setContactData(String name, String phone, String mail) {
        this.contactTabAddButton.setText(name);
        if ((phone.equals("") | mail.equals(""))) {
            if (((mail.equals("")) & phone.equals(""))) {
                this.hasPhone = false;
                this.hasEmail = true;
                this.contactTabDataButton.setText(mail);
            } else {
                int i;
                if (phone.equals("")) {
                    i = 0;
                } else {
                    i = 1;
                }
                if ((mail.equals(""))) {
                    this.hasEmail = false;
                    this.hasPhone = true;
                    this.contactTabDataButton.setText(phone);
                } else {
                    this.hasEmail = false;
                    this.hasPhone = false;
                }
            }
        } else {
            this.hasPhone = true;
            this.hasEmail = true;
            this.contactTabDataButton.setText(String.valueOf(phone) + "\n" + mail);
        }

    }

    public void toContactPick(View v) {
        try {
            startActivityForResult(new Intent("android.intent.action.PICK", Contacts.CONTENT_URI), PICK_CONTACT);
        } catch (Exception e) {
            //EasyTracker.getTracker().sendException(e.getMessage(), false);
            this.toastMessage.setText(R.string.error_pick_contact);
            this.toastMessage.show();
        }
    }

    public void toEditContact(View v) {
        String id = this.prefs.getString("contactId", "-1");
        if (!id.equals("-1")) {
            getContactData(id, R.string.contact_tab_edit_contact_title);
            return;
        }
        this.toastMessage.setText(R.string.error_unasigned_contact);
        this.toastMessage.show();
    }

    @SuppressLint("WrongConstant")
    public void toSaveContact(View v) {
        Editor editor = this.prefs.edit();
        EditText name = (EditText) this.contactForm.findViewById(R.id.contactFormName);
        RadioGroup phone = (RadioGroup) this.contactForm.findViewById(R.id.contactFormPhoneGroup);
        RadioGroup email = (RadioGroup) this.contactForm.findViewById(R.id.contactFormMailGroup);
        if (((TextView) this.contactForm.findViewById(R.id.contactFormTitleText)).getText().toString().equals(getResources().getString(R.string.contact_tab_new_contact_title))) {
            editor.putString("contactId", this.newContactId);
            this.contactAdapter = new MyListAdapter((Context) this, new boolean[3], 1, this.fadeFont);
            editor.putBoolean("contactSmsMode", false);
            editor.putBoolean("contactPhonecallMode", false);
            editor.putBoolean("contactEmailMode", false);
            editor.putBoolean("testMode", false);
            this.contactTabDataButton.setVisibility(0);
        }
        String sName = name.getText().toString();
        String sPhone = "";
        String sEmail = "";
        RadioButton selPhone = (RadioButton) phone.findViewById(phone.getCheckedRadioButtonId());
        RadioButton selEmail = (RadioButton) email.findViewById(email.getCheckedRadioButtonId());
        if (selPhone != null) {
            sPhone = selPhone.getText().toString();
        }
        if (selEmail != null) {
            sEmail = selEmail.getText().toString();
        }
        editor.putString("phoneNumber", sPhone);
        editor.putString("eMail", sEmail);
        editor.putString("contactName", sName);
        editor.apply();
        setContactData(sName, sPhone, sEmail);
        this.contactForm.dismiss();
    }

    private void setModeText() {
        switch (this.prefs.getInt("sensibility", 1)) {
            case 0:
                this.modeTabTitle.setText(getResources().getString(R.string.mode_tab_text_high_sensibility_title));
                this.modeTabText.setText(getResources().getString(R.string.mode_tab_text_high_sensibility_text));
                return;
            case 1:
                this.modeTabTitle.setText(getResources().getString(R.string.mode_tab_text_normal_sensibility_title));
                this.modeTabText.setText(getResources().getString(R.string.mode_tab_text_normal_sensibility_text));
                return;
            case 2:
                this.modeTabTitle.setText(getResources().getString(R.string.mode_tab_text_low_sensibility_title));
                this.modeTabText.setText(getResources().getString(R.string.mode_tab_text_low_sensibility_text));
                return;
            default:
                return;
        }
    }

    private void setToneTitle() {
        if (this.prefs.getString("toneTitle", "noTone").equals("noTone")) {
            Uri tone = RingtoneManager.getValidRingtoneUri(getApplicationContext());
            Editor audioEditor = this.prefs.edit();
            if (tone != null) {
                audioEditor.putString("toneUri", tone.toString());
                audioEditor.putString("toneTitle", RingtoneManager.getRingtone(getApplicationContext(), tone).getTitle(getApplicationContext()));
            } else {
                audioEditor.putString("toneUri", "null");
                audioEditor.putString("toneTitle", getResources().getString(R.string.silent));
            }
            audioEditor.apply();
            setToneTitle();
            return;
        }
        this.toneButton.setText(checkLenght(this.prefs.getString("toneTitle", "")));
    }

    public void toRingtonePick(View v) {
        try {
            Intent pick = new Intent("android.intent.action.RINGTONE_PICKER");
            pick.putExtra("android.intent.extra.ringtone.TYPE", 7);
            pick.putExtra("android.intent.extra.ringtone.TITLE", "Seleccione tono de alerta");
            this.prefs.getString("toneUri", "null");
            pick.putExtra("android.intent.extra.ringtone.EXISTING_URI", Uri.parse(this.prefs.getString("toneUri", "null")));
            startActivityForResult(pick, PICK_AUDIO);
        } catch (Exception e) {
            this.toastMessage.setText(R.string.error_pick_tone);
            this.toastMessage.show();
        }
    }
}

