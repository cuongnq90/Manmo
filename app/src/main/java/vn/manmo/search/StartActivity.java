package vn.manmo.search;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import vn.manmo.search.utils.AppUtils;


public class StartActivity extends AppCompatActivity implements LocationListener {

    private Context mContext;
    private TextView mBtnReload;
    private TextView mTvCheckInternet;
    private TextView mTvCheckGPS;
    private ProgressBar mLoading;
    private TextView mTvGetLocation;
    LocationManager mLocationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1;
    private boolean mGetLocation = false;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6666;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean nextActivity = false;
    FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mContext = this;
        setupView();
    }

    private void setupView() {
        mLoading = findViewById(R.id.loading);
        mBtnReload = findViewById(R.id.btn_reload);
        mTvCheckInternet = findViewById(R.id.tv_check_internet);
        mTvCheckGPS = findViewById(R.id.tv_check_gps);
        mTvGetLocation = findViewById(R.id.btn_get_location);

        mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);

        mBtnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnReload.setVisibility(View.GONE);
                mLoading.setVisibility(View.VISIBLE);
                mTvGetLocation.setVisibility(View.VISIBLE);
                mTvCheckInternet.setVisibility(View.GONE);
                mTvCheckGPS.setVisibility(View.GONE);
                preCheck();
            }
        });
        preCheck();
    }

    private void preCheck() {
        boolean checkState = true;
        if (!AppUtils.isInternetConnected(mContext)) {
            mTvCheckInternet.setVisibility(View.VISIBLE);
            checkState = false;
        }
        boolean isGPSOn = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSOn) {
            mTvCheckGPS.setVisibility(View.VISIBLE);
            checkState = false;
        }
        if (checkState) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getLocationPermission();
            } else {
                getDeviceLocation();
            }
        } else {
            mLoading.setVisibility(View.GONE);
            mTvGetLocation.setVisibility(View.GONE);
            mBtnReload.setVisibility(View.VISIBLE);
        }

    }
//

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.INTERNET,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation();
        } else {
            finish();
        }
    }

    private void getDeviceLocation() {
        CountDownTimer countDownTimer = new CountDownTimer(7000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 4000 && millisUntilFinished > 3000) {
                    if(nextActivity) {
                        this.cancel();
                        AppUtils.mLocationUpdated = true;
                        startActivity(new Intent(StartActivity.this, MainActivity.class));
                        finish();
                    } else {
                        nextActivity = true;
                    }
                }
            }

            @Override
            public void onFinish() {
                AppUtils.mLocationUpdated = false;
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        };
        countDownTimer.start();
        try {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location lastKnownLocation = (Location) task.getResult();
                        AppUtils.mLatitude = lastKnownLocation.getLatitude();
                        AppUtils.mLongitude = lastKnownLocation.getLongitude();
                        if(nextActivity) {
                            startActivity(new Intent(StartActivity.this, MainActivity.class));
                            finish();
                        } else {
                            nextActivity = true;
                        }
                    }
                }
            });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(!mGetLocation) {
            AppUtils.mLatitude = location.getLatitude();
            AppUtils.mLongitude = location.getLongitude();
            mGetLocation = true;
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
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
}
