package com.example.beamagayver.view.fragments;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.data.FireStoreProcess;
import com.example.beamagayver.pojo.Post;
import com.example.beamagayver.view.activities.AddPostActivity;
import com.example.beamagayver.view.adapters.SessionAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SessionsFragment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference = db.collection("Sessions");
    private SessionAdapter adapter1;
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
        init();
    }

    private void init() {
        try {
            Query query = reference.orderBy("mPostTime", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                    .setQuery(query, Post.class)
                    .build();
            adapter1 = new SessionAdapter(options, getContext());
            RV.setHasFixedSize(true);
            RV.setLayoutManager(new LinearLayoutManager(getContext()));
            RV.setAdapter(adapter1);
            progressBar.setVisibility(View.GONE);
            noSessionsImage.setVisibility(View.GONE);
            noSessionsTV.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "init: " + e.getMessage());
        }
        addPost.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter1.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter1.stopListening();
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_post) {
            Log.i(TAG, "onClick: begin transaction for add post Activity");
            startActivity(new Intent(getContext(), AddPostActivity.class));
        }
    }


}
