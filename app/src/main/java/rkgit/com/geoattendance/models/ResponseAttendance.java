package rkgit.com.geoattendance.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ayush Kulshrestha
 * on 31-03-2018.
 */

public class ResponseAttendance {

    @SerializedName("response")
    public List<UserAttendanceData> response;

}
