package com.example.beamagayver.view.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.beamagayver.R;
import com.example.beamagayver.Utilities.ChangeFragments;
import com.example.beamagayver.view.fragments.LoginFragment.RegisterMethodsFragment;

import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        if(savedInstanceState == null) {
            ChangeFragments.setFragments(this ,new RegisterMethodsFragment() , ChangeFragments.RIGHT_TO_LEFT);
        }
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        String fragmentName = ChangeFragments.currFragment.getClass().getCanonicalName();
        if(fragmentName != null && !fragmentName.isEmpty() &&
                fragmentName.equals(RegisterMethodsFragment.class.getCanonicalName())){
            finish();
        }
    }
}
