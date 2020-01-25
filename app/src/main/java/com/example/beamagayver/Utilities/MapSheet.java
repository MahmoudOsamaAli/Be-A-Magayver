package com.example.beamagayver.Utilities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.beamagayver.R;
import com.example.beamagayver.pojo.LocationModel;
import com.example.beamagayver.view.activities.AddPostActivity;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapSheet extends BottomSheetDialogFragment implements OnMapReadyCallback
        , GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener
        , View.OnClickListener  , AdapterView.OnItemSelectedListener {

    private static final String TAG = "MapSheet";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_LOCATION_CODE = 99;
    private static final float DEFAULT_ZOOM = 15f;
    private boolean mLocationPermissionsGranted;
    private GoogleMap mMap;
    private LatLng currLocation;
    private onGetLocation listener;
    private Activity mContext;
    @BindView(R.id.get_location)
    Button getLocationButton;
    @BindView(R.id.icon_close)
    ImageView closeButton;
    @BindView(R.id.map_activity_countries_spinner)
    Spinner countriesSpinner;
    @BindView(R.id.map_activity_city_edit_text)
    TextInputEditText cityEditText;
    @BindView(R.id.map_activity_street_edit_text)
    TextInputEditText streetEditText;
    private String selectedCountry;
    private LocationModel locationModel;


    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog: ");
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;
            setupFullHeight(d);
            try {
                Field behaviorField = d.getClass().getDeclaredField("behavior");
                behaviorField.setAccessible(true);
                final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(d);
                if (behavior != null) {
                    behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        }
                    });
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.map_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (isServiceSDK()) init();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (AddPostActivity) context;
        listener = (onGetLocation) mContext;
    }

    private boolean isServiceSDK() {
        Log.i(TAG, "isServiceSDK: ");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext);
        if (available == ConnectionResult.SUCCESS) {
            Log.i(TAG, "isServiceSDK: Googleplay services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog d = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, 1000);
            d.show();
        } else {
            Toast.makeText(mContext, "you can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void init() {
        Log.i(TAG, "init: ");
        closeButton.setOnClickListener(this);
        getLocationButton.setOnClickListener(this);
        countriesSpinner.setOnItemSelectedListener(this);
        getLocationPermissions();
    }

    private void getLocationPermissions() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(mContext.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(mContext.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                        permissions,
                        REQUEST_LOCATION_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    permissions,
                    REQUEST_LOCATION_CODE);
        }
    }

    private void initMap() {
        Log.i(TAG, "initMap: ");
        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mapFragment);
        fragmentTransaction.commit();
        getDeviceLocation();
        mapFragment.getMapAsync(this);

    }

    private void getDeviceLocation() {
        Log.i(TAG, "getDeviceLocation: getting the devices current location");
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        try {
            if (mLocationPermissionsGranted) {
                LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    boolean b = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                            LocationManager.NETWORK_PROVIDER);
                    if (b) {
                        Log.i(TAG, "getDeviceLocation: b = " + b);
                        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                            if (location != null) {
                                currLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                Log.i(TAG, "getDeviceLocation: long " + location.getLongitude() + " lat " + location.getLatitude());
                            } else {
                                Log.i(TAG, "getDeviceLocation: location is null");
                            }
                        }).addOnCompleteListener(task -> {
                            Log.i(TAG, "getDeviceLocation: location has completed");
                        });
                    } else {
                        new AlertDialog.Builder(mContext)
                                .setTitle("Enable GPS : Error getting your location")
                                .setPositiveButton("Ok", null)
                                .show();
                        Log.i(TAG, "getDeviceLocation: b = " + b);
                    }
                }
            }
        } catch (SecurityException e) {
            Log.i(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(Objects.requireNonNull(bottomSheet));
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) Objects.requireNonNull(getContext())).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(getLocationButton)) {
            if (currLocation != null &&! selectedCountry.isEmpty() &&
                    !streetEditText.getText().toString().isEmpty() && !cityEditText.getText().toString().isEmpty()) {
                Log.i(TAG, "onClick: currLocation not null");
                locationModel = new LocationModel(selectedCountry , cityEditText.getText().toString() ,
                        streetEditText.getText().toString() , currLocation.longitude , currLocation.latitude);
                listener.onGetLocationListener(locationModel);
                dismiss();
            } else {
                Log.i(TAG, "onClick: currLocation not null");
                new AlertDialog.Builder(mContext)
                        .setTitle("Error")
                        .setMessage("You have to select a county , location , city and street for the Session")
                        .create().show();
            }
        } else if (view.equals(closeButton)) {
            dismiss();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("X = " + latLng.latitude + " Y = " + latLng.longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.clear();
        getLocationPermissions();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(this);
        if (mLocationPermissionsGranted) {
            if (currLocation != null) {
                Log.i(TAG, "onMapReady: current location is not null");
                mMap.addMarker(new MarkerOptions().position(currLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, DEFAULT_ZOOM));
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedCountry = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface onGetLocation {
        void onGetLocationListener(LocationModel model);
    }
}