package rkgit.com.geoattendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.auth0.android.Auth0;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.lock.AuthButtonSize;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.InitialScreen;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.UsernameStyle;
import com.auth0.android.lock.utils.CustomField;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.management.ManagementException;
import com.auth0.android.management.UsersAPIClient;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import rkgit.com.geoattendance.managers.ApiClient;
import rkgit.com.geoattendance.managers.Auth0Api;
import rkgit.com.geoattendance.models.UserInfoModel;
import rkgit.com.geoattendance.modules.dashboard.DashboardActivity;
import rkgit.com.geoattendance.utility.GeoPreference;

public class AuthenticationActivity extends AppCompatActivity {
    private com.auth0.android.lock.Lock lock;
    String email, userId, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        redirectToAuth();
    }

    private void redirectToAuth() {
        com.auth0.android.lock.Lock.Builder builder = com.auth0.android.lock.Lock.newBuilder(getAccount(), callback);
        builder.loginAfterSignUp(true);
        builder.withAuthButtonSize(AuthButtonSize.BIG);
        builder.withUsernameStyle(UsernameStyle.EMAIL);
        builder.initialScreen(InitialScreen.LOG_IN);
        builder.setDefaultDatabaseConnection(getString(R.string.auth0_connection_name));
        List<CustomField> customFields = new ArrayList<>();
        customFields.add(new CustomField(R.drawable.original, CustomField.FieldType.TYPE_NAME, getString(R.string.name_key), R.string.name_hint));
        customFields.add(new CustomField(R.drawable.phone, CustomField.FieldType.TYPE_PHONE_NUMBER, getString(R.string.phone_number_key), R.string.phone_number_hint));
        builder.withSignUpFields(customFields);
        lock = builder.build(this);
        startActivity(lock.newIntent(this));
    }

    private Auth0 getAccount() {
        Auth0 account = new Auth0(getString(R.string.com_auth0_client_id), getString(R.string.com_auth0_domain));
        account.setOIDCConformant(true);
        account.setLoggingEnabled(true);
        return account;

    }

    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            String accessToken = credentials.getAccessToken();
            GeoPreference.getInstance().setAccessTokenAuth0(accessToken);
            saveUserInfo();
            //redirectToDash();
            //credentials.getType();
        }

        @Override
        public void onCanceled() {

        }

        @Override
        public void onError(LockException error) {
            //Toast.makeText(getApplicationContext(),"Invalid credentials",Toast.LENGTH_LONG).show();
            //AppUtility.showToastAtBottom(getApplicationContext(), getString(R.string.invalid_credentials));
        }
    };

    private void saveUserInfo() {
        String token = GeoPreference.getInstance().getAccessTokenAuth0();
        Auth0Api change = ApiClient.getClient().create(Auth0Api.class);
        Call<UserInfoModel> call = change.getUserdata("Bearer " + token);
        call.enqueue(new Callback<UserInfoModel>() {
            @Override
            public void onResponse(Call<UserInfoModel> call, retrofit2.Response<UserInfoModel> response) {
                email = response.body().getEmail();
                userId = response.body().getSub();
                username = response.body().getName();
                username = response.body().getNickname();
                GeoPreference.getInstance().setUserId(userId);
                GeoPreference.getInstance().setUserEmail(email);
                GeoPreference.getInstance().setUsername(username);
//                updateUsermetaData();
                redirectToDash();
            }

            @Override
            public void onFailure(Call<UserInfoModel> call, Throwable t) {
                //Toast.makeText(SplashActivity.this, R.string.change_password_mail_failure, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void updateUsermetaData() {
        String token = GeoPreference.getInstance().getAccessTokenAuth0();
        String userID = GeoPreference.getInstance().getUserId();
        Auth0 auth0 = new Auth0(this);
        UsersAPIClient usersClient = new UsersAPIClient(auth0, token);
        usersClient.getProfile(userID)
                .start(new BaseCallback<UserProfile, ManagementException>() {
                    @Override
                    public void onSuccess(UserProfile profile) {

//                        GeoPreference.getInstance().setUsername((String) profile.getUserMetadata().get("name"));
                        redirectToDash();
                    }

                    @Override
                    public void onFailure(ManagementException error) {
                        //show error
                        redirectToDash();
                        //oastT.makeText(AuthenticationActivity.this, "fk", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectToDash() {
        Intent intent = new Intent(AuthenticationActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

