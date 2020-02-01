package com.example.beamagayver.view.fragments.activities;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.data.FireStoreProcess;
import com.example.beamagayver.data.PrefManager;
import com.example.beamagayver.pojo.UserActivity;
import com.example.beamagayver.view.activities.HomeActivity;
import com.example.beamagayver.view.adapters.ActivitiesAdapter;
import com.example.beamagayver.view.adapters.RecyclerViewListClicked;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitiesFragment extends Fragment implements listenToFireBase , RecyclerViewListClicked {
    private static final String TAG = "ActivitiesFragment";
    @BindView(R.id.RV_activities)
    RecyclerView RV;
    private ActivitiesAdapter adapter;
    private Context mContext;
    private ArrayList<UserActivity> data;
    private PrefManager manager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Log.i(TAG, "onViewCreated: ");
        init();
    }

    private void init() {
        try {
            manager = new PrefManager(mContext);
            data = new ArrayList<>();
            adapter = new ActivitiesAdapter(data, mContext, this);
            RV.setHasFixedSize(true);
            RV.setLayoutManager(new LinearLayoutManager(mContext));
            RV.setAdapter(adapter);
            listenToFireBase();
            Log.i(TAG, "init: created RV");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "init: " + e.getMessage());
        }
    }

    private void listenToFireBase() {
        String type = manager.readString(getResources().getString(R.string.account_type));
        String userID = manager.readString(getResources().getString(R.string.account_id));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(type).document(userID).collection("activities")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
           if(e == null && queryDocumentSnapshots != null){
               List<DocumentSnapshot> documents= queryDocumentSnapshots.getDocuments();
               Log.i(TAG, "listenToFireBase: " + documents.size());
               data.clear();
               for(DocumentSnapshot dc : documents){
                   UserActivity activity = dc.toObject(UserActivity.class);
                   data.add(activity);
               }
               Collections.sort(data);
               Collections.reverse(data);
               adapter.notifyDataSetChanged();
           }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;

    }

    @Override
    public void onDataChanged() {
        Log.i(TAG, "onDataChanged: ");
        listenToFireBase();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Log.i(TAG, "recyclerViewListClicked: position is = " + position);
        postBottomSheet dialog = new postBottomSheet(data.get(position), getActivity());
        dialog.show (Objects.requireNonNull(getActivity()).getSupportFragmentManager() , dialog.getTag());
    }
}
