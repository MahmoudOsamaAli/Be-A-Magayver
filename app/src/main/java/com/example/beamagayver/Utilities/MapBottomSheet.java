package com.example.beamagayver.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.beamagayver.R;
import com.example.beamagayver.pojo.LocationModel;
import com.example.beamagayver.view.activities.HomeActivity;
import com.example.beamagayver.view.activities.AddPostActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

public class MapBottomSheet extends BottomSheetDialogFragment implements OnMapReadyCallback
        , GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener
        , View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener
        , LocationListener {

    private static final String TAG = "MapBottomSheet";
    private static final float DEFAULT_ZOOM = 15f;
    private GoogleMap mMap;
    private LatLng currLocation;
    private DialogListener listener;
    private Activity mContext;
    private Marker marker;
    private LocationModel locationModel;
    private GetLocation getLocation;
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


    public MapBottomSheet() {
    }

    public MapBottomSheet(LocationModel model) {
        this.locationModel = model;
    }

    private void setViews() {
        currLocation = new LatLng(locationModel.getLatitude(), locationModel.getLongitude());
        cityEditText.setText(locationModel.getCity());
        streetEditText.setText(locationModel.getStreet());
        String country = locationModel.getCountry();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.country_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countriesSpinner.setAdapter(adapter);
        if (country != null) {
            int spinnerPosition = adapter.getPosition(country);
            countriesSpinner.setSelection(spinnerPosition);
        }
    }

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
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (isServiceSDK()) init();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mContext = (AddPostActivity) context;
            listener = (DialogListener) mContext;
        } catch (Exception e) {
            e.printStackTrace();
            mContext = (HomeActivity) context;
        }
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
        cityEditText.setOnFocusChangeListener(this);
        streetEditText.setOnFocusChangeListener(this);
        closeButton.setOnClickListener(this);
        getLocationButton.setOnClickListener(this);
        countriesSpinner.setOnItemSelectedListener(this);
        if (locationModel != null) setViews();
        else getLocation = new GetLocation(mContext, getActivity(), this);
        initMap();
    }

    private void initMap() {
        Log.i(TAG, "initMap: ");
        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
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
            if (currLocation != null && !selectedCountry.isEmpty() &&
                    !Objects.requireNonNull(streetEditText.getText()).toString().isEmpty() &&
                    !Objects.requireNonNull(cityEditText.getText()).toString().isEmpty()) {
                Log.i(TAG, "onClick: currLocation not null");
                locationModel = new LocationModel(selectedCountry, cityEditText.getText().toString(),
                        streetEditText.getText().toString(), currLocation.longitude, currLocation.latitude);
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
        if (marker != null) {
            marker.remove();
        }
        currLocation = latLng;
        marker = mMap.addMarker(new MarkerOptions()
                .title("X = " + latLng.latitude + " Y = " + latLng.longitude)
                .position(currLocation).draggable(true).visible(true));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getLocation = new GetLocation(mContext, getActivity(), this);
        Toast.makeText(mContext, "Getting your location", Toast.LENGTH_SHORT).show();
        if (currLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, DEFAULT_ZOOM));
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(this);
        if (currLocation != null) {
            Log.i(TAG, "onMapReady: current location is not null");
            marker = mMap.addMarker(new MarkerOptions().position(currLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, DEFAULT_ZOOM));
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //spinner
        selectedCountry = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // spinner
    }

    @Override
    public void onFocusChange(View view, boolean focused) {
        InputMethodManager keyboard = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focused && keyboard != null)
            keyboard.showSoftInput(view, 0);
        else if (keyboard != null) {
            keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currLocation = new LatLng(location.getLatitude(), location.getLongitude());
        Log.i(TAG, "onLocationChanged: lon " + location.getLatitude() + " ,lat " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(mContext, "Getting your location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        new AlertDialog.Builder(mContext)
                .setMessage("Open GPS to calculate how are sessions far away from you")
                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss())
                .create().show();
    }
}