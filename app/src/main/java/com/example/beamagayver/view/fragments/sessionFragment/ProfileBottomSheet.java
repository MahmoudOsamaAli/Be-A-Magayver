package com.example.beamagayver.view.fragments.sessionFragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.beamagayver.R;
import com.example.beamagayver.pojo.User;
import com.example.beamagayver.pojo.UserActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "ProfileBottomSheet";

    @BindView(R.id.user_profile_photo)
    CircleImageView userProfilePhoto;
    @BindView(R.id.name_header)
    TextView nameHeader;
    @BindView(R.id.instructor_session_count)
    TextView instructorSessionCount;
    @BindView(R.id.user_type)
    TextView userType;
    String uid;
    @BindView(R.id.icon_close)
    ImageView iconClose;
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.phone_number_lb)
    LinearLayout phoneNumberLb;

    ProfileBottomSheet(String uid) {
        this.uid = uid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        iconClose.setOnClickListener(this);
        getUser();
    }

    private void getUser() {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("instructor").document(uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    User user = task.getResult().toObject(User.class);
                    if (user != null) {
                        Log.i(TAG, "getUser: " + user.getUid());
                        setViews(user);
                        db.collection("instructor").document(uid).collection("activities")
                                .get().addOnCompleteListener(task1 -> {
                            int count = 0;
                            if (task1.isSuccessful() && task1.isComplete() && task1.getResult() != null) {
                                List<UserActivity> userActivities = task1.getResult().toObjects(UserActivity.class);
                                for (UserActivity activities : userActivities) {
                                    if (activities.isCreated()) count++;
                                }
                                String s = "Created " + count + " Sessions";
                                instructorSessionCount.setVisibility(View.VISIBLE);
                                instructorSessionCount.setText(s);
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "getUser: " + e.getMessage());
        }
    }

    private void setViews(User user) {
        Log.i(TAG, "setViews: " + user.getUid());
        String name = user.getName();
        String photoUri = user.getPhotoUri();
        String phoneNumber = user.getPhoneNumber();
        if (phoneNumber == null) {
            phoneNumberLb.setVisibility(View.GONE);
        }else {
            phoneTv.setText(phoneNumber);
        }
        Picasso.get().load(Uri.parse(photoUri)).into(userProfilePhoto);
        nameHeader.setText(name);
        userType.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(iconClose)) {
            dismiss();
        }
    }
}
