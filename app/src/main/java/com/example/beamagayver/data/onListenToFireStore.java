package com.example.beamagayver.data;

import com.example.beamagayver.pojo.Post;

public interface onListenToFireStore {

    void onPostAdded(Post post);
    void onPostModified(Post post);
    void onPostDeleted(Post post);
}
