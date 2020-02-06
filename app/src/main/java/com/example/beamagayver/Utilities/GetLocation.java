package com.example.beamagayver.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class GetLocation {
    private static final String TAG = "GetLocation";
    private static final int REQUEST_LOCATION_CODE = 99;
    private Context mContext;
    private Activity activity;
    private  LocationListener locationListener;

    public GetLocation (Context context , Activity activity , LocationListener listener){
        this.mContext = context;
        this.activity = activity;
        this.locationListener = listener;
        getDeviceLocation();
    }

    private void getDeviceLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(mContext
                    , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(mContext
                    , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity
                        , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                                , Manifest.permission.ACCESS_FINE_LOCATION}
                        , REQUEST_LOCATION_CODE);
                Log.i(TAG, "getDeviceLocation: no permission to get location");
            } else {
                Log.i(TAG, "getDeviceLocation: called");
                LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                if (lm != null) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10,
                            locationListener);
                    Location currLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    locationListener.onLocationChanged(currLocation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "getDeviceLocation: catched an exception" + e.getMessage());
        }
    }
}

