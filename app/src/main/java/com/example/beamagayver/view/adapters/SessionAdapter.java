package com.example.beamagayver.view.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.Utilities.NumberUtils;
import com.example.beamagayver.data.FireStoreProcess;
import com.example.beamagayver.data.PrefManager;
import com.example.beamagayver.pojo.JoinedModel;
import com.example.beamagayver.pojo.LikesModel;
import com.example.beamagayver.pojo.Post;
import com.example.beamagayver.pojo.UserActivity;
import com.example.beamagayver.view.activities.HomeActivity;
import com.example.beamagayver.view.activities.addPost.AddPostActivity;
import com.example.beamagayver.view.fragments.sessionFragment.showProfile;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class SessionAdapter extends FirestoreRecyclerAdapter<Post, SessionAdapter.MyHolder> {

    private static final String TAG = "SessionAdapter";
    private double sessionLON;
    private double sessionLAT;
    private Context mContext;
    private Activity mActivity;
    private Location mCurrentLocation = HomeActivity.mCurrentLocation;
    private String userID;
    private String userType;
    private showProfile listener;

    public SessionAdapter(@NonNull FirestoreRecyclerOptions<Post> options, Context context, Activity activity ,showProfile listener) {
        super(options);
        mContext = context;
        mActivity = activity;
        this.listener = listener;
        PrefManager prefManager = new PrefManager(context);
        userID = prefManager.readString(mContext.getResources().getString(R.string.account_id));
        userType = prefManager.readString(mContext.getResources().getString(R.string.account_type));
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull Post p) {
        try {
            holder.currPost = p;
            String startDate = p.getmStartDate() + " - " + p.getmStartTime();
            Uri photoUri = Uri.parse(p.getmOwnerImage());
            sessionLAT = p.getmLocation().getLatitude();
            sessionLON = p.getmLocation().getLongitude();
            if (mCurrentLocation != null) {
                double distance = NumberUtils.distance(sessionLAT, sessionLON, mCurrentLocation);
                String dis = distance + " Kilometers away from you";
                holder.postDist.setVisibility(View.VISIBLE);
                holder.postDist.setText(dis);
                holder.progressBar.setVisibility(View.GONE);
            } else {
                holder.progressBar.setVisibility(View.GONE);
                holder.postDist.setVisibility(View.VISIBLE);
                holder.postDist.setText(mContext.getResources().getString(R.string.get_my_location));
            }
            holder.mOwnerName.setText(p.getmOwnerName());
            holder.mPostTime.setText(p.getmPostTime());
            holder.cost.setText(p.getmPrice());
            holder.mPostCaption.setText(p.getmPostCaption());
            holder.mPhoneNumber.setText(p.getmPhoneNumber());
            holder.mDuration.setText(p.getmDuration());
            holder.mStartDate.setText(startDate);
            holder.mCarDetails.setText(p.getmCarDetails());
            Picasso.get().load(photoUri).into(holder.mOwnerImage);
            // set like join
            LikesModel likesModel = p.getmLikes();
            JoinedModel joinedModel = p.getmJoined();
            String likes = likesModel.getNumber() + " likes";
            String joined = joinedModel.getNumber() + " joined";
            setLikes(likesModel.getUsers().contains(userID), holder);
            setJoined(joinedModel.getUsers().contains(userID), holder);
            holder.likesCount.setText(likes);
            holder.joinedCount.setText(joined);
            setViews(holder , p);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "onBindViewHolder: " + e.getMessage());
        }
    }

    private void setViews(MyHolder holder, Post p) {
        if(p.getmOwnerID().equals(userID)){
            holder.menu.setVisibility(View.VISIBLE);
            holder.join.setVisibility(View.GONE);
        }else{
            holder.menu.setVisibility(View.GONE);
            holder.join.setVisibility(View.VISIBLE);
        }
    }


    private void setLikes(boolean liked, MyHolder holder) {
        try {
            holder.liked = liked;
            if (liked) {
                Log.i(TAG, "setLikes: liked = " + true);
                holder.likeImage.setImageResource(R.drawable.ic_favorite_red);
                holder.likesCount.setTextColor(mContext.getResources().getColor(R.color.red));
            } else {
                Log.i(TAG, "setLikes: liked = " + false);
                holder.likeImage.setImageResource(R.drawable.ic_favorite_blanck);
                holder.likesCount.setTextColor(mContext.getResources().getColor(R.color.gray_dark));
            }


        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "setLikes: " + e.getMessage());
        }
    }

    private void setJoined(boolean joined, MyHolder holder) {
        holder.joined = joined;
        if (joined) {
            Log.i(TAG, "setLikes: joined " + true);
            holder.joinedImage.setImageResource(R.drawable.ic_check_green_24dp);
            holder.joinedCount.setTextColor(mContext.getResources().getColor(R.color.green));
        } else {
            Log.i(TAG, "setLikes: joined " + false);
            holder.joinedImage.setImageResource(R.drawable.ic_check_gray_24dp);
            holder.joinedCount.setTextColor(mContext.getResources().getColor(R.color.gray_dark));
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item, parent, false));
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        boolean liked;
        boolean joined;
        Post currPost;
        ImageView mOwnerImage;
        TextView mOwnerName;
        TextView mPostTime;
        TextView mPostCaption;
        ImageView menu;
        ImageView likeImage;
        ImageView joinedImage;
        ConstraintLayout likes;
        ConstraintLayout join;
        Button call;
        TextView likesCount;
        TextView joinedCount;
        TextView mCarDetails;
        TextView mPhoneNumber;
        TextView mDuration;
        TextView mStartDate;
        TextView postDist;
        LinearLayout distLayout;
        ProgressBar progressBar;
        TextView cost;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            mOwnerImage = itemView.findViewById(R.id.post_owner_imaage);
            mOwnerName = itemView.findViewById(R.id.post_owner_name_tv);
            mCarDetails = itemView.findViewById(R.id.post_car_details);
            mPhoneNumber = itemView.findViewById(R.id.instructor_number);
            mDuration = itemView.findViewById(R.id.post_duration);
            mStartDate = itemView.findViewById(R.id.post_start_date_time);
            likesCount = itemView.findViewById(R.id.like_count);
            joinedCount = itemView.findViewById(R.id.join_count);
            mPostTime = itemView.findViewById(R.id.post_time_tv);
            joinedImage = itemView.findViewById(R.id.join_image);
            mPostCaption = itemView.findViewById(R.id.post_caption);
            menu = itemView.findViewById(R.id.post_menu);
            likes = itemView.findViewById(R.id.like_button);
            call = itemView.findViewById(R.id.call_button);
            join = itemView.findViewById(R.id.join_button);
            likeImage = itemView.findViewById(R.id.like_image);
            postDist = itemView.findViewById(R.id.post_distance);
            distLayout = itemView.findViewById(R.id.distance_layout);
            progressBar = itemView.findViewById(R.id.get_location_progress);
            progressBar.setVisibility(View.GONE);
            cost = itemView.findViewById(R.id.cost_tv);
            menu.setOnClickListener(this);
            likes.setOnClickListener(this);
            call.setOnClickListener(this);
            join.setOnClickListener(this);
            distLayout.setOnClickListener(this);
            mOwnerImage.setOnClickListener(this);
            mOwnerName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.post_menu:
                    Log.i(TAG, "onClick: post menu");
                    PopupMenu popup = new PopupMenu(mContext, menu);
                    popup.inflate(R.menu.post_menu);
                    popup.show();
                    popup.setOnMenuItemClickListener(this);
                    break;
                case R.id.like_button:
                    performLiked();
                    break;
                case R.id.join_button:
                    performJoined();
                    break;
                case R.id.call_button:
                    callInstructor();
                    break;
                case R.id.distance_layout:
                    if (mCurrentLocation != null) {
                        launchGoogleMapsApp(mContext, sessionLAT, sessionLON);
                    } else {
                        postDist.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        mCurrentLocation = HomeActivity.getLocation();
                    }
                    break;
                case R.id.post_owner_imaage :
                    Log.i(TAG, "onClick: post owner image clicked");
                    listener.onImageClicked(currPost.getmOwnerID());
                    break;
                case R.id.post_owner_name_tv:
                    Log.i(TAG, "onClick: post owner name clicked");
                    listener.onImageClicked(currPost.getmOwnerID());
                    break;

            }
        }

        private void callInstructor() {
            try {
                String number = mPhoneNumber.getText().toString();
                if (!number.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mPhoneNumber.getText()));
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "No Phone Number Applied", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "onClick: " + e.getMessage());
            }
        }

        private void performJoined() {
            UserActivity activity = new UserActivity();
            activity.setUid(userID);
            activity.setAccountName(currPost.getmOwnerName());
            activity.setPostID(currPost.getmPostID());
            activity.setTime(DateFormat.format("dd/MM/yyyy hh:mm a", new Date()).toString());
            String postID = currPost.getmPostID();
            if (!joined) {
                joined = true;
                Log.i(TAG, "onClick: joined");
                FireStoreProcess.updatePostLikesJoined(postID, "mJoined", 1, userID);
                activity.setJoined(true);
                FireStoreProcess.addUserActivity(activity ,userID , userType);
            } else {
                joined = false;
                Log.i(TAG, "onClick: not joined");
                FireStoreProcess.updatePostLikesJoined(postID, "mJoined", -1, userID);
                activity.setJoined(false);
                FireStoreProcess process = new FireStoreProcess(mContext);
                FireStoreProcess.deleteActivity(userID , currPost.getmPostID() , userType);
            }


        }

        private void performLiked() {
            String postID = currPost.getmPostID();
            if (!liked) {
                liked = true;
                Log.i(TAG, "onClick: liked");
                FireStoreProcess.updatePostLikesJoined(postID, "mLikes", 1, userID);
            } else {
                liked = false;
                Log.i(TAG, "onClick: not liked");
                FireStoreProcess.updatePostLikesJoined(postID, "mLikes", -1, userID);
            }
        }

        private void launchGoogleMapsApp(Context context, double latitude, double longitude) {
            try {
                String format = "geo:0,0?q=" + latitude + "," + longitude + "(" + "Session location" + ")";
                Uri uri = Uri.parse(format);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "launchGoogleMapsApp: " + e.getMessage());
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.delete_post) {
                new AlertDialog.Builder(mContext)
                        .setMessage("Are you sure you want to delete this session")
                        .setPositiveButton("yes", (dialogInterface, i) -> {
                            FireStoreProcess.deletePost(currPost.getmPostID());
                            dialogInterface.dismiss();
                        }).setNegativeButton("No", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).create().show();
            }else if(item.getItemId() == R.id.edit_post){
                Intent intent = new Intent(mActivity , AddPostActivity.class);
                intent.putExtra("EditPost", currPost);
                mContext.startActivity(intent);
            }
            return true;
        }
    }
}
