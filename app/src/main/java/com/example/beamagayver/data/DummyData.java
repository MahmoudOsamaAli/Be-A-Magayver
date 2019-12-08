package com.example.beamagayver.data;

import android.content.Context;

import com.example.beamagayver.R;
import com.example.beamagayver.pojo.Post;

import java.util.ArrayList;

public class DummyData {

    ArrayList<Post> posts;
    Context context;

    public DummyData(Context context) {
        this.context = context;
    }

    public ArrayList<Post> getPosts() {
        Post post = new Post(R.drawable.ic_profile , "Mahmoud Osama" ,
                "1 hour ago" ,context.getString(R.string.post_contnent));
        posts.add(post);
        post = new Post(R.drawable.ic_profile , "Ali Osama" ,
                "2 hour ago" ,context.getString(R.string.post_contnent));
        posts.add(post);
        post = new Post(R.drawable.ic_profile , "Mahmoud Osama" ,
                "1 hour ago" ,context.getString(R.string.post_contnent));
        posts.add(post);
        post = new Post(R.drawable.ic_profile , "Ali Osama" ,
                "2 hour ago" ,context.getString(R.string.post_contnent));
        posts.add(post);
        post = new Post(R.drawable.ic_profile , "Mahmoud Osama" ,
                "1 hour ago" ,context.getString(R.string.post_contnent));
        posts.add(post);
        post = new Post(R.drawable.ic_profile , "Ali Osama" ,
                "2 hour ago" ,context.getString(R.string.post_contnent));
        posts.add(post);
        return posts;
    }
}
