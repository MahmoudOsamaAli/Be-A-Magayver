package com.example.beamagayver.data;

import android.util.Log;


import androidx.annotation.NonNull;

import com.example.beamagayver.pojo.Post;
import com.example.beamagayver.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;


public class FireStoreProcess {
    private static final String TAG = "FireStoreProcess";

    public static void addUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        Log.i(TAG, "addPostToUser: user id " +uid);
        db.collection("Sessions")
                .add(post).addOnSuccessListener(documentReference ->
                Log.i(TAG, "addPostToUser: post id" + documentReference.getId()))
        .addOnCompleteListener(
                task -> Log.i(TAG, "onComplete: " + task.getResult())
        );
    }

}
