package com.example.beamagayver.view.fragments.LoginFragment;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

public interface LoginView {

    interface onStartActivityForResult {

        void onStartActivityForResult(Intent i);
    }

    interface onRegistrationListener{
        void onSuccess(FirebaseUser firebaseUser);
        void onFailure(String message);
    }
}
