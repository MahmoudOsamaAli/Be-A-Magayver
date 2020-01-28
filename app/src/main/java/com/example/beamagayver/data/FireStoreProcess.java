package com.example.beamagayver.data;

import android.util.Log;


import androidx.annotation.NonNull;

import com.example.beamagayver.pojo.JoinedModel;
import com.example.beamagayver.pojo.LikesModel;
import com.example.beamagayver.pojo.Post;
import com.example.beamagayver.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class FireStoreProcess {
    private static final String TAG = "FireStoreProcess";

    static onListenToFireStore listener;

    public FireStoreProcess(onListenToFireStore listener) {
        this.listener = listener;
    }

    public static void addUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = user.getUid();
        Log.i(TAG, "addUser: user id = " + uid);
        String type = user.getUserType();
        if (uid != null) {
            db.collection(type).document(uid).get().addOnCompleteListener(task -> {
                // check if user is in data base
                if (task.isSuccessful() && task.isComplete()) {
                    Log.i(TAG, "addUser: task check if user in database is completed");
                    DocumentSnapshot result = task.getResult();
                    if (result != null && result.exists()) {
                        Log.i(TAG, "addUser: user in the database");
                    } else {
                        Log.i(TAG, "addUser: user is not in database");
                        db.collection(type).document(uid).set(user)
                                .addOnSuccessListener(documentReference -> {
                                    Log.i(TAG, "addUser: task is successefull");
                                }).addOnCompleteListener(task1 -> {
                            Log.i(TAG, "addUser: task is completed");
                        });
                    }
                }
            });
        }

    }

    public static void addPostToUser(Post post) {
        String uid = post.getmOwnerID();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.i(TAG, "addPostToUser: user id " + uid);
        db.collection("Sessions")
                .add(post).addOnSuccessListener(documentReference -> Log.i(TAG, "addPostToUser: post id" + documentReference.getId()))
                .addOnCompleteListener(task -> {
                    Log.i(TAG, "onComplete: " + task.getResult());
                    db.collection("Sessions").document(task.getResult().getId())
                            .update(
                                    "mPostID", task.getResult().getId()
                            ).addOnSuccessListener(aVoid -> {
                        Log.i(TAG, "addPostToUser: add id is successeded");
                    }).addOnFailureListener(e -> {
                        Log.i(TAG, "addPostToUser: task is failed");
                    }).addOnCompleteListener(task1 -> {
                        Log.i(TAG, "onComplete: task is completed");
                    });
                });
    }

    public static void updatePostLikesJoined(String postID, String field, int value, String userID) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ref = db.collection("Sessions").document(postID);
            WriteBatch batch = db.batch();
            ref.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.isComplete()) {
                    Log.i(TAG, "updatePostLikesJoined: " + task.getResult().exists());
                    if (task.getResult().exists()) {
                        DocumentSnapshot document = task.getResult();
                        Post post = document.toObject(Post.class);
                        if(field.equals("mLikes") && post != null){
                            try {
                                LikesModel likesModel = post.getmLikes();
                                if(likesModel != null && value == 1){
                                    Log.i(TAG, "updatePostLikesJoined: add like");
                                    likesModel.setSingleUser(userID);
                                    Log.i(TAG, "updatePostLikesJoined: add = " + likesModel.getUsers().size());
                                    likesModel.setNumber(likesModel.getNumber() +1);
                                    batch.update(ref, field, likesModel);
                                    batch.commit();
                                }else if(likesModel != null && likesModel.getUsers().size() >0 && likesModel.getNumber() >0){
                                    Log.i(TAG, "updatePostLikesJoined: remove like");
                                    boolean remove = likesModel.getUsers().remove(userID);
                                    Log.i(TAG, "updatePostLikesJoined: removed = " + remove);
                                    likesModel.setNumber(likesModel.getNumber() -1);
                                    batch.update(ref, field, likesModel);
                                    batch.commit();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i(TAG, "updatePostLikesJoined: " + e.getMessage());
                            }
                        }else if(field.equals("mJoined") && post != null){
                            
                            JoinedModel joinedModel = post.getmJoined();
                            if(joinedModel != null && value == 1){
                                Log.i(TAG, "updatePostLikesJoined: add join");
                                boolean add = joinedModel.getUsers().add(userID);
                                Log.i(TAG, "updatePostLikesJoined: add join = " +add );
                                joinedModel.setNumber(joinedModel.getNumber() +1);
                                batch.update(ref, field, joinedModel);
                                batch.commit();
                            }else if(joinedModel != null && joinedModel.getUsers().size()>0 && joinedModel.getNumber() >0){
                                boolean remove = joinedModel.getUsers().remove(userID);
                                Log.i(TAG, "updatePostLikesJoined: remove join = " +remove);
                                joinedModel.setNumber(joinedModel.getNumber() -1);
                                batch.update(ref, field, joinedModel);
                                batch.commit();
                            }
                        }
                    }
                } else {
                    Log.i(TAG, "Error getting documents: "+ task.getException().getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "updatePostLikesJoined: " + e.getMessage());
        }
    }
}
