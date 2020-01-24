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
import com.example.beamagayver.data.onListenToFireStore;
import com.example.beamagayver.pojo.Post;
import com.example.beamagayver.view.activities.AddPostActivity;
import com.example.beamagayver.view.adapters.SessionsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionsFragment extends Fragment implements View.OnClickListener, onListenToFireStore {

    private static final String TAG = "SessionsFragment";

    ArrayList<Post> posts;
    @BindView(R.id.RV_sessions)
    RecyclerView RV;
    @BindView(R.id.add_post)
    FloatingActionButton addPost;
    SessionsAdapter adapter;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    FireStoreProcess process;
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
        posts = new ArrayList<>();
        getPosts();

    }


    private void init() {
        try {
            adapter = new SessionsAdapter(getContext(), posts);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            RV.setLayoutManager(manager);
            RV.setAdapter(adapter);
            Log.i(TAG, "init: called");
            if (posts != null && !posts.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                noSessionsImage.setVisibility(View.GONE);
                noSessionsTV.setVisibility(View.GONE);
            } else {
                Log.i(TAG, "init: posts is null");
                noSessionsImage.setVisibility(View.VISIBLE);
                noSessionsTV.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "init: " + e.getMessage());
        }
        process = new FireStoreProcess(this);
        FireStoreProcess.listenToFireBase(posts);
        addPost.setOnClickListener(this);
    }

    private void getPosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference sessions = db.collection("Sessions");
        sessions.get().addOnSuccessListener(result -> {
            if (result != null) {
                for (DocumentSnapshot doc : result) {
                    Post p = doc.toObject(Post.class);
                    posts.add(p);
                    Log.i(TAG, "onComplete: posts size during the loop = " + posts.size());
                }
            }
        }).addOnCompleteListener(Objects.requireNonNull(getActivity()), task ->
                init());
                Log.i(TAG, "getPosts: posts size after the task = " + posts.size());
//                init());

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_post) {
            Log.i(TAG, "onClick: begin transaction for add post Activity");
            startActivity(new Intent(getContext(), AddPostActivity.class));
        }
    }

    @Override
    public void onPostAdded(Post post) {
        Log.i(TAG, "onPostAdded: interface is called");
        if (post != null) {
            posts.add(post);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPostModified(Post post) {
        Log.i(TAG, "onPostModified: interface is called");
        try {
            if (post != null) {
                String postID = post.getmPostID();
                for (Post p : posts) {
                    if (postID != null && p.getmPostID().equals(postID)) {
                        posts.set(posts.indexOf(p), post);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i(TAG, "onPostModified: " + ex.getMessage());
        }
    }

    @Override
    public void onPostDeleted(Post post) {
        Log.i(TAG, "onPostDeleted: interface is called");
        posts.remove(post);
        adapter.notifyDataSetChanged();
    }


}
