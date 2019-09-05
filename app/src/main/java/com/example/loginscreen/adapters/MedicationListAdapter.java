package com.example.loginscreen.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginscreen.R;
import com.example.loginscreen.bean.DataHolder;
import com.example.loginscreen.bean.SaveMedicineInfoBean;
import com.example.loginscreen.fragment.CallBack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MedicationListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SaveMedicineInfoBean> saveMedicineInfoBeans;
    private LayoutInflater layoutInflater;
    private CallBack callBack;


    public MedicationListAdapter(Context context, ArrayList<SaveMedicineInfoBean> saveMedicineInfoBeans) {
        this.context = context;
        this.saveMedicineInfoBeans = saveMedicineInfoBeans;
        layoutInflater = LayoutInflater.from(context);
    }


    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    @Override
    public int getCount() {
        return saveMedicineInfoBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return saveMedicineInfoBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.medication_item_row, parent, false);
            viewHolder.icon = convertView.findViewById(R.id.icon);
            viewHolder.name = convertView.findViewById(R.id.medicationName);
            viewHolder.medicationFrequency = convertView.findViewById(R.id.medicationFrequency);
            viewHolder.medicationPeriod = convertView.findViewById(R.id.medicationPeriod);
            viewHolder.more = convertView.findViewById(R.id.manageMedicationButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final SaveMedicineInfoBean saveMedicineInfoBean = saveMedicineInfoBeans.get(position);
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
        return convertView;
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
            selectedStr = selectedStr.trim();
            int size = selectedStr.length();
            if(size > 0){
                return selectedStr.substring(0, selectedStr.lastIndexOf(","));
            }
            return "";
        }

        return everyStr;
    }

    public class ViewHolder {
        ImageView icon;
        TextView name, medicationFrequency, medicationPeriod;
        ImageButton more;
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

    public void dataNotify(ArrayList<SaveMedicineInfoBean> saveMedicineInfoBeans) {
        this.saveMedicineInfoBeans = saveMedicineInfoBeans;
        notifyDataSetInvalidated();
    }
}
