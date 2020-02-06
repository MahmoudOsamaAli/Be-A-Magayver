package com.example.beamagayver.view.fragments.sessionFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.data.PrefManager;
import com.example.beamagayver.pojo.Post;
import com.example.beamagayver.view.activities.AddPostActivity;
import com.example.beamagayver.view.adapters.SessionAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionsFragment extends Fragment implements View.OnClickListener , AdapterView.OnItemSelectedListener , showProfile{

    private static final String TAG = "SessionsFragment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference = db.collection("Sessions");
    private Activity activity;
    private Context mContext;
    private SessionAdapter adapter;
    private PrefManager manager;
    private String sort = "mPostTime";
    private Query query =  reference.orderBy(sort, Query.Direction.DESCENDING);
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
    @BindView(R.id.sort_spinner)
    Spinner sortSpinner;
    LinearLayoutManager layoutManager;


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
            sortSpinner.setOnItemSelectedListener(this);
            Log.i(TAG, "init: sort by " + sort);
            FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                    .setQuery(query, Post.class)
                    .build();
            adapter = new SessionAdapter(options, mContext , activity, this );
            RV.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getContext());
            RV.setLayoutManager(layoutManager);
            RV.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            noSessionsImage.setVisibility(View.GONE);
            noSessionsTV.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "init: " + e.getMessage());
        }
        addPost.setOnClickListener(this);
        manager = new PrefManager(mContext);
        String type = manager.readString(getResources().getString(R.string.account_type));
        if(!type.equals("instructor")){
            addPost.hide();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selected = adapterView.getItemAtPosition(i).toString();
        FirestoreRecyclerOptions<Post> options;
        Log.i(TAG, "onItemSelected: selected item = " + selected);
        switch (selected){
            case "Near you" :
                sort = "distance";
                query =  reference.orderBy(sort, Query.Direction.ASCENDING);
                options = new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
                adapter = new SessionAdapter(options, mContext , activity , this);
                RV.setAdapter(adapter);
                adapter.startListening();
                Log.i(TAG, "onItemSelected: Near you");
                break;
            case "Likes":
                sort = "mLikes";
                query =  reference.orderBy(sort, Query.Direction.DESCENDING);
                options = new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
                adapter = new SessionAdapter(options, mContext , activity , this);
                RV.setAdapter(adapter);
                adapter.startListening();
                Log.i(TAG, "onItemSelected: likes");
                break;
            case "Joined":
                sort = "mJoined";
                query =  reference.orderBy(sort, Query.Direction.DESCENDING);
                options = new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
                adapter = new SessionAdapter(options, mContext , activity , this);
                RV.setAdapter(adapter);
                adapter.startListening();
                Log.i(TAG, "onItemSelected: joined");
                break;
            case "Cost":
                sort = "mPrice";
                query =  reference.orderBy(sort, Query.Direction.ASCENDING);
                options = new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
                adapter = new SessionAdapter(options, mContext , activity , this);
                RV.setAdapter(adapter);
                adapter.startListening();
                Log.i(TAG, "onItemSelected: cost");
                break;
            case "Duration" :
                sort = "mDuration";
                query =  reference.orderBy(sort, Query.Direction.ASCENDING);
                options = new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
                adapter = new SessionAdapter(options, mContext , activity , this);
                RV.setAdapter(adapter);
                adapter.startListening();
                Log.i(TAG, "onItemSelected: duration");
                break;
            case "Start Date":
                sort = "mStartDate";
                query =  reference.orderBy(sort, Query.Direction.ASCENDING);
                options = new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
                adapter = new SessionAdapter(options, mContext , activity , this);
                RV.setAdapter(adapter);
                adapter.startListening();
                break;
                default:
                    sort = "mPostTime";
                    query =  reference.orderBy(sort, Query.Direction.DESCENDING);
                    options = new FirestoreRecyclerOptions.Builder<Post>()
                            .setQuery(query, Post.class)
                            .build();
                    adapter = new SessionAdapter(options, mContext , activity , this);
                    RV.setAdapter(adapter);
                    adapter.startListening();
                    Log.i(TAG, "onItemSelected: post time");
        }

        Log.i(TAG, "onItemSelected: start listening adapter");

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onImageClicked(String uid) {
        Log.i(TAG, "onImageClicked: " + uid);
        ProfileBottomSheet sheet = new ProfileBottomSheet(uid);
        sheet.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager() , sheet.getTag());
    }
}
