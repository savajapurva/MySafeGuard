package com.example.loginscreen.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.loginscreen.bean.MasterObject;
import com.example.loginscreen.bean.SaveMedicineInfoBean;
import com.example.loginscreen.util.AppUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "MySafeDB";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] CREATE_TABLE_ARRAY = DBConstants.CREATE_TBL_ARRAY;
        for (String table : CREATE_TABLE_ARRAY) {
            db.execSQL(table);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean onSaveMedicineInfo(SaveMedicineInfoBean saveMedicineInfoBean) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("color", saveMedicineInfoBean.getColor());
        contentValues.put("type", saveMedicineInfoBean.getType());
        contentValues.put("name", saveMedicineInfoBean.getName());

        long id = db.insert(DBConstants.TBL_MEDICATION, null, contentValues);

        if (id != -1) {
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("medication_id", getLastInsertedMedicationId());
            contentValues1.put("creation_date", saveMedicineInfoBean.getCreationDateMillis());
            contentValues1.put("start_date", saveMedicineInfoBean.getStartDateMillis());
            contentValues1.put("end_date", saveMedicineInfoBean.getEndDateMillis());
            contentValues1.put("reminderType", saveMedicineInfoBean.getReminderType());
            contentValues1.put("scheduleType", saveMedicineInfoBean.getScheduleType());
            contentValues1.put("active_monday", saveMedicineInfoBean.getActive_monday());
            contentValues1.put("active_tuesday", saveMedicineInfoBean.getActive_tuesday());
            contentValues1.put("active_wednesday", saveMedicineInfoBean.getActive_wednesday());
            contentValues1.put("active_thursday", saveMedicineInfoBean.getActive_thursday());
            contentValues1.put("active_friday ", saveMedicineInfoBean.getActive_friday());
            contentValues1.put("active_saturday", saveMedicineInfoBean.getActive_saturday());
            contentValues1.put("active_sunday", saveMedicineInfoBean.getActive_sunday());
            contentValues1.put("active_nth_day", saveMedicineInfoBean.getActive_nth_day());

            long in = db.insert(DBConstants.TBL_ALARM, null, contentValues1);

            return in != -1;
        } else {
            return false;
        }
    }

    public ArrayList<SaveMedicineInfoBean> getAllMedicineInfo() {
        ArrayList<SaveMedicineInfoBean> saveMedicineInfoBeans = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT al.*, m.* FROM " +
                DBConstants.TBL_ALARM + " al join " +
                DBConstants.TBL_MEDICATION + " m on al.medication_id = m.medication_id;";

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            SaveMedicineInfoBean saveMedicineInfoBean;
            do {
                saveMedicineInfoBean = new SaveMedicineInfoBean();
                saveMedicineInfoBean.setAlarm_id(cursor.getInt(0));
                saveMedicineInfoBean.setMedication_id(cursor.getInt(1));
                saveMedicineInfoBean.setCreationDateMillis(cursor.getLong(2));
                saveMedicineInfoBean.setStartDateMillis(cursor.getLong(3));
                saveMedicineInfoBean.setEndDateMillis(cursor.getLong(4));
                saveMedicineInfoBean.setReminderType(cursor.getString(5));
                saveMedicineInfoBean.setScheduleType(cursor.getString(6));
                saveMedicineInfoBean.setActive_monday(cursor.getInt(7));
                saveMedicineInfoBean.setActive_tuesday(cursor.getInt(8));
                saveMedicineInfoBean.setActive_wednesday(cursor.getInt(9));
                saveMedicineInfoBean.setActive_thursday(cursor.getInt(10));
                saveMedicineInfoBean.setActive_friday(cursor.getInt(11));
                saveMedicineInfoBean.setActive_saturday(cursor.getInt(12));
                saveMedicineInfoBean.setActive_sunday(cursor.getInt(13));
                saveMedicineInfoBean.setActive_nth_day(cursor.getInt(14));
                saveMedicineInfoBean.setColor(cursor.getInt(16));
                saveMedicineInfoBean.setType(cursor.getInt(17));
                saveMedicineInfoBean.setName(cursor.getString(18));
                saveMedicineInfoBeans.add(saveMedicineInfoBean);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return saveMedicineInfoBeans;

    }

    private int getLastInsertedMedicationId() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery("SELECT * FROM " + DBConstants.TBL_MEDICATION + " ORDER BY medication_id DESC LIMIT 1", null);
        int id = 0;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        AppUtil.log(TAG, "getLastInsertedMedicationId: " + id);
        return id;
    }

    public int getLastInsertedAlarmId() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery("SELECT * FROM " + DBConstants.TBL_ALARM + " ORDER BY alarm_id DESC LIMIT 1", null);
        int id = 0;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        AppUtil.log(TAG, "getLastInsertedAlarmId: " + id);
        return id;
    }

    private SaveMedicineInfoBean getSaveMedicineInfoBean(int alarmId) {
        SaveMedicineInfoBean saveMedicineInfoBean = null;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT al.*, m.color, m.type, m.name FROM " + DBConstants.TBL_ALARM + " al join " +
                DBConstants.TBL_MEDICATION + " m on al.medication_id = m.medication_id" +
                " and al.alarm_id = \"" + alarmId + "\";";

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            saveMedicineInfoBean = new SaveMedicineInfoBean();
            saveMedicineInfoBean.setAlarm_id(cursor.getInt(0));
            saveMedicineInfoBean.setMedication_id(cursor.getInt(1));
            saveMedicineInfoBean.setCreationDateMillis(cursor.getLong(2));
            saveMedicineInfoBean.setStartDateMillis(cursor.getLong(3));
            saveMedicineInfoBean.setEndDateMillis(cursor.getLong(4));
            saveMedicineInfoBean.setReminderType(cursor.getString(5));
            saveMedicineInfoBean.setScheduleType(cursor.getString(6));
            saveMedicineInfoBean.setActive_monday(cursor.getInt(7));
            saveMedicineInfoBean.setActive_tuesday(cursor.getInt(8));
            saveMedicineInfoBean.setActive_wednesday(cursor.getInt(9));
            saveMedicineInfoBean.setActive_thursday(cursor.getInt(10));
            saveMedicineInfoBean.setActive_friday(cursor.getInt(11));
            saveMedicineInfoBean.setActive_saturday(cursor.getInt(12));
            saveMedicineInfoBean.setActive_sunday(cursor.getInt(13));
            saveMedicineInfoBean.setActive_nth_day(cursor.getInt(14));
            saveMedicineInfoBean.setColor(cursor.getInt(15));
            saveMedicineInfoBean.setType(cursor.getInt(16));
            saveMedicineInfoBean.setName(cursor.getString(17));
        }
        cursor.close();
        return saveMedicineInfoBean;
    }

    private long getDayStartTime() {
        long currentMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentMillis);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private long getDayEndTime() {
        long currentMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentMillis);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public ArrayList<MasterObject> getMasterObjects() {
        long currentTime = getDayStartTime();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ArrayList<MasterObject> masterList = new ArrayList<>();
        String query = "SELECT af.alarm_id, (af.alarm_schedule_date+at.time) as dateTime FROM " +
                DBConstants.TBL_ALARM_FUTURE + " af join " + DBConstants.TBL_ALARM_TIME +
                " at on af.alarm_id = at.alarm_id and (alarm_schedule_date >= " + currentTime +
                " AND alarm_schedule_date < (strftime('%s', datetime(" + getDayEndTime() / 1000 +
                ", 'unixepoch'), '7 day')*1000)) ORDER BY alarm_schedule_date ASC;";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        MasterObject masterObject;
        if (cursor.moveToFirst()) {
            do {
                masterObject = new MasterObject();
                SaveMedicineInfoBean saveMedicineInfoBean = getSaveMedicineInfoBean(cursor.getInt(0));
                masterObject.setSaveMedicineInfoBean(saveMedicineInfoBean);
                masterObject.setDateTime(cursor.getLong(1));

                masterList.add(masterObject);
            } while (cursor.moveToNext());
        }
        return masterList;
    }

    public boolean onSaveAlarmTime(ArrayList<Long> timeList, int alarmId, ArrayList<Long> futureDates) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues;
        int count = 0;
        saveAlarmFuture(alarmId, futureDates);
        if (timeList != null && timeList.size() > 0) {
            for (long time : timeList) {
                count++;
                contentValues = new ContentValues();
                contentValues.put("alarm_id", alarmId);
                contentValues.put("time", time);
                db.insert(DBConstants.TBL_ALARM_TIME, null, contentValues);
            }
            return (timeList.size() == count);
        }
        return false;
    }

    private void saveAlarmFuture(int alarmId, ArrayList<Long> futureDates) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues;
        if (futureDates != null && futureDates.size() > 0) {
            for (long futureDate : futureDates) {
                contentValues = new ContentValues();
                contentValues.put("alarm_id", alarmId);
                contentValues.put("alarm_schedule_date", futureDate);
                db.insert(DBConstants.TBL_ALARM_FUTURE, null, contentValues);
            }
        }
    }

    public void deleteAllTableRow(int medicationId, int alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        deleteMedicine(db, medicationId);
        deleteAlarm(db, alarmId);
        deleteAlarmTime(db, alarmId);
        deleteAlarmFuture(db, alarmId);
    }

    private void deleteMedicine(SQLiteDatabase db, int medicationId) {
        String queryDelete = "DELETE from " + DBConstants.TBL_MEDICATION +
                " WHERE medication_id = " + medicationId;
        db.execSQL(queryDelete);
    }

    private void deleteAlarm(SQLiteDatabase db, int alarmId) {
        String queryDelete = "DELETE from " + DBConstants.TBL_ALARM +
                " WHERE alarm_id = " + alarmId;
        db.execSQL(queryDelete);
    }

    private void deleteAlarmTime(SQLiteDatabase db, int alarmId) {
        String queryDelete = "DELETE from " + DBConstants.TBL_ALARM_TIME +
                " WHERE alarm_id = " + alarmId;
        db.execSQL(queryDelete);
    }

    private void deleteAlarmFuture(SQLiteDatabase db, int alarmId) {
        String queryDelete = "DELETE from " + DBConstants.TBL_ALARM_FUTURE +
                " WHERE alarm_id = " + alarmId;
        db.execSQL(queryDelete);
    }

}
