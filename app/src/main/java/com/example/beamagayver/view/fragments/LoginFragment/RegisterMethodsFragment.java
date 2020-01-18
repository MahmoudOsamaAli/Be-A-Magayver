package com.example.beamagayver.view.fragments.LoginFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.beamagayver.R;
import com.example.beamagayver.data.FireStoreProcess;
import com.example.beamagayver.pojo.User;
import com.example.beamagayver.view.activities.HomeActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shobhitpuri.custombuttons.GoogleSignInButton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RegisterMethodsFragment extends Fragment implements View.OnClickListener, LoginView.onRegistrationListener, LoginView.onStartActivityForResult {

    private static final String TAG = "RegisterMethodsFragment";
    private static final String EMAIL = "email";
    private boolean Badmin;
    private boolean Buser;
    private boolean Binstructor;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.instructor_button)
    Button instructorButton;
    @BindView(R.id.new_client_button)
    Button newClientButton;
    @BindView(R.id.admin_button)
    Button adminButton;
    @BindView(R.id.methods)
    LinearLayout methods;
    @BindView(R.id.instructor_login_methods)
    LinearLayout instructorMethods;
    @BindView(R.id.user_login_methods)
    LinearLayout userMethods;
    @BindView(R.id.google_for_instructor)
    GoogleSignInButton googleForInstructor;
    @BindView(R.id.fb_for_instructor)
    Button fbForInstructor;
    @BindView(R.id.twitter_for_instructor)
    Button twitterForInstructor;
    @BindView(R.id.google_for_user)
    GoogleSignInButton googleForUser;
    @BindView(R.id.fb_for_user)
    Button fbForUser;
    @BindView(R.id.twitter_for_user)
    Button twitterForUser;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private Context mContext;
    private LoginPresenter presenter;
    CallbackManager mCallbackManager;

    Handler handler = new Handler();
    Runnable runnableLogIn = new Runnable() {
        @Override
        public void run() {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                updateUI(currentUser);
            }
            methods.setVisibility(View.VISIBLE);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_methods, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        try {
            mCallbackManager = CallbackManager.Factory.create();
            methods.setVisibility(View.GONE);
            handler.postDelayed(runnableLogIn, 2500);
            instructorButton.setOnClickListener(this);
            newClientButton.setOnClickListener(this);
            googleForInstructor.setOnClickListener(this);
            googleForUser.setOnClickListener(this);
            fbForInstructor.setOnClickListener(this);
            fbForUser.setOnClickListener(this);
            twitterForInstructor.setOnClickListener(this);
            fbForUser.setOnClickListener(this);
            mAuth = FirebaseAuth.getInstance();
            mContext = getContext();

            presenter = new LoginPresenter(mContext, this, this, mAuth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.instructor_button:
                if (instructorMethods.getVisibility() == View.GONE) {
                    instructorMethods.setVisibility(View.VISIBLE);
                    userMethods.setVisibility(View.GONE);
                } else instructorMethods.setVisibility(View.GONE);
                break;
            case R.id.new_client_button:
                if (userMethods.getVisibility() == View.GONE) {
                    userMethods.setVisibility(View.VISIBLE);
                    instructorMethods.setVisibility(View.GONE);
                } else userMethods.setVisibility(View.GONE);
                break;
            case R.id.google_for_instructor:
                presenter.startSignWithGoogle();
                Binstructor = true;
                break;
            case R.id.google_for_user:
                presenter.startSignWithGoogle();
                Buser = true;
                break;
            case R.id.fb_for_instructor:
                Binstructor = true;
                startSignInWithFB();
                break;
            case R.id.fb_for_user:
//                presenter.startSignInWithFB();
                Buser = true;
                startSignInWithFB();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                task.addOnSuccessListener(googleSignInAccount ->
                        Log.i(TAG, "GoogleSignIn onSuccess(): is called"))
                        .addOnCompleteListener(task1 ->
                                Log.i(TAG, "GoogleSignIn onComplete(): is called")).
                        addOnFailureListener(e -> {
                            Log.i(TAG, "GoogleSignIn onFailure(): is called");
                            e.printStackTrace();
                        });
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    presenter.handleAccessTokenGoogle(account);
                    Log.i(TAG, "onActivityResult: calling handleAccessTokenFB for google sign in " + account);
                } else {
                    Toast.makeText(mContext, "account is null", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            e.printStackTrace();
            Log.w(TAG, "Google sign in failed", e);
            Toast.makeText(mContext, "Google sign in failed", Toast.LENGTH_SHORT).show();
        }
        if (mCallbackManager != null) {

            try {
                boolean b = mCallbackManager.onActivityResult(requestCode, resultCode, data);
                Log.i(TAG, "onActivityResult: calling mCallBackManager " + b);
            } catch (Exception e) {
                Log.i(TAG, "onActivityResult: exception " + e.getMessage());

            }
        }

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String type = "";
            if (Buser) type = "user";
            else if (Binstructor) type = "instructor";
            if (Buser || Binstructor) {
                User mUser = new User(name, email, type);
                FireStoreProcess.addUser(mUser);
                Log.i(TAG, "updateUI: user has been added to Firebase");
            }

            startActivity(new Intent(getContext(), HomeActivity.class));
            Objects.requireNonNull(getActivity()).finish();
        } else Toast.makeText(mContext, "user is null", Toast.LENGTH_SHORT).show();

//        try {
//            Log.i(TAG, "updateUI(): is called");
//            if (user != null) {
//                Log.i(TAG, "updateUI(): FirebaseUser != null");
//
//                User newUser = new User();
//                newUser.setUid(user.getUid());
//                newUser.setName(user.getDisplayName());
//                newUser.setEmail(user.getEmail());
//                newUser.setTokenId(FirebaseInstanceId.getInstance().getToken());
//
//                //TODO save user data into shared preferences
//                mPrefManager.saveString(PrefManager.USER_ID, user.getUid());
//                mPrefManager.saveString(PrefManager.USER_TOKEN, newUser.getTokenId());
//                mPrefManager.saveString(PrefManager.USER_NAME, newUser.getTokenId());
//
//                Log.i(TAG, "updateUI(): user ID: " + user.getUid() + " saved in sharedPreference");
//                Log.i(TAG, "updateUI(): user ID: " + user.getDisplayName() + " saved in sharedPreference");
//
//                if (user.getEmail() != null) {
//                    mPrefManager.saveString(PrefManager.USER_EMAIL, user.getEmail());
//                    Log.i(TAG, "updateUI(): user email: " + user.getEmail());
//                }
//
//                if (user.getPhoneNumber() != null) {
//                    mPrefManager.saveString(PrefManager.USER_PHONE, user.getPhoneNumber());
//                    Log.i(TAG, "updateUI(): user phone: " + user.getPhoneNumber());
//                }
//
//                if (AppUtils.inNetwork(mCurrent)) {
//                    if (mPresenter != null) {
//                        Log.i(TAG, "updateUI(): saving user into firestore");
//                        mPresenter.saveUserIntoFireStore(newUser);
//                    }
//                }else{
//                    AppUtils.showAlertDialog(mCurrent,getString(R.string.check_network_connection));
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void startSignInWithFB() {
        try {
            mCallbackManager = CallbackManager.Factory.create();
            Log.i(TAG, "startSignInWithFB: init call back manager");
            LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile"));
            Log.i(TAG, "startSignInWithFB: setting permissions to LoginManager  like email");

            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Toast.makeText(mContext, "on fucking success", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "register callback  facebook:onSuccess:" + loginResult);
                    handleAccessTokenFB(loginResult.getAccessToken());
                    Log.i(TAG, "register callback onSuccess: calling method handleAccessTokenFB with a null GoogleSignInAccount");
                }

                @Override
                public void onCancel() {
                    Log.i(TAG, "facebook:onCancel");
                    Toast.makeText(mContext, "on fucking cancel", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Log.i(TAG, "facebook:onError", error);
                    Toast.makeText(mContext, "on fucking error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "startSignInWithFB: exception is occured " + e.getMessage());
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
                            updateUI(user);
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

    @Override
    public void onStartActivityForResult(Intent i) {
        Log.i(TAG, "onStartActivityForResult: called");
        startActivityForResult(i, RC_SIGN_IN);

    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        Log.i(TAG, "onSuccess: for registration called");
        updateUI(firebaseUser);
    }

    @Override
    public void onFailure(String message) {
        Log.i(TAG, "onFailure: for registration called");
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
