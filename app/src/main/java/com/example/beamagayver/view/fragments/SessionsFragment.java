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
import com.example.beamagayver.data.DummyData;
import com.example.beamagayver.pojo.Image;
import com.example.beamagayver.pojo.Post;
import com.example.beamagayver.view.adapters.SessionsAdapter;

import java.util.ArrayList;

public class SessionsFragment extends Fragment {

    RecyclerView RV;
    ArrayList<Post> posts;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sessions, container, false);
        RV = view.findViewById(R.id.RV_sessions);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        RV.setLayoutManager(manager);
        RV.setAdapter(new SessionsAdapter(getActivity() , posts));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posts = getPosts();

    }
    private ArrayList<Post> getPosts() {
        ArrayList<Post> data = new ArrayList<>();
        Post post = new Post(R.drawable.ic_profile , "Mahmoud Osama" ,
                "1 hour ago" ,getString(R.string.post_contnent));
        data.add(post);
        post = new Post(R.drawable.ic_profile , "Ali Osama" ,
                "2 hour ago" ,getString(R.string.post_contnent));
        data.add(post);
        post = new Post(R.drawable.ic_profile , "Mahmoud Osama" ,
                "1 hour ago" ,getString(R.string.post_contnent));
        data.add(post);
        post = new Post(R.drawable.ic_profile , "Ali Osama" ,
                "2 hour ago" ,getString(R.string.post_contnent));
        data.add(post);
        post = new Post(R.drawable.ic_profile , "Mahmoud Osama" ,
                "1 hour ago" ,getString(R.string.post_contnent));
        data.add(post);
        post = new Post(R.drawable.ic_profile , "Ali Osama" ,
                "2 hour ago" ,getString(R.string.post_contnent));
        data.add(post);
        return data;
    }
}
