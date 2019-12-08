package com.example.beamagayver.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.pojo.Post;

import java.util.ArrayList;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.MyHolder> {

    Context mContext;
    ArrayList<Post> data;

    public SessionsAdapter(Context mContext, ArrayList<Post> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.session_item, parent , false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.mImage.setImageResource(data.get(position).getImageLink());
        holder.mName.setText(data.get(position).getmName());
        holder.mTime.setText(data.get(position).getmTime());
        holder.mPostContent.setText(data.get(position).getmPostContent());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mName;
        TextView mTime;
        TextView mPostContent;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.post_owner_imaage);
            mName = itemView.findViewById(R.id.post_owner_name_tv);
            mTime = itemView.findViewById(R.id.post_time_tv);
            mPostContent = itemView.findViewById(R.id.post_content);
        }
    }
}
