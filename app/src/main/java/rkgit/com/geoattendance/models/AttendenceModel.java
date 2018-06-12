package rkgit.com.geoattendance.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ayush Kulshrestha
 * on 23-03-2018.
 */

public class AttendenceModel {
    @SerializedName("emp_id")
    public String empId;
    @SerializedName("date")
    public String date;
    @SerializedName("time")
    public String time;

    public AttendenceModel(String empId, String date, String time) {
        this.empId = empId;
        this.date = date;
        this.time = time;
    }
}
