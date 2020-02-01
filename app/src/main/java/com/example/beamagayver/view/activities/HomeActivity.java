package com.example.beamagayver.view.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.beamagayver.R;
import com.example.beamagayver.view.adapters.MyPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity  {

    private static final String TAG = "HomeActivity";

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    MyPagerAdapter adapter;
    private static final int LOCATION_REQUEST_CODE = 1;
    public static Location mCurrentLocation;
    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            double lon = location.getLongitude();
            double lat = location.getLatitude();
            mCurrentLocation = location;
            Log.i(TAG, "onLocationChanged: lon " + lon + " ,lat " + lat);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(HomeActivity.this, "Getting Location", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(HomeActivity.this, "Open GPS", Toast.LENGTH_SHORT).show();
        }
    };

    public static Location getLocation() {
        return mCurrentLocation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        try {
            adapter = new MyPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            initLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        private void initLocation() {
            try {
                if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this)
                        , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this
                        , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this
                            , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                                    , Manifest.permission.ACCESS_FINE_LOCATION}
                            , LOCATION_REQUEST_CODE);
                    Log.i(TAG, "initLocation: no permission to get location");
                } else {
                    Log.i(TAG, "initLocation: called");
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (lm != null) {
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10,
                                locationListener);
                        mCurrentLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "initLocation: catched an exception" + e.getMessage());
            }
        }

}
