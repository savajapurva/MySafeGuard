package com.example.loginscreen.bean;

import com.example.loginscreen.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DataHolder {
    private List<MedicineTypeBean> medicineWhite,
            medicineYellow, medicineRed, medicineTeal,
            medicineGreen, medicineBrown, medicinePink,
            medicineOrange, medicineBlack, medicinePurple,
            medicineBlue, medicineOlive;

    private DataHolder() {

    }

    private static DataHolder dataHolder;

    public static DataHolder getInstance() {
        if (dataHolder == null) {
            dataHolder = new DataHolder();
            dataHolder.defaultData();
        }
        return dataHolder;
    }

    private HashMap<Integer, List<MedicineTypeBean>> mappingTypeToColor;
    private HashSet<String> dayOfWeek;

    public void defaultSetDayOfWeek(){
        dayOfWeek = new HashSet<>();
        dayOfWeek.add("Monday");
        dayOfWeek.add("Tuesday");
        dayOfWeek.add("Wednesday");
        dayOfWeek.add("Thursday");
        dayOfWeek.add("Friday");
        dayOfWeek.add("Saturday");
        dayOfWeek.add("Sunday");
    }

    private void defaultData() {
        onGetWhiteMedicine();
        onGetYellowMedicine();
        onGetRedMedicine();
        onGetPinkMedicine();
        onGetTealMedicine();
        onGetGreenMedicine();
        onGetBlackMedicine();
        onGetOrangeMedicine();
        onGetBrownMedicine();
        onGetPurpleMedicine();
        onGetBlueMedicine();
        onGetOliveMedicine();
        mappingTypeToColor = new HashMap<>();
        mappingTypeToColor.put(0, getMedicineWhite());
        mappingTypeToColor.put(1, getMedicineYellow());
        mappingTypeToColor.put(2, getMedicineRed());
        mappingTypeToColor.put(3, getMedicinePink());
        mappingTypeToColor.put(4, getMedicineTeal());
        mappingTypeToColor.put(5, getMedicineGreen());
        mappingTypeToColor.put(6, getMedicineBlack());
        mappingTypeToColor.put(7, getMedicineOrange());
        mappingTypeToColor.put(8, getMedicineBrown());
        mappingTypeToColor.put(9, getMedicinePurple());
        mappingTypeToColor.put(10, getMedicineBlue());
        mappingTypeToColor.put(11, getMedicineOlive());
        defaultSetDayOfWeek();
    }

    public HashMap<Integer, List<MedicineTypeBean>> getMappingTypeToColor() {
        return mappingTypeToColor;
    }

    public void setMappingTypeToColor(HashMap<Integer, List<MedicineTypeBean>> mappingTypeToColor) {
        this.mappingTypeToColor = mappingTypeToColor;
    }

    public boolean isDayOfWeek(String day) {
        if(dayOfWeek != null){
            return dayOfWeek.contains(day);
        }
        return false;
    }

    public void removeDayOfWeek(String day){
        if(dayOfWeek != null){
            dayOfWeek.remove(day);
        }
    }

    public void setDayOfWeek(String day) {
        if(dayOfWeek != null){
            dayOfWeek.add(day);
        }
    }

    private void onGetWhiteMedicine() {
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

    private void onGetYellowMedicine() {
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

    private void onGetRedMedicine() {
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

    private void onGetPinkMedicine() {
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

    private void onGetTealMedicine() {
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

    private void onGetGreenMedicine() {
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

    private void onGetBlackMedicine() {
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

    private void onGetOrangeMedicine() {
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

    private void onGetBrownMedicine() {
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

    private void onGetPurpleMedicine() {
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

    private void onGetBlueMedicine() {
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

    private void onGetOliveMedicine() {
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

    public List<MedicineTypeBean> getMedicineWhite() {
        return medicineWhite;
    }

    public void setMedicineWhite(List<MedicineTypeBean> medicineWhite) {
        this.medicineWhite = medicineWhite;
    }

    public List<MedicineTypeBean> getMedicineYellow() {
        return medicineYellow;
    }

    public void setMedicineYellow(List<MedicineTypeBean> medicineYellow) {
        this.medicineYellow = medicineYellow;
    }

    public List<MedicineTypeBean> getMedicineRed() {
        return medicineRed;
    }

    public void setMedicineRed(List<MedicineTypeBean> medicineRed) {
        this.medicineRed = medicineRed;
    }

    public List<MedicineTypeBean> getMedicineTeal() {
        return medicineTeal;
    }

    public void setMedicineTeal(List<MedicineTypeBean> medicineTeal) {
        this.medicineTeal = medicineTeal;
    }

    public List<MedicineTypeBean> getMedicineGreen() {
        return medicineGreen;
    }

    public void setMedicineGreen(List<MedicineTypeBean> medicineGreen) {
        this.medicineGreen = medicineGreen;
    }

    public List<MedicineTypeBean> getMedicineBrown() {
        return medicineBrown;
    }

    public void setMedicineBrown(List<MedicineTypeBean> medicineBrown) {
        this.medicineBrown = medicineBrown;
    }

    public List<MedicineTypeBean> getMedicinePink() {
        return medicinePink;
    }

    public void setMedicinePink(List<MedicineTypeBean> medicinePink) {
        this.medicinePink = medicinePink;
    }

    public List<MedicineTypeBean> getMedicineOrange() {
        return medicineOrange;
    }

    public void setMedicineOrange(List<MedicineTypeBean> medicineOrange) {
        this.medicineOrange = medicineOrange;
    }

    public List<MedicineTypeBean> getMedicineBlack() {
        return medicineBlack;
    }

    public void setMedicineBlack(List<MedicineTypeBean> medicineBlack) {
        this.medicineBlack = medicineBlack;
    }

    public List<MedicineTypeBean> getMedicinePurple() {
        return medicinePurple;
    }

    public void setMedicinePurple(List<MedicineTypeBean> medicinePurple) {
        this.medicinePurple = medicinePurple;
    }

    public List<MedicineTypeBean> getMedicineBlue() {
        return medicineBlue;
    }

    public void setMedicineBlue(List<MedicineTypeBean> medicineBlue) {
        this.medicineBlue = medicineBlue;
    }

    public List<MedicineTypeBean> getMedicineOlive() {
        return medicineOlive;
    }

    public void setMedicineOlive(List<MedicineTypeBean> medicineOlive) {
        this.medicineOlive = medicineOlive;
    }
}
