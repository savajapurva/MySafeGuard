package com.example.loginscreen.bean;

public class MasterObject {

    private SaveMedicineInfoBean saveMedicineInfoBean;
    private long dateTime;

    public SaveMedicineInfoBean getSaveMedicineInfoBean() {
        return saveMedicineInfoBean;
    }

    public void setSaveMedicineInfoBean(SaveMedicineInfoBean saveMedicineInfoBean) {
        this.saveMedicineInfoBean = saveMedicineInfoBean;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
