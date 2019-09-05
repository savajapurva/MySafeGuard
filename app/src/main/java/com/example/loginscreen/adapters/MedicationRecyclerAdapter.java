package com.example.loginscreen.adapters;
import com.example.loginscreen.R;
import com.example.loginscreen.bean.DataHolder;
import com.example.loginscreen.bean.SaveMedicineInfoBean;
import com.example.loginscreen.fragment.CallBack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MedicationRecyclerAdapter extends RecyclerView.Adapter<MedicationRecyclerAdapter.MedicationViewHolder> {

    private CallBack callBack;

    private Context context;
    private ArrayList<SaveMedicineInfoBean> saveMedicineInfoBeans;
    private LayoutInflater layoutInflater;

    public MedicationRecyclerAdapter(Context context, ArrayList<SaveMedicineInfoBean> saveMedicineInfoBeans) {
        this.context = context;
        this.saveMedicineInfoBeans = saveMedicineInfoBeans;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.medication_item_row, viewGroup, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder viewHolder, int i) {
        final SaveMedicineInfoBean saveMedicineInfoBean = saveMedicineInfoBeans.get(i);

        viewHolder.icon.setImageResource(getIconResId(saveMedicineInfoBean.getColor(), saveMedicineInfoBean.getType()));
        viewHolder.name.setText(saveMedicineInfoBean.getName());
        String reminderType = saveMedicineInfoBean.getReminderType();
        String scheduleType = getScheduleData(saveMedicineInfoBean);
        viewHolder.medicationFrequency.setText(String.valueOf(reminderType + " | " + scheduleType));
        String startDate = dateInString(saveMedicineInfoBean.getStartDateMillis());
        String endDate = dateInString(saveMedicineInfoBean.getEndDateMillis());
        viewHolder.medicationPeriod.setText(String.valueOf(startDate + " - " + endDate));

        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.medication_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.remove_medication){
                            if(callBack != null){
                                callBack.deleteOperation(saveMedicineInfoBean);
                            }
                            return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    private String getScheduleData(final SaveMedicineInfoBean saveMedicineInfoBean) {

        String scheduleType = saveMedicineInfoBean.getScheduleType();
        int nthDay = saveMedicineInfoBean.getActive_nth_day();
        String everyStr = "Every " + nthDay + " day";
        if(scheduleType.equalsIgnoreCase("Selected Days")){
            int mon= saveMedicineInfoBean.getActive_monday();
            int tue = saveMedicineInfoBean.getActive_thursday();
            int wed = saveMedicineInfoBean.getActive_wednesday();
            int thu = saveMedicineInfoBean.getActive_thursday();
            int fri = saveMedicineInfoBean.getActive_friday();
            int sat = saveMedicineInfoBean.getActive_saturday();
            int sun = saveMedicineInfoBean.getActive_sunday();
            StringBuilder stringBuilder = new StringBuilder();
            if(mon == 0){
                stringBuilder.append("Mo, ");
            }
            if(tue == 0){
                stringBuilder.append("Tu, ");
            }
            if(wed == 0){
                stringBuilder.append("We, ");
            }
            if(thu == 0){
                stringBuilder.append("Th, ");
            }
            if(fri == 0){
                stringBuilder.append("Fr, ");
            }
            if(sat == 0){
                stringBuilder.append("Sa, ");
            }
            if(sun == 0){
                stringBuilder.append("Su, ");
            }

            String selectedStr = stringBuilder.toString();
            int size = selectedStr.length();
            selectedStr = selectedStr.trim();
            if(size > 0){
                return selectedStr.substring(0, selectedStr.lastIndexOf(","));
            }
            return "";
        }

        return everyStr;
    }


    private String dateInString(long dateMillis) {
        Date date = new Date(dateMillis);
        SimpleDateFormat sdfIn = new SimpleDateFormat("d MMM");
        return sdfIn.format(date);

    }

    private int getIconResId(int colorId, int typeId) {
        DataHolder dataHolder = DataHolder.getInstance();
        return dataHolder.getMappingTypeToColor().get(colorId).get(typeId).getImageId();
    }

    @Override
    public int getItemCount() {
        return saveMedicineInfoBeans.size();
    }

    public class MedicationViewHolder extends RecyclerView.ViewHolder  {
        ImageView icon;
        TextView name, medicationFrequency, medicationPeriod;
        ImageButton more;
        public MedicationViewHolder(View convertView) {
            super(convertView);

            icon = convertView.findViewById(R.id.icon);
            name = convertView.findViewById(R.id.medicationName);
            medicationFrequency = convertView.findViewById(R.id.medicationFrequency);
            medicationPeriod = convertView.findViewById(R.id.medicationPeriod);
            more = convertView.findViewById(R.id.manageMedicationButton);
        }
    }
}
