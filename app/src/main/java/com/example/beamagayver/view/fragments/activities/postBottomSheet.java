package com.example.beamagayver.view.fragments.activities;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.beamagayver.R;
import com.example.beamagayver.Utilities.NumberUtils;
import com.example.beamagayver.data.FireStoreProcess;
import com.example.beamagayver.data.PrefManager;
import com.example.beamagayver.pojo.LikesModel;
import com.example.beamagayver.pojo.LocationModel;
import com.example.beamagayver.pojo.Post;
import com.example.beamagayver.pojo.UserActivity;
import com.example.beamagayver.view.activities.HomeActivity;
import com.example.beamagayver.view.activities.addPost.AddPostActivity;
import com.example.beamagayver.view.activities.addPost.MapBottomSheet;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class postBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "postBottomSheet";

    private Location mCurrentLocation;
    @BindView(R.id.post_owner_imaage)
    CircleImageView postOwnerImaage;
    @BindView(R.id.post_owner_name_tv)
    TextView postOwnerNameTv;
    @BindView(R.id.post_time_tv)
    TextView postTimeTv;
    @BindView(R.id.post_menu)
    ImageView postMenu;
    @BindView(R.id.post_caption)
    TextView postCaption;
    @BindView(R.id.post_car_details)
    TextView postCarDetails;
    @BindView(R.id.post_start_date_time)
    TextView postStartDateTime;
    @BindView(R.id.post_duration)
    TextView postDuration;
    @BindView(R.id.instructor_number)
    TextView instructorNumber;
    @BindView(R.id.cost_tv)
    TextView costTv;
    @BindView(R.id.post_distance)
    TextView postDistance;
    @BindView(R.id.get_location_progress)
    ProgressBar getLocationProgress;
    @BindView(R.id.like_image)
    ImageView likeImage;
    @BindView(R.id.like_count)
    TextView likeCount;
    @BindView(R.id.like_button)
    ConstraintLayout likeButton;
    @BindView(R.id.join_image)
    ImageView joinImage;
    @BindView(R.id.join_count)
    TextView joinCount;
    @BindView(R.id.join_button)
    ConstraintLayout joinButton;
    @BindView(R.id.call_button)
    Button callButton;
    @BindView(R.id.distance_layout)
    LinearLayout distanceLayout;
    private String postID;
    private UserActivity userActivity;
    private String uid;
    private String type;
    private PrefManager manager;
    private Post post;
    private boolean isLiked;
    private boolean isJoined;
    private Activity mActivity;

    public postBottomSheet(UserActivity userActivity, Activity activity) {
        this.userActivity = userActivity;
        this.mActivity = activity;
        postID = userActivity.getPostID();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.session_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();

    }

    private void init() {
        manager = new PrefManager(getContext());
        uid = manager.readString(Objects.requireNonNull(getContext()).getResources().getString(R.string.account_id));
        type = manager.readString(Objects.requireNonNull(getContext()).getResources().getString(R.string.account_type));
        likeButton.setOnClickListener(this);
        joinButton.setOnClickListener(this);
        distanceLayout.setOnClickListener(this);
        mCurrentLocation = HomeActivity.getLocation();
        getPost();
    }

    private void getPost() {
        Log.i(TAG, "getPost: started with post id = " + postID);
        try {
            if (postID != null ) {
                FirebaseFirestore.getInstance().collection("Sessions").document(postID).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.isComplete() && task.getResult() != null) {
                        post = task.getResult().toObject(Post.class);
                        if (post != null) {
                            setViews(post);
                        } else {
                            Log.i(TAG, "getPost: post is null");
                        }
                    }
                });
            } else {
                FirebaseFirestore.getInstance().collection("Sessions").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.isComplete() && task.getResult() != null) {
                        List<Post> posts = task.getResult().toObjects(Post.class);
                        for (Post p : posts) {
                            if (p.getmOwnerID().equals(uid) && p.getmPostTime().equals(userActivity.getTime())) {
                                setViews(p);
                                break;
                            }
                            Log.i(TAG, "getPost: post is null");
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "getPost: " + e.getMessage());
        }
    }

    private void setViews(Post post) {
        try {
            Log.i(TAG, "setViews: started");
            Picasso.get().load(Uri.parse(post.getmOwnerImage())).into(postOwnerImaage);
            postOwnerNameTv.setText(post.getmOwnerName());
            postTimeTv.setText(post.getmPostTime());
            postCaption.setText(post.getmPostCaption());
            postCarDetails.setText(post.getmCarDetails());
            String date = post.getmStartDate() + " " + post.getmStartTime();
            postStartDateTime.setText(date);
            postDuration.setText(post.getmDuration());
            instructorNumber.setText(post.getmPhoneNumber());
            costTv.setText(post.getmPrice());
            LocationModel model = post.getmLocation();
            if (mCurrentLocation != null) {
                double distance = NumberUtils.distance(model.getLatitude(), model.getLongitude(), mCurrentLocation);
                String dis = distance + " Kilometers away from you";
                postDistance.setText(dis);
                getLocationProgress.setVisibility(View.INVISIBLE);
            } else {
                Log.i(TAG, "setViews: " + null);
            }
            String likes = post.getmLikes().getNumber() + " likes";
            likeCount.setText(likes);
            setLike(post.getmLikes().getUsers().contains(uid));
            setJoined(post.getmJoined().getUsers().contains(uid));
            String joined = post.getmJoined().getNumber() + " joined";
            joinCount.setText(joined);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "setViews: " + e.getMessage());
        }
    }

    private void setJoined(boolean joined) {
        Log.i(TAG, "setJoined: " + joined);
        if (joined) {
            isJoined = true;
            joinImage.setImageResource(R.drawable.ic_check_green_24dp);
            joinCount.setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.green));
        } else {
            isJoined = false;
            joinImage.setImageResource(R.drawable.ic_check_gray_24dp);
            joinCount.setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.gray_dark));
        }
    }

    private void setLike(boolean like) {
        Log.i(TAG, "setLike: " + like);
        if (like) {
            isLiked = true;
            likeImage.setImageResource(R.drawable.ic_favorite_red);
            likeCount.setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.red));
        } else {
            isLiked = false;
            likeImage.setImageResource(R.drawable.ic_favorite_blanck);
            likeCount.setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.gray_dark));
        }
    }

    @Override
    public void onClick(View view) {

        if (post != null) {
            if (view.equals(likeButton)) {
                if (isLiked) {
                    FireStoreProcess.updatePostLikesJoined(postID, "mLikes", 0, uid);
                    dismiss();
                } else {
                    FireStoreProcess.updatePostLikesJoined(postID, "mLikes", 1, uid);
                    dismiss();
                }
            } else if (view.equals(joinButton)) {
                if (isJoined) {
                    setJoined(false);
                    FireStoreProcess.updatePostLikesJoined(postID, "mJoined", -1, uid);
                    dismiss();
                } else {
                    setJoined(true);
                    FireStoreProcess.updatePostLikesJoined(postID, "mJoined", 1, uid);
                    dismiss();
                }
            } else if (view.equals(callButton)) {

            } else if (view.equals(distanceLayout)) {
                MapBottomSheet dialog = new MapBottomSheet(post.getmLocation());
                dialog.show((Objects.requireNonNull(getActivity())).getSupportFragmentManager(), dialog.getTag());
                dismiss();
            }
        }
    }
}
