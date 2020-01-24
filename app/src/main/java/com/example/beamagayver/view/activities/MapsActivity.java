package com.example.beamagayver.view.activities;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.beamagayver.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
        , GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener {
    private static final String TAG = "MapsActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_LOCATION_CODE = 99;
    private boolean mLocationPermissionsGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private LatLng curr;
    private static final float DEFAULT_ZOOM = 15f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (isServiceSDK()) {
            init();
        }
    }

    public boolean isServiceSDK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            Log.i(TAG, "isServiceSDK: Googleplay services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog d = GoogleApiAvailability.getInstance().getErrorDialog(this, available, 1000);
            d.show();
        } else {
            Toast.makeText(this, "you can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void init() {
        getLocationPermissions();
    }

    private void getLocationPermissions() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        REQUEST_LOCATION_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_LOCATION_CODE);
        }
    }


    private void getDeviceLocation() {
        Log.i(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionsGranted) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager != null) {
                    boolean b = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                            LocationManager.NETWORK_PROVIDER);
                    if (b) {
                        Log.i(TAG, "getDeviceLocation: b = " + b);
                        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                            if (location != null) {
                                double longitude = location.getLongitude();
                                double latitude = location.getLatitude();
                                curr = new LatLng(latitude, longitude);
                                Log.i(TAG, "getDeviceLocation: long " + longitude + " lat " + latitude);
                            } else {
                                Log.i(TAG, "getDeviceLocation: location is null");
                            }
                        }).addOnCompleteListener(task -> {
                            Log.i(TAG, "getDeviceLocation: location has completed");
                        });
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Enable GPS : Error getting your location")
                                .setPositiveButton("Ok", null)
                                .show();
                        Log.i(TAG, "getDeviceLocation: b = " + b);
                    }
                }

//                final Task location = mFusedLocationProviderClient.getLastLocation();
//                location.addOnCompleteListener(task -> {
//                    if(task.isSuccessful()){
//                        Log.i(TAG, "onComplete: found location!");
//                        Location currentLocation = (Location) task.getResult();
//                        if (currentLocation != null) {
//                            Log.i(TAG, "getDeviceLocation: current location is not null");
//                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())
//                            );
//                        }
//
//                    }else{
//                        Log.i(TAG, "onComplete: current location is null");
//                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        } catch (SecurityException e) {
            Log.i(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        getDeviceLocation();
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionsGranted = false;
        Log.i(TAG, "onRequestPermissionsResult: request permission result is called");
        if (requestCode == REQUEST_LOCATION_CODE) {
            Log.i(TAG, "onRequestPermissionsResult: request code = " + requestCode);
            //init map
            mLocationPermissionsGranted = true;
            initMap();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(this);
        if (mLocationPermissionsGranted) {
            if (curr != null) {
                Log.i(TAG, "onMapReady: current location is not null");
                mMap.addMarker(new MarkerOptions().position(curr));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(curr));
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getLocationPermissions();
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(latLng).title("X = " + latLng.latitude + " Y = " + latLng.longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
