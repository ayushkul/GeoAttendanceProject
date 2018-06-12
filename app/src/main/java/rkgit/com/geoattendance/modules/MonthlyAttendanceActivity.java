package rkgit.com.geoattendance.modules;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rkgit.com.geoattendance.R;
import rkgit.com.geoattendance.managers.NodeApi;
import rkgit.com.geoattendance.managers.NodeApiClient;
import rkgit.com.geoattendance.models.ResponseAttendance;
import rkgit.com.geoattendance.models.UserAttendanceData;
import rkgit.com.geoattendance.models.ViewAttendanceRequestModel;
import rkgit.com.geoattendance.utility.GeoPreference;

/**
 * Created by Ayush Kulshrestha
 * on 24-04-2018.
 */

public class MonthlyAttendanceActivity extends AppCompatActivity {
    @BindView(R.id.month_name_text_view)
    TextView monthName;
    @BindView(R.id.year_name_text_view)
    TextView yearName;
    @BindView(R.id.attendace_data_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.username_text_view)
    TextView username;
    @BindView(R.id.userid_text_view)
    TextView userid;
    @BindView(R.id.total_working_days_text_view)
    TextView workingDaysTextView;
    @BindView(R.id.present_text_view)
    TextView presentTextView;
    @BindView(R.id.absent_text_view)
    TextView absentTextView;
    @BindView(R.id.date_header)
    LinearLayout attandanceDataDateLinearLayout;
    @BindView(R.id.attendance_data_linear_layout)
    LinearLayout attandanceDataLinearLayout;
    int monthNo = 20;
    RecyclerViewAdapter adapter;
    ArrayList<UserAttendanceData> arrayList = new ArrayList<>();
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_analysis);
        ButterKnife.bind(this);
        setUpToolBar();
        attandanceDataLinearLayout.setVisibility(View.GONE);
        attandanceDataDateLinearLayout.setVisibility(View.GONE);
        yearName.setText("2018");
