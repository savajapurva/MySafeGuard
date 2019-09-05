package com.example.loginscreen.bean;

public class SaveMedicineInfoBean {
    private long creationDateMillis, startDateMillis, endDateMillis;
    private String reminderType, scheduleType;
    private int alarm_id;
    private int medication_id;
    private int active_monday;
    private int active_tuesday;
    private int active_wednesday;
    private int active_thursday;
    private int active_friday;
    private int active_saturday;
    private int active_sunday;
    private int active_nth_day;
    private int color;
    private int type;
    private String name;

    public SaveMedicineInfoBean() {
    }

    public long getCreationDateMillis() {
        return creationDateMillis;
    }

    public void setCreationDateMillis(long creationDateMillis) {
        this.creationDateMillis = creationDateMillis;
    }

    public long getStartDateMillis() {
        return startDateMillis;
    }

    public void setStartDateMillis(long startDateMillis) {
        this.startDateMillis = startDateMillis;
    }

    public long getEndDateMillis() {
        return endDateMillis;
    }

    public void setEndDateMillis(long endDateMillis) {
        this.endDateMillis = endDateMillis;
    }

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public int getMedication_id() {
        return medication_id;
    }

    public void setMedication_id(int medication_id) {
        this.medication_id = medication_id;
    }

    public int getActive_monday() {
        return active_monday;
    }

    public void setActive_monday(int active_monday) {
        this.active_monday = active_monday;
    }

    public int getActive_tuesday() {
        return active_tuesday;
    }

    public void setActive_tuesday(int active_tuesday) {
        this.active_tuesday = active_tuesday;
    }

    public int getActive_wednesday() {
        return active_wednesday;
    }

    public void setActive_wednesday(int active_wednesday) {
        this.active_wednesday = active_wednesday;
    }

    public int getActive_thursday() {
        return active_thursday;
    }

    public void setActive_thursday(int active_thursday) {
        this.active_thursday = active_thursday;
    }

    public int getActive_friday() {
        return active_friday;
    }

    public void setActive_friday(int active_friday) {
        this.active_friday = active_friday;
    }

    public int getActive_saturday() {
        return active_saturday;
    }

    public void setActive_saturday(int active_saturday) {
        this.active_saturday = active_saturday;
    }

    public int getActive_sunday() {
        return active_sunday;
    }

    public void setActive_sunday(int active_sunday) {
        this.active_sunday = active_sunday;
    }

    public int getActive_nth_day() {
        return active_nth_day;
    }

    public void setActive_nth_day(int active_nth_day) {
        this.active_nth_day = active_nth_day;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }
}
