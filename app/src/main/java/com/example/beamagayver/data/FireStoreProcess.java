package com.example.beamagayver.data;

import android.util.Log;


import com.example.beamagayver.pojo.Post;
import com.example.beamagayver.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class FireStoreProcess {
    private static final String TAG = "FireStoreProcess";

    static onListenToFireStore listener;

    public FireStoreProcess(onListenToFireStore listener) {
        this.listener = listener;
    }

    public static void addUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String email = user.getmEmail();
        if (email != null) {
            db.collection("User").document(user.getmUserType()).collection("users").get().addOnCompleteListener(task -> {
                QuerySnapshot result = task.getResult();
                if (result != null) {
                    boolean b1 = false;
                    for (QueryDocumentSnapshot queryDocumentSnapshot : result) {
                        String mEmail = Objects.requireNonNull(queryDocumentSnapshot.get("mEmail")).toString();
                        Log.i(TAG, "onComplete: mEmail = " + mEmail);
                        if (mEmail.equals(email)) {
                            b1 = true;
                            Log.i(TAG, "onComplete: boolean b1 = " + b1);
                            break;
                        }
                    }
                    if (!b1) {
                        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                        db1.collection("User").document(user.getmUserType()).collection(uid).add(user).addOnCompleteListener(task1 ->
                                Log.i(TAG, "addUser: " + task1.getResult()));
                    }
                }
            });
        }

    }

    public static void addPostToUser(FirebaseUser user, Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = user.getUid();
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

    public static void listenToFireBase(ArrayList<Post> posts) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ListenerRegistration listener = db.collection("Sessions").addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.i(TAG, "listen:error" + e.getMessage());
                return;
            }
            if (snapshots != null) {
                if (posts.size() != snapshots.getDocumentChanges().size()) {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Post newPost = dc.getDocument().toObject(Post.class);
                        switch (dc.getType()) {
                            case ADDED:
                                FireStoreProcess.listener.onPostAdded(newPost);
                                Log.i(TAG, "New post: " + dc.getDocument().getData());
                                break;
                            case REMOVED:
                                FireStoreProcess.listener.onPostDeleted(newPost);
                                Log.i(TAG, "Removed post: " + dc.getDocument().getData());
                                break;
                        }
                    }
                } else if (posts.size() == snapshots.getDocumentChanges().size()){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Post newPost = dc.getDocument().toObject(Post.class);
                        if (dc.getType() == DocumentChange.Type.MODIFIED) {
                            FireStoreProcess.listener.onPostModified(newPost);
                            Log.i(TAG, "listenToFireBase: listener modified called");
                        }

                    }
                }
            }
        });
        listener.remove();
    }
}
