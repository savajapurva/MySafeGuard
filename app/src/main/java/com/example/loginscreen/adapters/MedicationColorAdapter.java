package com.example.loginscreen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.loginscreen.R;
import com.example.loginscreen.bean.MedicationColorBean;

import java.util.List;

public class MedicationColorAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<MedicationColorBean> medicationColorBeans;

    public MedicationColorAdapter(Context context, List<MedicationColorBean> medicationColorBeans) {
        this.context = context;
        this.medicationColorBeans = medicationColorBeans;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return medicationColorBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return medicationColorBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    boolean f = true;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        if (convertView == null) {

            gridViewAndroid = new View(context);
            gridViewAndroid = layoutInflater.inflate(R.layout.medication_color_item, null);
            LinearLayout backColor = gridViewAndroid.findViewById(R.id.backcolor);
            ImageView selected = gridViewAndroid.findViewById(R.id.selected);
            backColor.setBackground(context.getResources().getDrawable(medicationColorBeans.get(position).getSelectedColor()));
            //backColor.setBackgroundColor(context.getResources().getColor(medicationColorBeans.get(position).getSelectedColor()));

            if (medicationColorBeans.get(position).isChecked()) {
                selected.setVisibility(View.VISIBLE);
            } else {
                selected.setVisibility(View.GONE);
            }
        } else {
            gridViewAndroid = convertView;
        }
        return gridViewAndroid;
    }
}
