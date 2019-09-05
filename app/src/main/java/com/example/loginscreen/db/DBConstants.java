package com.example.loginscreen.db;

public class DBConstants {

    public static final String TBL_ALARM = "alarm";

    public static final String CREATE_ALARM_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_ALARM +
            " (" +
            "`alarm_id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`medication_id` INTEGER NOT NULL, " +
            "`creation_date` INTEGER NOT NULL, " +
            "`start_date` INTEGER NOT NULL, " +
            "`end_date` INTEGER NOT NULL, " +
            "`reminderType` TEXT NOT NULL, " +
            "`scheduleType` TEXT NOT NULL, " +
            "`active_monday` INTEGER NOT NULL, " +
            "`active_tuesday` INTEGER NOT NULL, " +
            "`active_wednesday` INTEGER NOT NULL," +
            "`active_thursday` INTEGER NOT NULL, " +
            "`active_friday` INTEGER NOT NULL, " +
            "`active_saturday` INTEGER NOT NULL, " +
            "`active_sunday` INTEGER NOT NULL, " +
            "`active_nth_day` INTEGER NOT NULL, " +
            "FOREIGN KEY(`medication_id`) " +
            "REFERENCES `medication`(`medication_id`) " +
            "ON UPDATE NO ACTION ON DELETE CASCADE );";

    public static final String TBL_MEDICATION = "medication";
    private static final String CREATE_MEDICATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_MEDICATION +
            " (" +
            "`medication_id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`color` INTEGER NOT NULL, " +
            "`type` INTEGER NOT NULL, " +
            "`name` TEXT NOT NULL);";


    public static final String TBL_ALARM_TIME = "alarm_time";
    private static final String CREATE_ALARM_TIME = "CREATE TABLE IF NOT EXISTS " + TBL_ALARM_TIME +
            " (" +
            "`alarm_time_id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`alarm_id` INTEGER NOT NULL, " +
            "`time` INTEGER NOT NULL);";


    public static final String TBL_ALARM_FUTURE = "alarm_future";
    private static final String CREATE_ALARM_FUTURE = "CREATE TABLE IF NOT EXISTS " + TBL_ALARM_FUTURE +
            " (" +
            "`alarm_future_id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`alarm_id` INTEGER NOT NULL, " +
            "`alarm_schedule_date` INTEGER NOT NULL);";


    public static String[] CREATE_TBL_ARRAY = {CREATE_MEDICATION_TABLE, CREATE_ALARM_TABLE, CREATE_ALARM_TIME, CREATE_ALARM_FUTURE};
}