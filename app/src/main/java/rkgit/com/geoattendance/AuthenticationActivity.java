package rkgit.com.geoattendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
    }
}