//        createMonthChooser();
    }

    @SuppressLint("NewApi")
    private void createMonthChooser() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.month_picker_dialog_layout);
        final ListView monthList = dialog.findViewById(R.id.month_list);
        final ArrayList<String> monthArrayList = new ArrayList<>();
        monthArrayList.add("January");
        monthArrayList.add("February");
        monthArrayList.add("March");
        monthArrayList.add("April");
        monthArrayList.add("May");
        monthArrayList.add("June");
        monthArrayList.add("July");
        monthArrayList.add("August");
        monthArrayList.add("September");
        monthArrayList.add("October");
        monthArrayList.add("November");
        monthArrayList.add("December");
        monthList.setAdapter(new ArrayAdapter<String>(this, R.layout.layout_month, R.id.month_list_item, monthArrayList));
        monthList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                monthName.setText(monthArrayList.get(i));
                monthNo = i + 1;
                showDataOfMonth(monthNo);
            }
        });
        dialog.show();
    }

    @OnClick(R.id.month_name_text_view)
    void onMonthClick() {
        createMonthChooser();
    }

    @OnClick(R.id.year_name_text_view)
    void onYearClick() {
        createYearChooser();
    }

    private void createYearChooser() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.month_picker_dialog_layout);
        final ListView monthList = dialog.findViewById(R.id.month_list);
        final ArrayList<String> monthArrayList = new ArrayList<>();
        monthArrayList.add("2018");
        monthList.setAdapter(new ArrayAdapter<String>(this, R.layout.layout_month, R.id.month_list_item, monthArrayList));
        monthList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                yearName.setText(monthArrayList.get(i));
                showDataOfMonth(monthNo);
            }
        });
        dialog.show();
    }

    private void showDataOfMonth(final int month) {
        if (GeoPreference.getInstance().getIpAddress() != null)
            NodeApiClient.changeBaseUrl(GeoPreference.getInstance().getIpAddress());
        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching data");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        NodeApi nodeApiClient = NodeApiClient.getClient().create(NodeApi.class);
        final ViewAttendanceRequestModel model = new ViewAttendanceRequestModel(GeoPreference.getInstance().getUserId());
        Call<ResponseAttendance> call = nodeApiClient.fetchUserData(model);
        call.enqueue(new Callback<ResponseAttendance>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAttendance> call, @NonNull Response<ResponseAttendance> response) {
                if (response.code() == 200 && response.body() != null) {
                    ArrayList<UserAttendanceData> list = (ArrayList<UserAttendanceData>) response.body().response;
                    if (dialog.isShowing())
                        dialog.dismiss();
                    setDataOnView(list, month);
                } else if (dialog.isShowing())
                    dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseAttendance> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                ArrayList<UserAttendanceData> list = new ArrayList<>();
                UserAttendanceData data = new UserAttendanceData("08-05-2018", "13:12", "13:14");
                list.add(data);
                data = new UserAttendanceData("09-05-2018", "09:37", "09:40");
                list.add(data);
                data = new UserAttendanceData("10-05-2018", "04:42", "04:43");
                list.add(data);
                createDataForView(list,4);
                Toast.makeText(MonthlyAttendanceActivity.this, "Sorry for the inconvenience, Servers are under maintenance !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setDataOnView(ArrayList<UserAttendanceData> list, int month) {
        if (list == null)
            return;
        List<UserAttendanceData> finalList = new ArrayList<>();
        ArrayList<UserAttendanceData> uniqueList = null;
        for (int i = 0; i < list.size(); i++) {
            String listMonth = list.get(i).date.substring(3, 5);
            if (Integer.valueOf(listMonth) == month)
                finalList.add(list.get(i));
            Set<UserAttendanceData> uniqueGas = new HashSet<UserAttendanceData>(finalList);
            uniqueList = new ArrayList<>(uniqueGas);
        }
        if (finalList.size() > 0 && uniqueList != null)
            createDataForView(uniqueList, month);
        else {
            UserAttendanceData data = new UserAttendanceData("-", "-", "-");
            ArrayList<UserAttendanceData> fynlList = new ArrayList<>();
            fynlList.add(data);
            fynlList.add(data);
            fynlList.add(data);
            workingDaysTextView.setText("Total working Days : -");
            presentTextView.setText("Presents : -");
            absentTextView.setText("Absents : -");
            showDataOnView(fynlList);
            Toast.makeText(this, "You have no records in selected Month!", Toast.LENGTH_SHORT).show();
        }

    }

    private void createDataForView(ArrayList<UserAttendanceData> finalList, int month) {
        int totalDays = 0, presents = 0, absents = 0;
        boolean found=false;
        LinkedHashSet<UserAttendanceData> list = new LinkedHashSet<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.MONTH, month-1);
        int numDays = calendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= numDays; i++) {
            found=false;
            calendar.set(Calendar.DATE, i);
            if (calendar.getTime().after(new Date()))
                break;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale.getDefault());
            String formattedDate = sdf.format(calendar.getTime());
            String dateValue = formattedDate.substring(0, 10);
            for (UserAttendanceData data : finalList) {
                if (Integer.valueOf(data.date.substring(0, 2).trim()) == i) {
                    list.add(data);
                    presents++;
                    totalDays++;
                    found=true;
                }
            }
            if((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY  || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY )&&!found){
                UserAttendanceData holidayModel = new UserAttendanceData(dateValue, "off", "off");
//                    totalDays--;
                list.add(holidayModel);
            }else if(!(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)&&!found){
                UserAttendanceData holidayModel = new UserAttendanceData(dateValue, "A", "A");
                list.add(holidayModel);
                totalDays++;
                absents++;
            }

        }
        ArrayList<UserAttendanceData> finalLists=new ArrayList<>(list);
        showDataOnView(finalLists);
        workingDaysTextView.setText(String.format("Total working Days : %s", String.valueOf(totalDays)));
        presentTextView.setText(String.format("Presents : %s", String.valueOf(presents)));
        absentTextView.setText(String.format("Absents : %s", String.valueOf(absents)));
    }

    private void showDataOnView(ArrayList<UserAttendanceData> dataOnView) {
        arrayList = dataOnView;
        adapter = new RecyclerViewAdapter(arrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        attandanceDataDateLinearLayout.setVisibility(View.VISIBLE);
        attandanceDataLinearLayout.setVisibility(View.VISIBLE);
    }


    private void setUpToolBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Month wise Attendance");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        username.setText(GeoPreference.getInstance().getUsername());
        userid.setText(GeoPreference.getInstance().getUserId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
