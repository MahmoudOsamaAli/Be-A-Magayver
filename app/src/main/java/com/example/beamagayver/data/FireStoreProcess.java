package com.example.beamagayver.data;

import android.content.Context;
import android.util.Log;


import com.example.beamagayver.pojo.JoinedModel;
import com.example.beamagayver.pojo.LikesModel;
import com.example.beamagayver.pojo.Post;
import com.example.beamagayver.pojo.User;
import com.example.beamagayver.pojo.UserActivity;
import com.example.beamagayver.view.fragments.activities.listenToFireBase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class FireStoreProcess {
    private static final String TAG = "FireStoreProcess";

    private static listenToFireBase listener;
    private Context context;
    private static PrefManager manager;

    public FireStoreProcess() {
    }

    public FireStoreProcess(Context context) {
        this.context = context;
        manager = new PrefManager(context);
        initListener();
    }

    private void initListener() {
        listener = () -> {
        };
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
                        db.collection(type).document(uid).collection("activities")
                                .add(new UserActivity())
                                .addOnCompleteListener(task12 -> {
                                    Log.i(TAG, "onComplete: " + task12.getResult().getId());
                                    String id = task12.getResult().getId();
                                    db.collection(type).document(uid).collection("activities").document(id).delete();
                                });
                    }
                }
            });
        }

    }

    public static void addPostToUser(Post post) {
        String uid = post.getmOwnerID();
        UserActivity activity = new UserActivity();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.i(TAG, "addPostToUser: user id " + uid);
        db.collection("Sessions")
                .add(post).addOnSuccessListener(documentReference -> Log.i(TAG, "addPostToUser: post id" + documentReference.getId()))
                .addOnCompleteListener(task -> {
                    db.collection("Sessions").document(task.getResult().getId()).update(
                            "mPostID", task.getResult().getId()).addOnCompleteListener(task1 -> {

                    });
                });
        activity.setAccountName(post.getmOwnerName());
        activity.setUid(post.getmOwnerID());
        activity.setTime(post.getmPostTime());
        activity.setCreated(true);
        addUserActivity(activity, post.getmOwnerID(), "instructor");
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
                        if (field.equals("mLikes") && post != null) {
                            try {
                                LikesModel likesModel = post.getmLikes();
                                if (likesModel != null && value == 1) {
                                    Log.i(TAG, "updatePostLikesJoined: add like");
                                    likesModel.setSingleUser(userID);
                                    Log.i(TAG, "updatePostLikesJoined: add = " + likesModel.getUsers().size());
                                    likesModel.setNumber(likesModel.getNumber() + 1);
                                    batch.update(ref, field, likesModel);
                                    batch.commit();
                                } else if (likesModel != null && likesModel.getUsers().size() > 0 && likesModel.getNumber() > 0) {
                                    Log.i(TAG, "updatePostLikesJoined: remove like");
                                    boolean remove = likesModel.getUsers().remove(userID);
                                    Log.i(TAG, "updatePostLikesJoined: removed = " + remove);
                                    likesModel.setNumber(likesModel.getNumber() - 1);
                                    batch.update(ref, field, likesModel);
                                    batch.commit();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i(TAG, "updatePostLikesJoined: " + e.getMessage());
                            }
                        } else if (field.equals("mJoined") && post != null) {

                            JoinedModel joinedModel = post.getmJoined();
                            if (joinedModel != null && value == 1) {
                                Log.i(TAG, "updatePostLikesJoined: add join");
                                boolean add = joinedModel.getUsers().add(userID);
                                Log.i(TAG, "updatePostLikesJoined: add join = " + add);
                                joinedModel.setNumber(joinedModel.getNumber() + 1);
                                batch.update(ref, field, joinedModel);
                                batch.commit();
                            } else if (joinedModel != null && joinedModel.getUsers().size() > 0 && joinedModel.getNumber() > 0) {
                                boolean remove = joinedModel.getUsers().remove(userID);
                                Log.i(TAG, "updatePostLikesJoined: remove join = " + remove);
                                joinedModel.setNumber(joinedModel.getNumber() - 1);
                                batch.update(ref, field, joinedModel);
                                batch.commit();
                            }
                        }
                    }
                } else {
                    Log.i(TAG, "Error getting documents: " + task.getException().getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "updatePostLikesJoined: " + e.getMessage());
        }
    }

    public static void deletePost(String postID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Sessions").document(postID).delete()
                .addOnSuccessListener(aVoid -> {
                    Log.i(TAG, "deletePost: deleted successfully");
                }).addOnFailureListener(e -> {
            Log.i(TAG, "deletePost: " + e.getMessage());
        }).addOnCompleteListener(task -> {
            Log.i(TAG, "deletePost: " + task.isSuccessful() + " " + task.isCanceled());
        });
    }

    public static void updatePost(Post post) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Sessions").document(post.getmPostID()).update(createMapPost(post))
                .addOnSuccessListener(aVoid -> {
                    Log.i(TAG, "updatePost: updated successfully");
                });
    }

    private static HashMap<String, Object> createMapPost(Post post) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mPostID", post.getmPostID());
        map.put("mOwnerID", post.getmOwnerID());
        map.put("mOwnerName", post.getmOwnerName());
        map.put("mOwnerImage", post.getmOwnerImage());
        map.put("mPostTime", post.getmPostTime());
        map.put("mPostCaption", post.getmPostCaption());
        map.put("mCarDetails", post.getmCarDetails());
        map.put("mDuration", post.getmDuration());
        map.put("mStartDate", post.getmStartDate());
        map.put("mStartTime", post.getmStartTime());
        map.put("mJoined", post.getmJoined());
        map.put("mLikes", post.getmLikes());
        map.put("mPrice", post.getmPrice());
        map.put("mPhoneNumber", post.getmPhoneNumber());
        map.put("mLocation", post.getmLocation());
        return map;
    }

    private static HashMap<String, Object> createMapActivity(UserActivity activity) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", activity.getUid());
        map.put("joined", activity.isJoined());
        map.put("postID", activity.getPostID());
        map.put("time", activity.getTime());
        return map;
    }

    public static void addUserActivity(UserActivity activity, String uid, String userType) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(userType).document(uid).collection("activities").add(activity)
                    .addOnSuccessListener(aVoid -> {
                        Log.i(TAG, "addUserActivity: success");
                    }).addOnFailureListener(e -> {
                Log.i(TAG, "addUserActivity: failed : " + e.getMessage());
            }).addOnCompleteListener(task -> {
                Log.i(TAG, "addUserActivity: completed");
            });
            listener.onDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "addUserActivity: " + e.getMessage());
        }
    }

    public static void deleteActivity(String uid, String postID, String userType) {
        try {
            CollectionReference reference = FirebaseFirestore.getInstance().collection(userType).document(uid).collection("activities");
            reference.whereEqualTo("uid", uid).whereEqualTo("postID", postID).get()
                    .addOnCompleteListener(task -> {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (documents.size() > 0) {
                            String id = documents.get(0).getId();
                            Log.i(TAG, "deleteActivity: document id = " + id);
                            reference.document(id).delete();
                            listener.onDataChanged();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "deleteActivity: " + e.getMessage());
        }
    }

    public static void deleteActivityByTime(String uid, String time, String type) {
        try {
            CollectionReference reference = FirebaseFirestore.getInstance().collection(type).document(uid).collection("activities");
            reference.whereEqualTo("uid", uid).whereEqualTo("time", time).whereEqualTo("created", true).get()
                    .addOnCompleteListener(task -> {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (documents.size() > 0) {
                            String id = documents.get(0).getId();
                            Log.i(TAG, "deleteActivity: document id = " + id);
                            reference.document(id).delete();
                            listener.onDataChanged();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "deleteActivity: " + e.getMessage());
        }
    }
}
