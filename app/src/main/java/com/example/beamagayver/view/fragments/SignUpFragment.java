package com.example.beamagayver.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.beamagayver.R;
import com.example.beamagayver.view.activities.HomeActivity;
import com.example.beamagayver.view.activities.WelcomeActivity;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "SignUpFragment";
    @BindView(R.id.tv_sign_up)
    TextView tvSignUp;
    @BindView(R.id.first_last_name_et)
    EditText firstLastNameEt;
    @BindView(R.id.email_et)
    EditText emailEt;
    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.confirm_password_et)
    EditText confirmPasswordEt;
    @BindView(R.id.normal_sign_button)
    Button normalSignButton;
    @BindView(R.id.sign_google_button)
    GoogleSignInButton signGoogleButton;
    @BindView(R.id.fb_login_button)
    Button fbLoginButton;
    @BindView(R.id.twitter_login_button)
    Button twitterLoginButton;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.rellay2)
    LinearLayout rellay2;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this , view);
        init();
    }

    private void init() {
        try{
            normalSignButton.setOnClickListener(this);
            signGoogleButton.setOnClickListener(this);
            fbLoginButton.setOnClickListener(this);
            twitterLoginButton.setOnClickListener(this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()){
                case R.id.normal_sign_button:
                case R.id.sign_google_button:
                case R.id.fb_login_button:
                case R.id.twitter_login_button:
                    startActivity(new Intent(getContext() , HomeActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
