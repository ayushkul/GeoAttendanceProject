package rkgit.com.geoattendance.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ayush Kulshrestha
 * on 23-03-2018.
 */

public class NodeApiClient {
    private static String BASE_URL = "http://192.168.1.4:4000/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        Gson gson = new GsonBuilder().setLenient().create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static void changeBaseUrl(String url) {
        if (url == null)
            return;
        BASE_URL = url.trim();
        retrofit=null;
        retrofit = getClient();
    }
}
