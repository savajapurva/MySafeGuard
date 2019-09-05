package com.example.loginscreen.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.loginscreen.R;
import com.example.loginscreen.bean.DataHolder;
import com.example.loginscreen.bean.MasterObject;
import com.example.loginscreen.bean.SaveMedicineInfoBean;
import com.example.loginscreen.fragment.CallBack;
import com.example.loginscreen.util.AppUtil;

import java.util.ArrayList;

public class CurrentMedicineAdapter extends RecyclerView.Adapter<CurrentMedicineAdapter.MedicationViewHolder> {

    private CallBack callBack;

    private Context context;
    private ArrayList<MasterObject> masterObjectArrayList;
    private LayoutInflater layoutInflater;

    public CurrentMedicineAdapter(Context context, ArrayList<MasterObject> masterObjectArrayList) {
        this.context = context;
        this.masterObjectArrayList = masterObjectArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public CurrentMedicineAdapter.MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.current_medicine_item_row, viewGroup, false);
        return new CurrentMedicineAdapter.MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentMedicineAdapter.MedicationViewHolder viewHolder, int i) {
        MasterObject masterObject = masterObjectArrayList.get(i);
        final SaveMedicineInfoBean saveMedicineInfoBean = masterObject.getSaveMedicineInfoBean();

        viewHolder.icon.setImageResource(getIconResId(saveMedicineInfoBean.getColor(), saveMedicineInfoBean.getType()));
        viewHolder.name.setText(saveMedicineInfoBean.getName());
        String onlyDate = AppUtil.getDateWithoutTime(masterObject.getDateTime());
        viewHolder.medicationFrequency.setText(onlyDate);
        viewHolder.medicationPeriod.setText(AppUtil.getOnlyTime(masterObject.getDateTime()));
        long currentTime = System.currentTimeMillis();
        String currentDate = AppUtil.getDateWithoutTime(currentTime);
        if (currentDate.equalsIgnoreCase(onlyDate)) {
            if (currentTime > masterObject.getDateTime()) {
                viewHolder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.medicationMystic));
            } else {
                viewHolder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorAccentt));
            }
        } else {
            viewHolder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.medicationColorWhite));
        }
    }


    private int getIconResId(int colorId, int typeId) {
        DataHolder dataHolder = DataHolder.getInstance();
        return dataHolder.getMappingTypeToColor().get(colorId).get(typeId).getImageId();
    }

    @Override
    public int getItemCount() {
        return masterObjectArrayList.size();
    }

    public class MedicationViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name, medicationFrequency, medicationPeriod;
        RelativeLayout relativeLayout;

        public MedicationViewHolder(View convertView) {
            super(convertView);

            relativeLayout = convertView.findViewById(R.id.rLayout);
            icon = convertView.findViewById(R.id.icon);
            name = convertView.findViewById(R.id.medicationName);
            medicationFrequency = convertView.findViewById(R.id.medicationFrequency);
            medicationPeriod = convertView.findViewById(R.id.medicationPeriod);
        }
    }
}
