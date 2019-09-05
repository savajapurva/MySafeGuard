package com.example.loginscreen.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loginscreen.R;
import com.example.loginscreen.adapters.CurrentMedicineAdapter;
import com.example.loginscreen.bean.SaveMedicineInfoBean;
import com.example.loginscreen.bean.MasterObject;
import com.example.loginscreen.db.DatabaseHelper;
import com.example.loginscreen.adapters.MedicationRecyclerAdapter;

import java.util.ArrayList;

import static android.widget.GridLayout.VERTICAL;
import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

public class DashBoardFragment extends CoreFragment  {

    private static final String TAG = "DashBoardFragment";
    private RecyclerView medicineList;
    private ArrayList<MasterObject> masterObjectArrayList;

    private CurrentMedicineAdapter currentMedicineAdapter;

    public DashBoardFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        medicineList = rootView.findViewById(R.id.medicineList);
        masterObjectArrayList = new ArrayList<>();
        currentMedicineAdapter = new CurrentMedicineAdapter(getActivity(), masterObjectArrayList);
        //currentMedicineAdapter.setCallBack(DashBoardFragment.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        medicineList.setLayoutManager(linearLayoutManager);
        medicineList.setAdapter(currentMedicineAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        medicineList.addItemDecoration(decoration);
        (new GetMasterObjects()).execute();
        return rootView;
    }

    class GetMasterObjects extends AsyncTask<Void, Boolean, Boolean> {
        ProgressDialog progressDialog;
        ArrayList<MasterObject> list = new ArrayList<>();

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
            list = databaseHelper.getMasterObjects();
            return (list != null && list.size() > 0);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            masterObjectArrayList.clear();
            masterObjectArrayList.addAll(list);
            currentMedicineAdapter.notifyDataSetChanged();

        }
    }
}
