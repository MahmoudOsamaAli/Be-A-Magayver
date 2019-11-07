package com.example.beamagayver.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.pojo.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class activitiesAdapter extends RecyclerView.Adapter<activitiesAdapter.MyHolder> {

    Context mContext;
    ArrayList<Image> data;

    public activitiesAdapter(Context mContext, ArrayList<Image> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.activities_item , parent , false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Picasso.get().load(data.get(position).getImageLink()).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView mImage;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.activity_image);

        }
    }
}
