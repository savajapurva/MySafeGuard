package com.example.loginscreen.bean;

public class MedicationColorBean {
    int selectedColor;
    String color;
    boolean checked;

    public MedicationColorBean(int selectedColor, boolean checked) {
        this.selectedColor = selectedColor;
        this.checked = checked;
    }

    public MedicationColorBean(String color, int selectedColor, boolean checked) {
        this.color = color;
        this.selectedColor = selectedColor;
        this.checked = checked;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
