package com.example.beamagayver.view.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.beamagayver.R;
import com.example.beamagayver.Utilities.Utilities;
import com.example.beamagayver.view.activities.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    public static final String FRG_NAME = "PROFILE";
    @BindView(R.id.user_profile_photo)
    CircleImageView userProfilePhoto;
    @BindView(R.id.change_photo)
    CircleImageView changePhoto;
    @BindView(R.id.name_icon)
    ImageView nameIcon;
    @BindView(R.id.name_label)
    TextView nameLabel;
    @BindView(R.id.account_name)
    TextView accountName;
    @BindView(R.id.edit_name)
    ImageView editName;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.mail_icon)
    ImageView mailIcon;
    @BindView(R.id.mail_label)
    TextView mailLabel;
    @BindView(R.id.mail)
    TextView mail;
    @BindView(R.id.edit_mail)
    ImageView editMail;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.phone_icon)
    ImageView phoneIcon;
    @BindView(R.id.phone_label)
    TextView phoneLabel;
    @BindView(R.id.phone_number)
    TextView phoneNumber;
    @BindView(R.id.edit_phone_number)
    ImageView editPhoneNumber;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.country_icon)
    ImageView countryIcon;
    @BindView(R.id.country_label)
    TextView countryLabel;
    @BindView(R.id.country_text)
    TextView countryText;
    @BindView(R.id.edit_country)
    ImageView editCountry;
    @BindView(R.id.profile_content_layout)
    ConstraintLayout profileContentLayout;
    private FirebaseUser user;
    @BindView(R.id.sign_out)
    Button signOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        try {
            user = FirebaseAuth.getInstance().getCurrentUser();
            signOut.setOnClickListener(this);
            if (user != null) updateProfileViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProfileViews() {
        String mName = user.getDisplayName();
        String mEmail = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        accountName.setText(mName);
        mail.setText(mEmail);
        if (photoUrl != null) Picasso.get().load(photoUrl.toString()).into(userProfilePhoto);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.sign_out){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext() , WelcomeActivity.class));
        }

    }
}
