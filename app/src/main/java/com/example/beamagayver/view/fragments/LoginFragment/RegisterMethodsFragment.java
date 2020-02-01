package com.example.beamagayver.view.fragments.LoginFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.beamagayver.data.PrefManager;
import com.example.beamagayver.pojo.User;
import com.example.beamagayver.view.activities.HomeActivity;
import com.example.beamagayver.view.activities.WelcomeActivity;
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


import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private CallbackManager mCallbackManager;
    private PrefManager mPrefManager;
    private Handler handler = new Handler();
    private Runnable runnableLogIn = new Runnable() {
        @Override
        public void run() {
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
        if (!checkForCurrentUser()) init();
    }

    private void init() {
        try {
            presenter = new LoginPresenter(mContext, this, this, mAuth);
            mPrefManager = new PrefManager(mContext);
            mCallbackManager = CallbackManager.Factory.create();
            methods.setVisibility(View.VISIBLE);
            instructorButton.setOnClickListener(this);
            newClientButton.setOnClickListener(this);
            googleForInstructor.setOnClickListener(this);
            googleForUser.setOnClickListener(this);
            fbForInstructor.setOnClickListener(this);
            fbForUser.setOnClickListener(this);
            twitterForInstructor.setOnClickListener(this);
            fbForUser.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean checkForCurrentUser() {
        mContext = getContext();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mContext.startActivity(new Intent(mContext, HomeActivity.class));
            getActivity().finish();
            return true;
        }
        return false;
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

    private void updateUI(FirebaseUser user) {
        try {
            if (user != null) {
                String type = "";
                if (Buser) type = "user";
                else if (Binstructor) type = "instructor";
                // make user model
                User newUser = new User();
                newUser.setUid(user.getUid());
                newUser.setName(user.getDisplayName());
                newUser.setEmail(user.getEmail());
                newUser.setPhotoUri(modifyPhoto(user.getPhotoUrl()));
                newUser.setUserType(type);
                //save in shared preferences
                saveInSharedPref(newUser);
                // save user in fire store
                saveInCloud(newUser);

                startActivity(new Intent(getContext(), HomeActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            } else Toast.makeText(mContext, "user is null", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "updateUI: " + e.getMessage());
        }
    }

    private String modifyPhoto(Uri photoUrl) {
        String originalPieceOfUrl = "s96-c/photo.jpg";
        String newPieceOfUrlToAdd = "s400-c/photo.jpg";
        String photoPath = photoUrl.toString();
        return photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
    }

    private void saveInCloud(User newUser) {
        FireStoreProcess.addUser(newUser);
        Log.i(TAG, "updateUI: user has been added to Firebase");
    }

    private void saveInSharedPref(User user) {
        mPrefManager.saveString(getResources().getString(R.string.account_id), user.getUid());
        mPrefManager.saveString(getResources().getString(R.string.account_name), user.getName());
        mPrefManager.saveString(getResources().getString(R.string.account_email), user.getEmail());
        mPrefManager.saveString(getResources().getString(R.string.account_photo), user.getPhotoUri());
        mPrefManager.saveString(getResources().getString(R.string.account_type), user.getUserType());
        mPrefManager.saveString(getResources().getString(R.string.account_country), "Egypt");
        mPrefManager.saveString(getResources().getString(R.string.account_phone_number), "empty field");
        if (user.getUserType().equals("instructor"))
            mPrefManager.saveInt(getResources().getString(R.string.session_count), 0);
        Log.i(TAG, "saveInSharedPref: user saved in shared preferences");
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
