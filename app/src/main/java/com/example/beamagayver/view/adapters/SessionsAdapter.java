package com.example.beamagayver.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.data.FireStoreProcess;
import com.example.beamagayver.data.onListenToFireStore;
import com.example.beamagayver.pojo.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.MyHolder> implements onListenToFireStore {

    private static final String TAG = "SessionsAdapter";
    Context mContext;
    ArrayList<Post> data;
    onListenToFireStore listener;

    public SessionsAdapter(Context mContext, ArrayList<Post> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.session_item, parent, false);
        return new MyHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        try {
            if (data.size() > 0) {
                Collections.sort(data);
                Collections.reverse(data);
                Post p = data.get(position);
                String likes = p.getmLikes() + " likes";
                String joined = p.getmJoined() + " joined";
                String startDate = p.getmStartDate() +" - " +p.getmStartTime();
                Uri photoUri = Uri.parse(p.getmOwnerImage());
                holder.mOwnerName.setText(p.getmOwnerName());
                holder.mPostTime.setText(p.getmPostTime());
                holder.mPostCaption.setText(p.getmPostCaption());
                holder.mPhoneNumber.setText(p.getmPhoneNumber());
                holder.mDuration.setText(p.getmDuration());
                holder.mStartDate.setText(startDate);
                holder.likesCount.setText(likes);
                holder.joinedCount.setText(joined);
                holder.mCarDetails.setText(p.getmCarDetails());
                Picasso.get().load(photoUri).into(holder.mOwnerImage);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onPostAdded(Post post) {
        try {
            if (post != null) {
                if(!data.contains(post)) {
                    data.add(post);
                   notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "onPostAdded: "+ e.getMessage());
        }
    }

    @Override
    public void onPostModified(Post post) {
        Log.i(TAG, "onPostModified: interface is called");
        try {
            if (post != null) {
                String postID = post.getmPostID();
                for (Post p : data) {
                    if (postID != null && p.getmPostID().equals(postID)) {
                        data.set(data.indexOf(p), post);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i(TAG, "onPostModified: " + ex.getMessage());
        }
        notifyDataSetChanged();
    }

    @Override
    public void onPostDeleted(Post post) {
        Log.i(TAG, "onPostDeleted: interface is called");
        String id = post.getmPostID();
        for(Post p : data){
            if(p.getmPostID().equals(id)) data.remove(post);
        }
        notifyDataSetChanged();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        boolean liked;
        boolean joined;
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

        MyHolder(@NonNull View itemView) {
            super(itemView);
            liked = false;
            joined = false;
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

            if (liked) likeImage.setImageResource(R.drawable.ic_favorite_red);
            else likeImage.setImageResource(R.drawable.ic_favorite_blanck);
            if(joined) joinedImage.setImageResource(R.drawable.ic_check_green_24dp);
            else joinedImage.setImageResource(R.drawable.ic_check_gray_24dp);
            menu.setOnClickListener(this);
            likes.setOnClickListener(this);
            call.setOnClickListener(this);
            join.setOnClickListener(this);
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
                    try {
                        if (!liked) {
                            liked = true;
                            likeImage.setImageResource(R.drawable.ic_favorite_red);
                        } else {
                            liked = false;
                            likeImage.setImageResource(R.drawable.ic_favorite_blanck);
                        }

                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.join_button:
                    if(!joined){
                        joined = true;
                        joinedImage.setImageResource(R.drawable.ic_check_green_24dp);
                    }else{
                        joined = false;
                        joinedImage.setImageResource(R.drawable.ic_check_gray_24dp);
                    }
                    break;
                case R.id.call_button:
                    try {
                        String number = mPhoneNumber.getText().toString();
                        if (number != null || !number.isEmpty() || number != "") {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:"+mPhoneNumber.getText()));
                            mContext.startActivity(intent);
                        } else {
                            Toast.makeText(mContext, "No Phone Number Applied", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, "onClick: " + e.getMessage());
                    }
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.delete_post) {
                Toast.makeText(mContext, "deleting the post", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }
}
