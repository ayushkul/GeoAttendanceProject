package rkgit.com.geoattendance.managers;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rkgit.com.geoattendance.models.AttendenceModel;
import rkgit.com.geoattendance.models.ResponseModel;
import rkgit.com.geoattendance.models.ViewAttendanceRequestModel;
import rkgit.com.geoattendance.models.ResponseAttendance;

/**
 * Created by Ayush Kulshrestha
 * on 23-03-2018.
 */

public interface NodeApi {
    @Headers({"Content-Type:application/json"})
    @POST("checkIn")
    Call<ResponseModel> markAttendance(@Body AttendenceModel attendenceModel);

    @Headers({"Content-Type:application/json"})
    @POST("checkOut")
    Call<ResponseModel> checkOut(@Body AttendenceModel attendenceModel);

    @Headers({"Content-Type:application/json"})
    @POST("fetchUserData")
    Call<ResponseAttendance> fetchUserData(@Body ViewAttendanceRequestModel userId);

}
