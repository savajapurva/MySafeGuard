package com.example.loginscreen.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loginscreen.R;
import com.example.loginscreen.adapters.MedicationRecyclerAdapter;
import com.example.loginscreen.bean.SaveMedicineInfoBean;
import com.example.loginscreen.db.DatabaseHelper;

import java.util.ArrayList;

import static android.widget.GridLayout.VERTICAL;
import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

public class MedicationFragment extends CoreFragment implements CallBack {

    private static final String TAG = "MedicationFragment";
    private RecyclerView medicineList;
    private ArrayList<SaveMedicineInfoBean> saveMedicineInfoBeans;

    private MedicationRecyclerAdapter medicationRecyclerAdapter;

    public MedicationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_medicines, container, false);
        medicineList = rootView.findViewById(R.id.medicineList);
        saveMedicineInfoBeans = new ArrayList<>();
        medicationRecyclerAdapter = new MedicationRecyclerAdapter(getActivity(), saveMedicineInfoBeans);
        medicationRecyclerAdapter.setCallBack(MedicationFragment.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        medicineList.setLayoutManager(linearLayoutManager);
        medicineList.setAdapter(medicationRecyclerAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        medicineList.addItemDecoration(decoration);

        (new GetMedicationData()).execute();

        return rootView;
    }

    @Override
    public void deleteOperation(SaveMedicineInfoBean saveMedicineInfoBean) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        int medicationId = saveMedicineInfoBean.getMedication_id();
        int alarmId = saveMedicineInfoBean.getAlarm_id();
        databaseHelper.deleteAllTableRow(medicationId, alarmId);
        new GetMedicationData().execute();
    }

    class GetMedicationData extends AsyncTask<Void, Boolean, Boolean> {
        ProgressDialog progressDialog;
        ArrayList<SaveMedicineInfoBean> list = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Fetching");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
            list = databaseHelper.getAllMedicineInfo();
            return (list != null && list.size() > 0);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            saveMedicineInfoBeans.clear();
            saveMedicineInfoBeans.addAll(list);
            medicationRecyclerAdapter.notifyDataSetChanged();

        }
    }
}
