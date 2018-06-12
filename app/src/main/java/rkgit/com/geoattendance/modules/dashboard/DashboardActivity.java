package rkgit.com.geoattendance.modules.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import rkgit.com.geoattendance.R;
import rkgit.com.geoattendance.modules.dashboard.fragments.BrowseFragment;
import rkgit.com.geoattendance.modules.dashboard.fragments.SettingFragment;

public class DashboardActivity extends AppCompatActivity {
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottom_navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        setupToolbar();
        initView();
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Dashboard");

    }

    private void initView() {
        selectFragment(BrowseFragment.newInstance());
        selectFragment(BrowseFragment.newInstance());
        bottom_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_browse:
                        bottom_navigation_view.getMenu().getItem(0).setChecked(true);
                        selectFragment(BrowseFragment.newInstance());
                        break;
                    case R.id.action_setting:
                        bottom_navigation_view.getMenu().getItem(1).setChecked(true);
                        selectFragment(SettingFragment.newInstance());
                        break;
                }
                return true;
            }
        });
    }

    private void selectFragment(Fragment fragment) {
        setActionBarView(fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    private void setActionBarView(Fragment fragment) {
        if (getSupportActionBar() != null) {
            if (fragment instanceof BrowseFragment)
                getSupportActionBar().setTitle("Dashboard");
            else if (fragment instanceof SettingFragment)
                getSupportActionBar().setTitle("Settings");
        }

    }
}
