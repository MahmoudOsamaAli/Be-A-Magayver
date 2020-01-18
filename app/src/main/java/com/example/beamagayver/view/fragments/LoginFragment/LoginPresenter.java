package com.example.beamagayver.view.fragments.LoginFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.beamagayver.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;


public class LoginPresenter {

    private static final String TAG = "LoginPresenter";
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Context mContext;
    private LoginView.onRegistrationListener view;
    private LoginView.onStartActivityForResult listener;
    CallbackManager mCallbackManager;

    LoginPresenter(Context mContext, LoginView.onRegistrationListener view, LoginView.onStartActivityForResult mListener, FirebaseAuth auth) {
        this.mContext = mContext;
        this.view = view;
        this.mAuth = auth;
        this.listener = mListener;

    }

    void startSignWithGoogle() {

        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(mContext.getResources().getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            Log.i(TAG, "startSignWithGoogle: got gso " + gso);
            mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);
            Log.i(TAG, "startSignWithGoogle: got google sign in client" + mGoogleSignInClient);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            Log.i(TAG, "startSignWithGoogle: start make an intent");
            listener.onStartActivityForResult(signInIntent);
            Log.i(TAG, "startSignWithGoogle: now start activity for result");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void handleAccessTokenGoogle(GoogleSignInAccount acct) {
        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            Log.i(TAG, "handleAccessTokenFB: getting credential for google login" + credential);
            getCredentialWithGoogle(credential);
            Log.i(TAG, "handleAccessTokenFB: calling getCredential ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCredentialWithGoogle(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mContext, task -> {
                    try {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            view.onSuccess(user);
                        } else {
                            Log.i(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            view.onFailure("wrong");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    void startSignInWithFB() {
        try {
//            FacebookSdk.sdkInitialize(mContext);
//            AppEventsLogger.activateApp(mContext);
//            mCallbackManager = CallbackManager.Factory.create();
            Log.i(TAG, "startSignInWithFB: init call back manager");
            LoginManager.getInstance().logInWithReadPermissions((Activity) mContext, Arrays.asList("email", "public_profile"));
            Log.i(TAG, "startSignInWithFB: setting permissions to LoginManager  like email and profile");
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.i(TAG, "register callback  facebook:onSuccess:" + loginResult);
                    handleAccessTokenFB(loginResult.getAccessToken());
                    Log.i(TAG, "register callback onSuccess: calling method handleAccessTokenFB with a null GoogleSignInAccount");
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.i(TAG, "facebook:onError", error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAccessTokenFB(AccessToken token) {
        try {
            Log.i(TAG, "handleAccessTokenFB: acct = " + token);
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            Log.i(TAG, "handleAccessTokenFB: getting credential for Facebook login" + credential);
            getCredentialWithFB(credential);
            Log.i(TAG, "handleAccessTokenFB: calling getCredential ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getCredentialWithFB(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mContext, task -> {
                    try {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            view.onSuccess(user);
//                            listener.onStartActivityForResult();
                        } else {
                            Log.i(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

    }
}
