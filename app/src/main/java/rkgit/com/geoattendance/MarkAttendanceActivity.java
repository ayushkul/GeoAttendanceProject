package rkgit.com.geoattendance;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rkgit.com.geoattendance.models.AttendenceModel;
import rkgit.com.geoattendance.managers.NodeApi;
import rkgit.com.geoattendance.managers.NodeApiClient;
import rkgit.com.geoattendance.models.ResponseModel;
import rkgit.com.geoattendance.utility.GeoPreference;

public class MarkAttendanceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleMap map;
    SupportMapFragment mapFragment;
    LatLng portLatLong;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    int shadeColor = 0x44ff0000;
    LocationManager locationManager;
    Circle circle;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mark_attendance);
        ButterKnife.bind(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frame);
        setupToolbar();
        dialog = new ProgressDialog(this);
        portLatLong = new LatLng(28.701241, 77.441576);//getPortLatLong();
        mapFragment.getMapAsync(this);
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mark Attendance");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, this);
        map = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
        googleMap.addMarker(new MarkerOptions().position(portLatLong).title("RKGIT"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(portLatLong, 17.0f));
        CircleOptions circleOptions = new CircleOptions().center(portLatLong).radius(100).fillColor(shadeColor).strokeColor(Color.BLUE);
        circle = googleMap.addCircle(circleOptions);
        buildApiClient();
    }

    private void buildApiClient() {

    }

    @OnClick(R.id.check_in_button)
    void validateLocation() {
        float[] distance = new float[2];
        if (map.getMyLocation().getLatitude() > 0 && map.getMyLocation().getLongitude() > 0) {
            Location.distanceBetween(map.getMyLocation().getLatitude(),
                    map.getMyLocation().getLongitude(), circle.getCenter().latitude,
                    circle.getCenter().longitude, distance);

            if (distance[0] > circle.getRadius()) {
                map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(portLatLong, 17.0f));
                Toast.makeText(this, "You are outside the Institute", Toast.LENGTH_SHORT).show();
            } else if (distance[0] < circle.getRadius()) {
                dialog.setMessage("Fetching data");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                markAttendence();
            }
        } else {
            Toast.makeText(this, "Location problem", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.check_out_button)
    void checkoutClick() {
        float[] distance = new float[2];
        Location.distanceBetween(map.getMyLocation().getLatitude(),
                map.getMyLocation().getLongitude(), circle.getCenter().latitude,
                circle.getCenter().longitude, distance);

        if (distance[0] > circle.getRadius()) {
            map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(portLatLong, 17.0f));
            Toast.makeText(this, "You are outside the Institute", Toast.LENGTH_SHORT).show();
        } else if (distance[0] < circle.getRadius()) {
            dialog.setMessage("Fetching data");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            checkOut();
        }

    }

    private void checkOut() {
        if (GeoPreference.getInstance().getIpAddress() != null)
            NodeApiClient.changeBaseUrl(GeoPreference.getInstance().getIpAddress());
        NodeApi nodeApiClient = NodeApiClient.getClient().create(NodeApi.class);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(new Date());
        String dateValue = formattedDate.substring(0, 10);
        String time = formattedDate.substring(11, 16);
        AttendenceModel attendenceModel = new AttendenceModel(GeoPreference.getInstance().getUserId(), dateValue, time);
        Call<ResponseModel> call = nodeApiClient.checkOut(attendenceModel);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                ResponseModel res = response.body();
                Toast.makeText(MarkAttendanceActivity.this, res.data, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(MarkAttendanceActivity.this, "Sorry for the inconvenience, Servers are under maintenance !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void markAttendence() {
        if (GeoPreference.getInstance().getIpAddress() != null)
            NodeApiClient.changeBaseUrl(GeoPreference.getInstance().getIpAddress());
        NodeApi nodeApiClient = NodeApiClient.getClient().create(NodeApi.class);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(new Date());
        String dateValue = formattedDate.substring(0, 10);
        String time = formattedDate.substring(11, 16);
        AttendenceModel attendenceModel = new AttendenceModel(GeoPreference.getInstance().getUserId(), dateValue, time);
        Call<ResponseModel> call = nodeApiClient.markAttendance(attendenceModel);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                ResponseModel res = response.body();
                Toast.makeText(MarkAttendanceActivity.this, res.data, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(MarkAttendanceActivity.this, "Sorry for the inconvenience, Servers are under maintenance !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("It's You!"));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
