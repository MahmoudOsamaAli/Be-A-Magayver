package com.example.beamagayver.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.data.FireStoreProcess;
import com.example.beamagayver.data.PrefManager;
import com.example.beamagayver.pojo.UserActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyHolder> {

    private static final String TAG = "ActivitiesAdapter";
    private Context mContext;
    private String userID;
    private ArrayList<UserActivity> data;
    private RecyclerViewListClicked itemListener;

    public ActivitiesAdapter(ArrayList<UserActivity> data, Context context, RecyclerViewListClicked itemListener) {
        this.data = data;
        mContext = context;
        this.itemListener = itemListener;
        PrefManager prefManager = new PrefManager(context);
        userID = prefManager.readString(mContext.getResources().getString(R.string.account_id));
        Log.i(TAG, "ActivitiesAdapter: constructor");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivitiesAdapter.MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activities_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        UserActivity activity = data.get(position);
        PrefManager manager = new PrefManager(mContext);
        String name = manager.readString(mContext.getResources().getString(R.string.account_name));
        String photo = manager.readString(mContext.getResources().getString(R.string.account_photo));
        String ac;
        if (activity.isCreated()) {
            ac = name + " has created session";
            holder.activity.setText(ac);
        } else if (activity.isJoined()) {
            ac = name + " has joined " + activity.getAccountName() + " session";
            holder.activity.setText(ac);
        }
        holder.time.setText(activity.getTime());
        Picasso.get().load(Uri.parse(photo)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView activity;
        TextView time;
        CircleImageView image;
        ImageView delete;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            activity = itemView.findViewById(R.id.activity_text);
            time = itemView.findViewById(R.id.activity_time);
            image = itemView.findViewById(R.id.image_profile);
            delete = itemView.findViewById(R.id.delete_activity);
            delete.setOnClickListener(this);
            itemView.setOnClickListener(this);
            Log.i(TAG, "MyHolder: ");
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: ");

            try {
                if(view.equals(delete)) {
                    PrefManager manager = new PrefManager(mContext);
                    UserActivity activity = data.get(getLayoutPosition());
                    String type = manager.readString(mContext.getResources().getString(R.string.account_type));
                    if (activity.isJoined()) {
                        Toast.makeText(mContext, "deleted", Toast.LENGTH_SHORT).show();
                        FireStoreProcess process = new FireStoreProcess(mContext);
                        FireStoreProcess.deleteActivity(userID, activity.getPostID(), type);
                    } else {
                        String time = activity.getTime();
                        Toast.makeText(mContext, "deleted", Toast.LENGTH_SHORT).show();
                        FireStoreProcess process = new FireStoreProcess(mContext);
                        FireStoreProcess.deleteActivityByTime(userID, time, type);
                    }
                } else{
                    itemListener.recyclerViewListClicked(view, this.getLayoutPosition());
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                Log.i(TAG, "onClick: " + e.getMessage());
            }
        }
    }
}
