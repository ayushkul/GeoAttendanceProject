package rkgit.com.geoattendance.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ayush Kulshrestha
 * on 31-03-2018.
 */

public class UserAttendanceData {
    @SerializedName("_id")
    public String id;
    @SerializedName("__v")
    public String a_vs;
    @SerializedName("date")
    public String date;
    @SerializedName("checkIn")
    public String checkIn;
    @SerializedName("checkOut")
    public String checkOut;

    public UserAttendanceData(String date, String checkIn, String checkOut) {
        this.date = date;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
