package rkgit.com.geoattendance.modules.dashboard.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import rkgit.com.geoattendance.modules.settings.AboutUsActivity;
import rkgit.com.geoattendance.managers.ApiClient;
import rkgit.com.geoattendance.managers.Auth0Api;
import rkgit.com.geoattendance.AuthenticationActivity;
import rkgit.com.geoattendance.DeveloperActivity;
import rkgit.com.geoattendance.utility.GeoPreference;
import rkgit.com.geoattendance.R;
import rkgit.com.geoattendance.modules.settings.UserInfoActivity;

/**
 * Created by Ayush Kulshrestha
 * on 06-03-2018.
 */

public class SettingFragment extends Fragment {
    @BindView(R.id.logout_layout)
    LinearLayout logoutLayout;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.user_info_layout)
    void onUserInfoClick() {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.change_password_layout)
    void onChangePasswordClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Password").setMessage("Do you want to change your password ?").setCancelable(false);
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                Auth0Api change = ApiClient.getClient().create(Auth0Api.class);
                String id = getString(R.string.com_auth0_client_id);
                String connection = getString(R.string.auth0_connection_name);
                String email = GeoPreference.getInstance().getUserEmail();
                Call<Void> call1 = change.emailSent(id, email, connection);
                call1.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                        Toast.makeText(getActivity(), "Email sent", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "Try Again after Some time...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.about_us_layout)
    void onAboutUsClick() {
        Intent intent = new Intent(getActivity(), AboutUsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.contact_us_layout)
    void onContactUsClick() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"akcool.ayush@gmail.com"});
        startActivity(Intent.createChooser(intent, "GeoAttendance Support"));
    }

    @OnClick(R.id.logout_layout)
    public void onLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout").setMessage("Do you want to logout from this app ?").setCancelable(false);
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                GeoPreference.getInstance().setAccessTokenAuth0("");
                GeoPreference.getInstance().setUsername("");
                GeoPreference.getInstance().setUserId("");
                GeoPreference.getInstance().setUserEmail("");
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.create().show();


    }
    @OnClick(R.id.developer_layout)
    public void onDevOps(){
        Intent intent=new Intent(getActivity(), DeveloperActivity.class);
        startActivity(intent);
    }
}
