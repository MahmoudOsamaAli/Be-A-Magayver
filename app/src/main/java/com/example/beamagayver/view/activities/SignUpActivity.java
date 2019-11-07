package com.example.beamagayver.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beamagayver.R;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.normal_sign_button)
    Button normalSignButton;
    @BindView(R.id.sign_google_button)
    GoogleSignInButton signGoogleButton;
    @BindView(R.id.fb_login_button)
    Button fbLoginButton;
    @BindView(R.id.twitter_login_button)
    Button twitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        normalSignButton.setOnClickListener(this);
        signGoogleButton.setOnClickListener(this);
        fbLoginButton.setOnClickListener(this);
        twitterLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(SignUpActivity.this , HomeActivity.class));
    }
}
