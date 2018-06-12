package rkgit.com.geoattendance.modules.dashboard.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rkgit.com.geoattendance.R;
import rkgit.com.geoattendance.managers.NodeApi;
import rkgit.com.geoattendance.managers.NodeApiClient;
import rkgit.com.geoattendance.models.ResponseAttendance;
import rkgit.com.geoattendance.models.UserAttendanceData;
import rkgit.com.geoattendance.models.ViewAttendanceRequestModel;
import rkgit.com.geoattendance.modules.RecyclerViewAdapter;
import rkgit.com.geoattendance.utility.GeoPreference;

/**
 * Created by Ayush Kulshrestha
 * on 31-03-2018.
 */

public class ViewAttendance extends AppCompatActivity {
    @BindView(R.id.attendace_data_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.username_text_view)
    TextView username;
    @BindView(R.id.userid_text_view)
    TextView userid;
    RecyclerViewAdapter adapter;
    ProgressDialog dialog;
    ArrayList<UserAttendanceData> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_attendance_layout);
        ButterKnife.bind(this);
        setupToolbar();
        getResponse();
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Your Attendance Info");
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

    public void getResponse() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching data");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        if (GeoPreference.getInstance().getIpAddress() != null)
            NodeApiClient.changeBaseUrl(GeoPreference.getInstance().getIpAddress());
        NodeApi nodeApiClient = NodeApiClient.getClient().create(NodeApi.class);
        ViewAttendanceRequestModel model = new ViewAttendanceRequestModel(GeoPreference.getInstance().getUserId());
        Call<ResponseAttendance> call = nodeApiClient.fetchUserData(model);
        call.enqueue(new Callback<ResponseAttendance>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAttendance> call, @NonNull Response<ResponseAttendance> response) {
                if (response.code() == 200 && response.body() != null) {
                    ArrayList<UserAttendanceData> list = (ArrayList<UserAttendanceData>) response.body().response;
                    if (dialog.isShowing())
                        dialog.dismiss();
                    setDataOnView(list);
                } else if (dialog.isShowing())
                    dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseAttendance> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                ArrayList<UserAttendanceData> list = new ArrayList<>();
                UserAttendanceData data = new UserAttendanceData("-", "-", "-");
                list.add(data);
                list.add(data);
                list.add(data);
                setDataOnView(list);
                Toast.makeText(ViewAttendance.this, "Sorry for the inconvenience, Servers are under maintenance !", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setDataOnView(ArrayList<UserAttendanceData> dataOnView) {
        arrayList = dataOnView;
        adapter = new RecyclerViewAdapter(arrayList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
