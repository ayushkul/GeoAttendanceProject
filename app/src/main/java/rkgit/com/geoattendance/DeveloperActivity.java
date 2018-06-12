package rkgit.com.geoattendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rkgit.com.geoattendance.utility.GeoPreference;

public class DeveloperActivity extends AppCompatActivity {
    @BindView(R.id.ip_edit_text)
    EditText ipBox;
    String URLIP = "http://192.168.1.4:4000/";
    String BASE_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        ButterKnife.bind(this);
        BASE_URL = GeoPreference.getInstance().getIpAddress()==null ? URLIP : GeoPreference.getInstance().getIpAddress();
        ipBox.setText(BASE_URL);
        setupToolbar();
    }

    @OnClick(R.id.ip_submit_button)
    public void submitIp() {
        GeoPreference.getInstance().setIpAddress(ipBox.getText().toString().trim());
        Toast.makeText(this, "IP of server is changed.", Toast.LENGTH_SHORT).show();
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Your Attendance Info");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

