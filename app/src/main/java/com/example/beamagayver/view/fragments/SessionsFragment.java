package com.example.beamagayver.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.pojo.Image;
import com.example.beamagayver.view.adapters.SessionsAdapter;

import java.util.ArrayList;

public class SessionsFragment extends Fragment {

    RecyclerView RV;
    ArrayList<Image> imageList;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sessions, container, false);
        RV = view.findViewById(R.id.RV_sessions);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        RV.setLayoutManager(manager);
        RV.setAdapter(new SessionsAdapter(getActivity() , imageList));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageList = new ArrayList<>();
        generateImageList();
    }

    private void generateImageList() {
        imageList.add(new Image("https://www.tripinsiders.net/wp-content/uploads/2016/12/sydnayxmas-768x512.jpg"));
        imageList.add(new Image("https://www.tripinsiders.net/wp-content/uploads/2016/12/sydnayxmas-768x512.jpg"));
        imageList.add(new Image("https://www.tripinsiders.net/wp-content/uploads/2016/12/sydnayxmas-768x512.jpg"));
        imageList.add(new Image("https://www.tripinsiders.net/wp-content/uploads/2016/12/sydnayxmas-768x512.jpg"));
        imageList.add(new Image("https://www.tripinsiders.net/wp-content/uploads/2016/12/sydnayxmas-768x512.jpg"));
        imageList.add(new Image("https://www.tripinsiders.net/wp-content/uploads/2016/12/sydnayxmas-768x512.jpg"));
        imageList.add(new Image("https://www.tripinsiders.net/wp-content/uploads/2016/12/sydnayxmas-768x512.jpg"));
        imageList.add(new Image("https://www.tripinsiders.net/wp-content/uploads/2016/12/sydnayxmas-768x512.jpg"));
        imageList.add(new Image("https://www.tripinsiders.net/wp-content/uploads/2016/12/sydnayxmas-768x512.jpg"));
    }
}
