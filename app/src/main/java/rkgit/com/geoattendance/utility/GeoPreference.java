package rkgit.com.geoattendance.utility;

import android.content.Context;
import android.content.SharedPreferences;

import rkgit.com.geoattendance.R;

/**
 * Created by Ayush Kulshrestha on 05-03-2018.
 */

public class GeoPreference {

    private static GeoPreference geoPreference;
    private SharedPreferences sharedPreferences;
    private static final String ACCESS_TOKEN_AUTH0 = "googleAccountName";
    private static final String IP_ADDRESS = "ipAddress";
    private static final String USER_EMAIL = "userEmail";
    private static final String USER_ID = "userId";
    private static final String USERNAME = "userName";

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

    private String getStringFromPreferences(String key) {
        return sharedPreferences.getString(key, "");
    }

    /*Getters*/

    public String getAccessTokenAuth0() {
        return getStringFromPreferences(ACCESS_TOKEN_AUTH0);
    }

    public String getIpAddress() {
        return getStringFromPreferences(IP_ADDRESS);
    }

    public String getUserEmail() {
        return getStringFromPreferences(USER_EMAIL);
    }
    public String getUsername() {
        return getStringFromPreferences(USERNAME);
    }
    public String getUserId() {
        return getStringFromPreferences(USER_ID);
    }


    /*Setters*/

    public void setAccessTokenAuth0(String accessToken) {
        setStringInPreferences(ACCESS_TOKEN_AUTH0, accessToken);
    }
    public void setUsername(String username) {
        setStringInPreferences(USERNAME, username);
    }

    public void setUserEmail(String email) {
        setStringInPreferences(USER_EMAIL, email);
    }

    public void setUserId(String userId) {
        setStringInPreferences(USER_ID, userId);
    }

    public void setIpAddress(String ipAddress) {
        setStringInPreferences(IP_ADDRESS, ipAddress);
    }

}
