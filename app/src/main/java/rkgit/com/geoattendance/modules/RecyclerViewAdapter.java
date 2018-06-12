package rkgit.com.geoattendance.modules;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rkgit.com.geoattendance.R;
import rkgit.com.geoattendance.models.UserAttendanceData;

/**
 * Created by Ayush Kulshrestha
 * on 31-03-2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerviewHolder> {

    private ArrayList<UserAttendanceData> arrayList;
    private Context context;

    public RecyclerViewAdapter(ArrayList<UserAttendanceData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public RecyclerviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_data_cell, parent, false);
        return new RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerviewHolder holder, int position) {
        UserAttendanceData userAttendanceData = arrayList.get(position);
        holder.attendanceDate.setText(userAttendanceData.date);
        holder.checkIn.setText(userAttendanceData.checkIn);
        holder.checkOut.setText(userAttendanceData.checkOut);/*
        if (userAttendanceData.checkIn.equalsIgnoreCase("A"))
            holder.checkIn.setTextColor(context.getResources().getColor(R.color.color_red));
        if (userAttendanceData.checkOut.equalsIgnoreCase("A"))
            holder.checkOut.setTextColor(context.getResources().getColor(R.color.color_red));*/
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class RecyclerviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date_of_attendance)
        TextView attendanceDate;
        @BindView(R.id.checkin_time)
        TextView checkIn;
        @BindView(R.id.checkout_time)
        TextView checkOut;


        RecyclerviewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
