package rkgit.com.geoattendance;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ayush Kulshrestha on 10-03-2018.
 */

public class GeoPreference {

    private static final String AuthToken="authToken";



    private static GeoPreference geoPreference;
    private SharedPreferences sharedPreferences;

    private GeoPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        geoPreference = new GeoPreference(context);
    }

    public static GeoPreference getInstance() {
        return geoPreference;
    }

    private boolean setStringInPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value == null ? "" : value);
        return editor.commit();
    }
    private String getStringFromPreferences(String key){
        return sharedPreferences.getString(key,"");
    }

    public String getAuthToken() {
        return getStringFromPreferences(AuthToken);
    }

    public void setAuthToken(String authToken){
        setStringInPreferences(AuthToken,authToken);
    }

}
