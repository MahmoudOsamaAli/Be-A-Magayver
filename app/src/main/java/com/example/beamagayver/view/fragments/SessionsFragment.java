package com.example.beamagayver.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.pojo.Post;
import com.example.beamagayver.view.activities.addPost.AddPostActivity;
import com.example.beamagayver.view.adapters.SessionAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SessionsFragment";
    private static final int LOCATION_REQUEST_CODE = 1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference = db.collection("Sessions");
    private FusedLocationProviderClient fusedLocationClient;
    private Activity mActicity;
    private Context mContext;
    private Location mCurrentLocation;
    private SessionAdapter adapter;
    @BindView(R.id.RV_sessions)
    RecyclerView RV;
    @BindView(R.id.add_post)
    FloatingActionButton addPost;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_sessions_image)
    ImageView noSessionsImage;
    @BindView(R.id.no_sessions_TV)
    TextView noSessionsTV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sessions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        progressBar.setVisibility(View.VISIBLE);
//        initLocation();
        init();
    }

    private void init() {
        try {

            Query query = reference.orderBy("mPostTime", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                    .setQuery(query, Post.class)
                    .build();
            adapter = new SessionAdapter(options, mContext , mActicity );
            RV.setHasFixedSize(true);
            RV.setLayoutManager(new LinearLayoutManager(getContext()));
            RV.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            noSessionsImage.setVisibility(View.GONE);
            noSessionsTV.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "init: " + e.getMessage());
        }
        addPost.setOnClickListener(this);
    }

    private void initLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext())
                    , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext()
                    , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity())
                        , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                                , Manifest.permission.ACCESS_FINE_LOCATION}
                                , LOCATION_REQUEST_CODE);
            } else {
                Log.i(TAG, "initLocation: called");
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mCurrentLocation = location;
                                adapter.notifyDataSetChanged();
                                Log.i(TAG, "onSuccess: location is not null lat = "
                                        + location.getLatitude() + " , lon = + " +location.getLongitude());
                            }else{
                                new AlertDialog.Builder(getContext())
                                        .setMessage("Open GPS to calculate how far sessions are away from you")
                                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                                            dialogInterface.dismiss();
                                        })
                                        .create().show();
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "initLocation: catched an exception" + e.getMessage());
        }
    }

//    private void getDeviceLocation() {
//        Log.i(TAG, "getDeviceLocation: getting the devices current location");
//        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
//        try {
//            if (mLocationPermissionsGranted) {
//                LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
//                if (locationManager != null) {
//                    boolean b = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//                            LocationManager.NETWORK_PROVIDER);
//                    if (b) {
//                        Log.i(TAG, "getDeviceLocation: b = " + b);
//                        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
//                            if (location != null) {
//                                currLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                                Log.i(TAG, "getDeviceLocation: long " + location.getLongitude() + " lat " + location.getLatitude());
//                            } else {
//                                Log.i(TAG, "getDeviceLocation: location is null");
//                            }
//                        }).addOnCompleteListener(task -> {
//                            Log.i(TAG, "getDeviceLocation: location has completed");
//                        });
//                    } else {
//                        new AlertDialog.Builder(mContext)
//                                .setTitle("Enable GPS : Error getting your location")
//                                .setPositiveButton("Ok", null)
//                                .show();
//                        Log.i(TAG, "getDeviceLocation: b = " + b);
//                    }
//                }
//            }
//        } catch (SecurityException e) {
//            Log.i(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActicity = (Activity) context;
        mContext = context;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_post) {
            Log.i(TAG, "onClick: begin transaction for add post Activity");
            startActivity(new Intent(getContext(), AddPostActivity.class));
        }
    }


}
