package com.example.loginscreen.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.loginscreen.R;
import com.example.loginscreen.adapters.MedicationColorAdapter;
import com.example.loginscreen.adapters.MedicineTypeAdapter;
import com.example.loginscreen.bean.DataHolder;
import com.example.loginscreen.bean.MedicationColorBean;
import com.example.loginscreen.bean.MedicineTypeBean;
import com.example.loginscreen.bean.SaveMedicineInfoBean;
import com.example.loginscreen.component.MyGridView;
import com.example.loginscreen.db.DatabaseHelper;
import com.example.loginscreen.util.AppUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewMedicine extends BaseActivity {

    private static final String TAG = "NewMedicine";

    ProgressDialog progressDialog;
    MyGridView medicationType, medicationColor;
    MedicineTypeAdapter medicineTypeAdapter;
    MedicationColorAdapter medicationColorAdapter;
    List<MedicineTypeBean> medicineTypeBeans, medicineWhite,
            medicineYellow, medicineRed, medicineTeal,
            medicineGreen, medicineBrown, medicinePink,
            medicineOrange, medicineBlack, medicinePurple,
            medicineBlue, medicineOlive;

    List<MedicationColorBean> medicationColorBeans;
    Context mContext;
    TextView firstTime, secondTime, thirdTime, endDate, startDate;
    AppCompatSpinner scheduleSelectDays, reminderTimeList;
    ArrayAdapter<String> stringArrayAdapter, scheduleStringArrayAdapter;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myEndCalendar = Calendar.getInstance();
    DatePickerDialog startDatePick, endDatePick;
    int mYear, mMonth, mDay;
    long firstTimeMillis, secondTimeMillis, thirdTimeMillis;
    SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
    NumberPicker pickSelectedDays;
    LinearLayout daysofWeekSelection;
    EditText medicationName;
    long startDateMillis, endDateMillis, creationDateMillis;
    Button saveMedicationButton, moBtn, tuBtn, weBtn, thBtn, frBtn, saBtn, suBtn;
    SaveMedicineInfoBean saveMedicineInfoBean;
    Toolbar toolbar;
    private String colorStr = "White";

    private final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);
        mContext = this;
        DataHolder.getInstance().defaultSetDayOfWeek();
        creationDateMillis = System.currentTimeMillis();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("New Medicine");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        saveMedicineInfoBean = new SaveMedicineInfoBean();

        medicationType = findViewById(R.id.medicationType);
        medicationColor = findViewById(R.id.medicationColor);
        reminderTimeList = findViewById(R.id.reminderTimeList);
        scheduleSelectDays = findViewById(R.id.scheduleSelectDays);
        medicationName = findViewById(R.id.medicationName);
        firstTime = findViewById(R.id.firstTime);
        secondTime = findViewById(R.id.secondTime);
        thirdTime = findViewById(R.id.thirdTime);

        endDate = findViewById(R.id.endDate);
        startDate = findViewById(R.id.startDate);
        Date currentDate = new Date();/// if user does not change today "text" set current time

        startDate.setText(format.format(currentDate.getTime()));
        pickSelectedDays = findViewById(R.id.pickSelectedDays);
        daysofWeekSelection = findViewById(R.id.daysofWeekSelection);

        moBtn = findViewById(R.id.moBtn);
        tuBtn = findViewById(R.id.tuBtn);
        weBtn = findViewById(R.id.weBtn);
        thBtn = findViewById(R.id.thBtn);
        frBtn = findViewById(R.id.frBtn);
        saBtn = findViewById(R.id.saBtn);
        suBtn = findViewById(R.id.suBtn);

        saveMedicationButton = findViewById(R.id.saveMedicationButton);

        pickSelectedDays.setMaxValue(365);
        pickSelectedDays.setMinValue(1);

        pickSelectedDays.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                saveMedicineInfoBean.setActive_nth_day(newVal);
                saveMedicineInfoBean.setActive_monday(0);
                saveMedicineInfoBean.setActive_tuesday(0);
                saveMedicineInfoBean.setActive_wednesday(0);
                saveMedicineInfoBean.setActive_thursday(0);
                saveMedicineInfoBean.setActive_friday(0);
                saveMedicineInfoBean.setActive_saturday(0);
                saveMedicineInfoBean.setActive_sunday(0);
            }
        });

        medicationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveMedicineInfoBean.setName(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        medicationColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onGetSelectedColorMedicine(medicationColorBeans.get(position).getColor());
                saveMedicineInfoBean.setColor(position);
            }
        });

        medicationType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveMedicineInfoBean.setType(position);
                onSelectEventType(position);
            }
        });

        List<String> list = new ArrayList<>();
        list.add("Once a day");
        list.add("3 times a day");
        stringArrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderTimeList.setAdapter(stringArrayAdapter);

        List<String> listSchedule = new ArrayList<>();
        listSchedule.add("Selected days");
        listSchedule.add("Days interval");
        scheduleStringArrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, listSchedule){
            @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView text = (TextView) view.findViewById(android.R.id.text1);
            text.setTextColor(Color.BLACK);
            return view;
        }};
        scheduleStringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scheduleSelectDays.setAdapter(scheduleStringArrayAdapter);

        mYear = myCalendar.get(Calendar.YEAR);
        mMonth = myCalendar.get(Calendar.MONTH);
        mDay = myCalendar.get(Calendar.DAY_OF_MONTH);

        reminderTimeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0) {
                    saveMedicineInfoBean.setReminderType("3 times a day");
                    secondTime.setVisibility(View.VISIBLE);
                    thirdTime.setVisibility(View.VISIBLE);
                } else {
                    saveMedicineInfoBean.setReminderType("Once a day");
                    secondTime.setVisibility(View.GONE);
                    thirdTime.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        scheduleSelectDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0) {
                    saveMedicineInfoBean.setScheduleType("Days Interval");
                    pickSelectedDays.setVisibility(View.VISIBLE);
                    daysofWeekSelection.setVisibility(View.GONE);
                } else {
                    saveMedicineInfoBean.setScheduleType("Selected Days");
                    pickSelectedDays.setVisibility(View.GONE);
                    daysofWeekSelection.setVisibility(View.VISIBLE);
                    moBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                    tuBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                    weBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                    thBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                    frBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                    saBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                    suBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        firstTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 8;
                int minute = 0;
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = selectedHour + ":" + selectedMinute;
                        firstTimeMillis = getTimeAccordingTo24Format(selectedHour, selectedMinute);
                        firstTime.setText(getTime(time));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        secondTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 9;
                int minute = 0;
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = selectedHour + ":" + selectedMinute;
                        secondTimeMillis = getTimeAccordingTo24Format(selectedHour, selectedMinute);
                        secondTime.setText(getTime(time));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        thirdTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 10;
                int minute = 0;
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = selectedHour + ":" + selectedMinute;
                        thirdTimeMillis = getTimeAccordingTo24Format(selectedHour, selectedMinute);
                        thirdTime.setText(getTime(time));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePick = new DatePickerDialog(mContext, myStartDateListener, mYear, mMonth, mDay);
                startDatePick.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                startDatePick.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePick = new DatePickerDialog(mContext, myEndDateListener, mYear, mMonth, mDay + 2);
                endDatePick.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                endDatePick.show();
            }
        });


        moBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveMedicineInfoBean.getActive_monday() != 0) {
                    saveMedicineInfoBean.setActive_monday(0);
                    DataHolder.getInstance().setDayOfWeek("Monday");
                    moBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                } else {
                    saveMedicineInfoBean.setActive_monday(1);
                    DataHolder.getInstance().removeDayOfWeek("Monday");
                    moBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_unselected));
                }
                saveMedicineInfoBean.setActive_nth_day(0);
            }
        });

        tuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveMedicineInfoBean.getActive_tuesday() != 0) {
                    saveMedicineInfoBean.setActive_tuesday(0);
                    DataHolder.getInstance().setDayOfWeek("Tuesday");
                    tuBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                } else {
                    saveMedicineInfoBean.setActive_tuesday(1);
                    DataHolder.getInstance().removeDayOfWeek("Tuesday");
                    tuBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_unselected));

                }
                saveMedicineInfoBean.setActive_nth_day(0);
            }
        });

        weBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveMedicineInfoBean.getActive_wednesday() != 0) {
                    saveMedicineInfoBean.setActive_wednesday(0);
                    DataHolder.getInstance().setDayOfWeek("Wednesday");
                    weBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                } else {
                    saveMedicineInfoBean.setActive_wednesday(1);
                    DataHolder.getInstance().removeDayOfWeek("Wednesday");
                    weBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_unselected));
                }
                saveMedicineInfoBean.setActive_nth_day(0);
            }
        });

        thBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveMedicineInfoBean.getActive_thursday() != 0) {
                    saveMedicineInfoBean.setActive_thursday(0);
                    DataHolder.getInstance().setDayOfWeek("Thursday");
                    thBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                } else {
                    saveMedicineInfoBean.setActive_thursday(1);
                    DataHolder.getInstance().removeDayOfWeek("Thursday");
                    thBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_unselected));
                }
                saveMedicineInfoBean.setActive_nth_day(0);
            }
        });

        frBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveMedicineInfoBean.getActive_friday() != 0) {
                    saveMedicineInfoBean.setActive_friday(0);
                    DataHolder.getInstance().setDayOfWeek("Friday");
                    frBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                } else {
                    saveMedicineInfoBean.setActive_friday(1);
                    DataHolder.getInstance().removeDayOfWeek("Friday");
                    frBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_unselected));
                }
                saveMedicineInfoBean.setActive_nth_day(0);
            }
        });

        saBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveMedicineInfoBean.getActive_saturday() != 0) {
                    saveMedicineInfoBean.setActive_saturday(0);
                    DataHolder.getInstance().setDayOfWeek("Saturday");
                    saBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                } else {
                    saveMedicineInfoBean.setActive_saturday(1);
                    DataHolder.getInstance().removeDayOfWeek("Saturday");
                    saBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_unselected));
                }
                saveMedicineInfoBean.setActive_nth_day(0);
            }
        });

        suBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveMedicineInfoBean.getActive_sunday() != 0) {
                    saveMedicineInfoBean.setActive_sunday(0);
                    DataHolder.getInstance().setDayOfWeek("Sunday");
                    suBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_selected));
                } else {
                    saveMedicineInfoBean.setActive_sunday(1);
                    DataHolder.getInstance().removeDayOfWeek("Sunday");
                    suBtn.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_unselected));
                }
                saveMedicineInfoBean.setActive_nth_day(0);
            }
        });

        saveMedicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(medicationName.getText().toString().trim())) {
                    saveMedicineInfoBean.setCreationDateMillis(creationDateMillis);
                    if (startDateMillis == 0) {
                        startDateMillis = System.currentTimeMillis();
                    }
                    saveMedicineInfoBean.setStartDateMillis(startDateMillis);
                    if (endDateMillis == 0) {
                        endDateMillis = System.currentTimeMillis();
                    }
                    saveMedicineInfoBean.setEndDateMillis(endDateMillis);

                    new SaveMedicine().execute();
                } else {
                    Toast.makeText(mContext, "Please enter medication name..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        onGetDefaultData();
    }

    private String getDayName(long dateInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    private ArrayList<Long> getFutureDates() {
        ArrayList<Long> futureDates;
        long tempStartDate = startDateMillis;
        int differenceBetweenTwoDates = (int) ((endDateMillis - startDateMillis) / ONE_DAY_MILLIS);

        if (differenceBetweenTwoDates != 0) {
            if ("Selected Days".equalsIgnoreCase(saveMedicineInfoBean.getScheduleType())) {
                futureDates = new ArrayList<>();
                futureDates.add(startDateMillis);
                for (int i = 0; i < differenceBetweenTwoDates; i++) {
                    tempStartDate = tempStartDate + ONE_DAY_MILLIS;
                    String dayName = getDayName(tempStartDate);
                    if (DataHolder.getInstance().isDayOfWeek(dayName)) {
                        futureDates.add(tempStartDate);
                    }
                }
            } else {
                futureDates = new ArrayList<>();
                futureDates.add(startDateMillis);
                int nThDay = saveMedicineInfoBean.getActive_nth_day();
                if (nThDay != 0) {
                    differenceBetweenTwoDates = differenceBetweenTwoDates / nThDay;
                    for (int i = 0; i < differenceBetweenTwoDates; i++) {
                        tempStartDate = tempStartDate + (nThDay * ONE_DAY_MILLIS);
                        futureDates.add(tempStartDate);
                    }
                }

            }
        } else {
            futureDates = new ArrayList<>();
            futureDates.add(startDateMillis);
        }
        return futureDates;
    }

    private long getTimeAccordingTo24Format(int hour, int minute) {
        long hourMillis = hour * 60 * 60 * 1000;
        long minuteMillis = minute * 60 * 1000;
        return (hourMillis + minuteMillis);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private DatePickerDialog.OnDateSetListener myStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(year, monthOfYear, dayOfMonth);
            myCalendar.set(Calendar.HOUR_OF_DAY, 0);
            myCalendar.set(Calendar.MINUTE, 0);
            myCalendar.set(Calendar.SECOND, 0);
            myCalendar.set(Calendar.MILLISECOND, 0);
            startDateMillis = myCalendar.getTimeInMillis();
            startDate.setText(format.format(myCalendar.getTime()));
        }
    };

    private DatePickerDialog.OnDateSetListener myEndDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {
            myEndCalendar.set(year, monthOfYear, dayOfMonth);
            myEndCalendar.set(Calendar.HOUR_OF_DAY, 0);
            myEndCalendar.set(Calendar.MINUTE, 0);
            myEndCalendar.set(Calendar.SECOND, 0);
            myEndCalendar.set(Calendar.MILLISECOND, 0);
            endDateMillis = myEndCalendar.getTimeInMillis();
            endDate.setText(format.format(myEndCalendar.getTime()));
        }
    };

    private ArrayList<Long> dateList;

    ArrayList<Long> timeList = new ArrayList<>();

    private void registerAlarm(ArrayList<Long> dateList, ArrayList<Long> timeList, int lastAlarmId){
        ArrayList<Long> futuresDate = new ArrayList<>();
        long dateAndTime;
        for(long date: dateList){
            for(long time: timeList){
                dateAndTime = date + time;
                futuresDate.add(dateAndTime);
            }
        }
        AppUtil.start(NewMedicine.this, futuresDate, lastAlarmId);
    }

    class SaveAlarmTime extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null || !progressDialog.isShowing()) {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setTitle("Saving");
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
            int lastAlarmId = databaseHelper.getLastInsertedAlarmId();
            if (lastAlarmId == 0) {
                return false;
            }
            dateList = getFutureDates();
            boolean flag = databaseHelper.onSaveAlarmTime(timeList, lastAlarmId, dateList);
            if(flag){
                registerAlarm(dateList, timeList, lastAlarmId);
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Intent returnIntent = new Intent();
            if (aBoolean) {
                Toast.makeText(mContext, "Saved successfully", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                Toast.makeText(mContext, "Something went wrong while saving the data", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        }
    }


    class SaveMedicine extends AsyncTask<Void, Boolean, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Saving");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
            return databaseHelper.onSaveMedicineInfo(saveMedicineInfoBean);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            timeList.add(firstTimeMillis);
            if ("3 times a day".equalsIgnoreCase(saveMedicineInfoBean.getReminderType())) {
                timeList.add(secondTimeMillis);
                timeList.add(thirdTimeMillis);
            }

            if (aBoolean) {
                //Toast.makeText(mContext, "Medicine saved successfully", Toast.LENGTH_SHORT).show();
                new SaveAlarmTime().execute();

            } else {
                Toast.makeText(mContext, "Something went wrong while saving the data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getTime(String time) {
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = fmt.parse(time);
        } catch (ParseException e) {
            AppUtil.log(TAG, "getTime > ParseException > " + e.getMessage(), e);
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");
        return fmtOut.format(date);
    }

    private void onGetDefaultData() {
        medicineTypeBeans = new ArrayList<>();
        medicineTypeBeans.add(new MedicineTypeBean(R.drawable.medication_tablet_round_white, true));
        medicineTypeBeans.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_white, false));
        medicineTypeBeans.add(new MedicineTypeBean(R.drawable.medication_capsule_white, false));
        medicineTypeBeans.add(new MedicineTypeBean(R.drawable.medication_injection_white, false));
        medicineTypeBeans.add(new MedicineTypeBean(R.drawable.medication_inhaler_white, false));
        medicineTypeBeans.add(new MedicineTypeBean(R.drawable.medication_salve_white, false));
        medicineTypeBeans.add(new MedicineTypeBean(R.drawable.medication_drop_white, false));
        medicineTypeBeans.add(new MedicineTypeBean(R.drawable.medication_suppository_white, false));

        medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicineTypeBeans);
        medicationType.setAdapter(medicineTypeAdapter);

        medicationColorBeans = new ArrayList<>();
        medicationColorBeans.add(new MedicationColorBean("White", R.drawable.rounded_rectangle_white, true));
        medicationColorBeans.add(new MedicationColorBean("Yellow", R.drawable.rounded_rectangle_yellow, false));
        medicationColorBeans.add(new MedicationColorBean("Red", R.drawable.rounded_rectangle_red, false));
        medicationColorBeans.add(new MedicationColorBean("Pink", R.drawable.rounded_rectangle_pink, false));
        medicationColorBeans.add(new MedicationColorBean("Teal", R.drawable.rounded_rectangle_teal, false));
        medicationColorBeans.add(new MedicationColorBean("Green", R.drawable.rounded_rectangle_green, false));
        medicationColorBeans.add(new MedicationColorBean("Black", R.drawable.rounded_rectangle_black, false));
        medicationColorBeans.add(new MedicationColorBean("Orange", R.drawable.rounded_rectangle_orange, false));
        medicationColorBeans.add(new MedicationColorBean("Brown", R.drawable.rounded_rectangle_brown, false));
        medicationColorBeans.add(new MedicationColorBean("Purple", R.drawable.rounded_rectangle_purple, false));
        medicationColorBeans.add(new MedicationColorBean("Blue", R.drawable.rounded_rectangle_blue, false));
        medicationColorBeans.add(new MedicationColorBean("Olive", R.drawable.rounded_rectangle_olive, false));

        medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
        medicationColor.setAdapter(medicationColorAdapter);

        firstTimeMillis = getTimeAccordingTo24Format(8, 0);
        secondTimeMillis = getTimeAccordingTo24Format(9, 0);
        thirdTimeMillis = getTimeAccordingTo24Format(10, 0);

        saveMedicineInfoBean.setColor(0);
        saveMedicineInfoBean.setType(0);
        saveMedicineInfoBean.setCreationDateMillis(creationDateMillis);
        saveMedicineInfoBean.setReminderType("Once a day");
        saveMedicineInfoBean.setScheduleType("Selected Days");
    }

    void onGetWhiteMedicine() {
        medicineWhite = new ArrayList<>();
        medicineWhite.add(new MedicineTypeBean(R.drawable.medication_tablet_round_white, false));
        medicineWhite.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_white, false));
        medicineWhite.add(new MedicineTypeBean(R.drawable.medication_capsule_white, false));
        medicineWhite.add(new MedicineTypeBean(R.drawable.medication_injection_white, false));
        medicineWhite.add(new MedicineTypeBean(R.drawable.medication_inhaler_white, false));
        medicineWhite.add(new MedicineTypeBean(R.drawable.medication_salve_white, false));
        medicineWhite.add(new MedicineTypeBean(R.drawable.medication_drop_white, false));
        medicineWhite.add(new MedicineTypeBean(R.drawable.medication_suppository_white, false));
    }

    void onGetYellowMedicine() {
        medicineYellow = new ArrayList<>();
        medicineYellow.add(new MedicineTypeBean(R.drawable.medication_tablet_round_yellow, false));
        medicineYellow.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_yellow, false));
        medicineYellow.add(new MedicineTypeBean(R.drawable.medication_capsule_yellow, false));
        medicineYellow.add(new MedicineTypeBean(R.drawable.medication_injection_yellow, false));
        medicineYellow.add(new MedicineTypeBean(R.drawable.medication_inhaler_yellow, false));
        medicineYellow.add(new MedicineTypeBean(R.drawable.medication_salve_yellow, false));
        medicineYellow.add(new MedicineTypeBean(R.drawable.medication_drop_yellow, false));
        medicineYellow.add(new MedicineTypeBean(R.drawable.medication_suppository_yellow, false));
    }

    void onGetRedMedicine() {
        medicineRed = new ArrayList<>();
        medicineRed.add(new MedicineTypeBean(R.drawable.medication_tablet_round_red, false));
        medicineRed.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_red, false));
        medicineRed.add(new MedicineTypeBean(R.drawable.medication_capsule_red, false));
        medicineRed.add(new MedicineTypeBean(R.drawable.medication_injection_red, false));
        medicineRed.add(new MedicineTypeBean(R.drawable.medication_inhaler_red, false));
        medicineRed.add(new MedicineTypeBean(R.drawable.medication_salve_red, false));
        medicineRed.add(new MedicineTypeBean(R.drawable.medication_drop_red, false));
        medicineRed.add(new MedicineTypeBean(R.drawable.medication_suppository_red, false));
    }

    void onGetPinkMedicine() {
        medicinePink = new ArrayList<>();
        medicinePink.add(new MedicineTypeBean(R.drawable.medication_tablet_round_pink, false));
        medicinePink.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_pink, false));
        medicinePink.add(new MedicineTypeBean(R.drawable.medication_capsule_pink, false));
        medicinePink.add(new MedicineTypeBean(R.drawable.medication_injection_pink, false));
        medicinePink.add(new MedicineTypeBean(R.drawable.medication_inhaler_pink, false));
        medicinePink.add(new MedicineTypeBean(R.drawable.medication_salve_pink, false));
        medicinePink.add(new MedicineTypeBean(R.drawable.medication_drop_pink, false));
        medicinePink.add(new MedicineTypeBean(R.drawable.medication_suppository_pink, false));
    }

    void onGetTealMedicine() {
        medicineTeal = new ArrayList<>();
        medicineTeal.add(new MedicineTypeBean(R.drawable.medication_tablet_round_teal, false));
        medicineTeal.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_teal, false));
        medicineTeal.add(new MedicineTypeBean(R.drawable.medication_capsule_teal, false));
        medicineTeal.add(new MedicineTypeBean(R.drawable.medication_injection_teal, false));
        medicineTeal.add(new MedicineTypeBean(R.drawable.medication_inhaler_teal, false));
        medicineTeal.add(new MedicineTypeBean(R.drawable.medication_salve_teal, false));
        medicineTeal.add(new MedicineTypeBean(R.drawable.medication_drop_teal, false));
        medicineTeal.add(new MedicineTypeBean(R.drawable.medication_suppository_teal, false));
    }

    void onGetGreenMedicine() {
        medicineGreen = new ArrayList<>();
        medicineGreen.add(new MedicineTypeBean(R.drawable.medication_tablet_round_green, false));
        medicineGreen.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_green, false));
        medicineGreen.add(new MedicineTypeBean(R.drawable.medication_capsule_green, false));
        medicineGreen.add(new MedicineTypeBean(R.drawable.medication_injection_green, false));
        medicineGreen.add(new MedicineTypeBean(R.drawable.medication_inhaler_green, false));
        medicineGreen.add(new MedicineTypeBean(R.drawable.medication_salve_green, false));
        medicineGreen.add(new MedicineTypeBean(R.drawable.medication_drop_green, false));
        medicineGreen.add(new MedicineTypeBean(R.drawable.medication_suppository_green, false));
    }

    void onGetBlackMedicine() {
        medicineBlack = new ArrayList<>();
        medicineBlack.add(new MedicineTypeBean(R.drawable.medication_tablet_round_black, false));
        medicineBlack.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_black, false));
        medicineBlack.add(new MedicineTypeBean(R.drawable.medication_capsule_black, false));
        medicineBlack.add(new MedicineTypeBean(R.drawable.medication_injection_black, false));
        medicineBlack.add(new MedicineTypeBean(R.drawable.medication_inhaler_black, false));
        medicineBlack.add(new MedicineTypeBean(R.drawable.medication_salve_black, false));
        medicineBlack.add(new MedicineTypeBean(R.drawable.medication_drop_black, false));
        medicineBlack.add(new MedicineTypeBean(R.drawable.medication_suppository_black, false));
    }

    void onGetOrangeMedicine() {
        medicineOrange = new ArrayList<>();
        medicineOrange.add(new MedicineTypeBean(R.drawable.medication_tablet_round_orange, false));
        medicineOrange.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_orange, false));
        medicineOrange.add(new MedicineTypeBean(R.drawable.medication_capsule_orange, false));
        medicineOrange.add(new MedicineTypeBean(R.drawable.medication_injection_orange, false));
        medicineOrange.add(new MedicineTypeBean(R.drawable.medication_inhaler_orange, false));
        medicineOrange.add(new MedicineTypeBean(R.drawable.medication_salve_orange, false));
        medicineOrange.add(new MedicineTypeBean(R.drawable.medication_drop_orange, false));
        medicineOrange.add(new MedicineTypeBean(R.drawable.medication_suppository_orange, false));
    }

    void onGetBrownMedicine() {
        medicineBrown = new ArrayList<>();
        medicineBrown.add(new MedicineTypeBean(R.drawable.medication_tablet_round_brown, false));
        medicineBrown.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_brown, false));
        medicineBrown.add(new MedicineTypeBean(R.drawable.medication_capsule_brown, false));
        medicineBrown.add(new MedicineTypeBean(R.drawable.medication_injection_brown, false));
        medicineBrown.add(new MedicineTypeBean(R.drawable.medication_inhaler_brown, false));
        medicineBrown.add(new MedicineTypeBean(R.drawable.medication_salve_brown, false));
        medicineBrown.add(new MedicineTypeBean(R.drawable.medication_drop_brown, false));
        medicineBrown.add(new MedicineTypeBean(R.drawable.medication_suppository_brown, false));
    }

    void onGetPurpleMedicine() {
        medicinePurple = new ArrayList<>();
        medicinePurple.add(new MedicineTypeBean(R.drawable.medication_tablet_round_purple, false));
        medicinePurple.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_purple, false));
        medicinePurple.add(new MedicineTypeBean(R.drawable.medication_capsule_purple, false));
        medicinePurple.add(new MedicineTypeBean(R.drawable.medication_injection_purple, false));
        medicinePurple.add(new MedicineTypeBean(R.drawable.medication_inhaler_purple, false));
        medicinePurple.add(new MedicineTypeBean(R.drawable.medication_salve_purple, false));
        medicinePurple.add(new MedicineTypeBean(R.drawable.medication_drop_purple, false));
        medicinePurple.add(new MedicineTypeBean(R.drawable.medication_suppository_purple, false));
    }

    void onGetBlueMedicine() {
        medicineBlue = new ArrayList<>();
        medicineBlue.add(new MedicineTypeBean(R.drawable.medication_tablet_round_blue, false));
        medicineBlue.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_blue, false));
        medicineBlue.add(new MedicineTypeBean(R.drawable.medication_capsule_blue, false));
        medicineBlue.add(new MedicineTypeBean(R.drawable.medication_injection_blue, false));
        medicineBlue.add(new MedicineTypeBean(R.drawable.medication_inhaler_blue, false));
        medicineBlue.add(new MedicineTypeBean(R.drawable.medication_salve_blue, false));
        medicineBlue.add(new MedicineTypeBean(R.drawable.medication_drop_blue, false));
        medicineBlue.add(new MedicineTypeBean(R.drawable.medication_suppository_blue, false));
    }

    void onGetOliveMedicine() {
        medicineOlive = new ArrayList<>();
        medicineOlive.add(new MedicineTypeBean(R.drawable.medication_tablet_round_olive, false));
        medicineOlive.add(new MedicineTypeBean(R.drawable.medication_tablet_oval_olive, false));
        medicineOlive.add(new MedicineTypeBean(R.drawable.medication_capsule_olive, false));
        medicineOlive.add(new MedicineTypeBean(R.drawable.medication_injection_olive, false));
        medicineOlive.add(new MedicineTypeBean(R.drawable.medication_inhaler_olive, false));
        medicineOlive.add(new MedicineTypeBean(R.drawable.medication_salve_olive, false));
        medicineOlive.add(new MedicineTypeBean(R.drawable.medication_drop_olive, false));
        medicineOlive.add(new MedicineTypeBean(R.drawable.medication_suppository_olive, false));
    }

    public void onSelectEventType(int position) {
        for (int i = 0; i < 8; i++) {
            if (i == position) {
                medicineTypeBeans.get(i).setChecked(true);
            } else {
                medicineTypeBeans.get(i).setChecked(false);
            }
        }
        onGetSelectedColorMedicine(colorStr);
    }

    private void onGetSelectedColorMedicine(String color) {
        switch (color) {
            case "White": {
                colorStr = "White";
                onGetWhiteMedicine();
                for (int i = 0; i < 8; i++) {
                    medicineWhite.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("White")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicineWhite);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
            case "Yellow": {
                colorStr = "Yellow";
                onGetYellowMedicine();
                for (int i = 0; i < 8; i++) {
                    medicineYellow.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("Yellow")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicineYellow);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
            case "Red": {
                colorStr = "Red";
                onGetRedMedicine();
                for (int i = 0; i < 8; i++) {
                    medicineRed.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("Red")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicineRed);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
            case "Pink": {
                colorStr = "Pink";
                onGetPinkMedicine();
                for (int i = 0; i < 8; i++) {
                    medicinePink.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("Pink")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicinePink);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
            case "Teal": {
                colorStr = "Teal";
                onGetTealMedicine();
                for (int i = 0; i < 8; i++) {
                    medicineTeal.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("Teal")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicineTeal);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
            case "Green": {
                colorStr = "Green";
                onGetGreenMedicine();
                for (int i = 0; i < 8; i++) {
                    medicineGreen.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("Green")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicineGreen);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
            case "Black": {
                colorStr = "Black";
                onGetBlackMedicine();
                for (int i = 0; i < 8; i++) {
                    medicineBlack.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("Black")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicineBlack);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
            case "Orange": {
                colorStr = "Orange";
                onGetOrangeMedicine();
                for (int i = 0; i < 8; i++) {
                    medicineOrange.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("Orange")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicineOrange);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
            case "Brown": {
                colorStr = "Brown";
                onGetBrownMedicine();
                for (int i = 0; i < 8; i++) {
                    medicineBrown.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("Brown")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicineBrown);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
            case "Purple": {
                colorStr = "Purple";
                onGetPurpleMedicine();
                for (int i = 0; i < 8; i++) {
                    medicinePurple.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("Purple")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicinePurple);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
            case "Blue": {
                colorStr = "Blue";
                onGetBlueMedicine();
                for (int i = 0; i < 8; i++) {
                    medicineBlue.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("Blue")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicineBlue);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
            case "Olive": {
                colorStr = "Olive";
                onGetOliveMedicine();
                for (int i = 0; i < 8; i++) {
                    medicineOlive.get(i).setChecked(medicineTypeBeans.get(i).isChecked());
                }
                for (int i = 0; i < 12; i++) {
                    if (medicationColorBeans.get(i).getColor().equalsIgnoreCase("Olive")) {
                        medicationColorBeans.get(i).setChecked(true);
                    } else {
                        medicationColorBeans.get(i).setChecked(false);
                    }
                }
                medicationColorAdapter = new MedicationColorAdapter(mContext, medicationColorBeans);
                medicationColor.setAdapter(medicationColorAdapter);
                medicineTypeAdapter = new MedicineTypeAdapter(mContext, medicineOlive);
                medicationType.setAdapter(medicineTypeAdapter);
                break;
            }
        }
    }
}
