package com.example.loginscreen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.loginscreen.R;
import com.example.loginscreen.bean.MedicineTypeBean;

import java.util.List;

public class MedicineTypeAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<MedicineTypeBean> medicineTypeBeans;

    public MedicineTypeAdapter(Context context, List<MedicineTypeBean> medicineTypeBeans) {
        this.context = context;
        this.medicineTypeBeans = medicineTypeBeans;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return medicineTypeBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return medicineTypeBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewAndroid;

        if (convertView == null) {
            gridViewAndroid = new View(context);
            gridViewAndroid = layoutInflater.inflate(R.layout.medicine_type_item, null);
            ImageView medicineType = gridViewAndroid.findViewById(R.id.medicineType);
            ImageView selected = gridViewAndroid.findViewById(R.id.selected);
            medicineType.setImageResource(medicineTypeBeans.get(position).getImageId());

            if (medicineTypeBeans.get(position).isChecked()) {
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
