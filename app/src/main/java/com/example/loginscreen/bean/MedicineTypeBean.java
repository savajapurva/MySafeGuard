package com.example.loginscreen.bean;

public class MedicineTypeBean {
    int imageId;
    boolean checked;

    public MedicineTypeBean(int imageId, boolean checked) {
        this.imageId = imageId;
        this.checked = checked;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
