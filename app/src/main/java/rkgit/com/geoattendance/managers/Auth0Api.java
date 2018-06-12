package rkgit.com.geoattendance.managers;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rkgit.com.geoattendance.models.UserInfoModel;


/**
 * Created by Ayush Kulshrestha on 18-03-2018.
 */

public interface Auth0Api {
    @GET("userinfo")
    Call<UserInfoModel> getUserdata(@Header("Authorization")String token);

    @FormUrlEncoded
    @POST("dbconnections/change_password")
    Call<Void> emailSent(@Field("client_id")String id, @Field("email")String email, @Field("connection")String connection);

}
