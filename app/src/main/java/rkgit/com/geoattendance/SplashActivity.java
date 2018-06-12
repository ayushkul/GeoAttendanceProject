package rkgit.com.geoattendance;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import rkgit.com.geoattendance.modules.dashboard.DashboardActivity;
import rkgit.com.geoattendance.utility.GeoPreference;

public class SplashActivity extends AppCompatActivity {
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (GeoPreference.getInstance() == null)
            GeoPreference.init(getApplicationContext());
        //getActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (GeoPreference.getInstance().getAccessTokenAuth0().length() > 0) {
                    redirectToDash();
                } else {

                    redirectToAuth();

                }
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    private void redirectToAuth() {
        GeoPreference.getInstance().setIpAddress("http://192.168.43.220:4000/");
        Intent intent = new Intent(SplashActivity.this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToDash() {
        GeoPreference.getInstance().setIpAddress("http://192.168.43.220:4000/");
        Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler = null;
        runnable = null;
    }
}
