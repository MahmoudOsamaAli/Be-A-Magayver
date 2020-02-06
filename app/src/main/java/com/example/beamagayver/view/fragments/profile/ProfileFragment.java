package com.example.beamagayver.view.fragments.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.beamagayver.R;
import com.example.beamagayver.data.PrefManager;
import com.example.beamagayver.pojo.User;
import com.example.beamagayver.pojo.UserActivity;
import com.example.beamagayver.view.activities.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "ProfileFragment";
    private static final int PICK_IMAGE_REQUEST = 1;
    private Picasso picasso;
    private PrefManager prefManager;
    private String id;
    private String name;
    private String email;
    private String photoUri;
    private String type;
    private String phoneNumber;
    private String country;
    private int sessionCount;
    private boolean isEditNameState = false;
    private boolean isEditPhoneState = false;
    private boolean isEditCountryState = false;
    @BindView(R.id.user_profile_photo)
    CircleImageView userProfilePhoto;
    @BindView(R.id.change_photo)
    CircleImageView changePhoto;
    @BindView(R.id.account_name)
    EditText accountNameET;
    @BindView(R.id.edit_name)
    ImageView editNameButton;
    @BindView(R.id.mail)
    TextView accountEmail;
    @BindView(R.id.phone_number)
    EditText phoneNumberET;
    @BindView(R.id.edit_phone_number)
    ImageView editPhoneNumberButton;
    @BindView(R.id.country_text)
    EditText countryET;
    @BindView(R.id.edit_country)
    ImageView editCountryButton;
    @BindView(R.id.sign_out)
    TextView signOut;
    @BindView(R.id.save_profile_photo)
    ImageView saveProfilePhoto;
    @BindView(R.id.restore_photo)
    ImageView restorePhoto;
    @BindView(R.id.change_photo_layout)
    LinearLayout changePhotoLayout;
    @BindView(R.id.name_header)
    TextView nameHeader;
    @BindView(R.id.user_type)
    TextView userTypeTV;
    @BindView(R.id.instructor_session_count)
    TextView sessionCountTV;
    @BindView(R.id.create_admin)
    Button createAdmin;


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
            picasso = Picasso.get();
            prefManager = new PrefManager(getContext());
            loadUserData();
            setViews();
            accountNameET.setFocusable(false);
            phoneNumberET.setFocusable(false);
            countryET.setFocusable(false);
            accountNameET.setOnFocusChangeListener(this);
            phoneNumberET.setOnFocusChangeListener(this);
            countryET.setOnFocusChangeListener(this);
            restorePhoto.setOnClickListener(this);
            editPhoneNumberButton.setOnClickListener(this);
            editCountryButton.setOnClickListener(this);
            editNameButton.setOnClickListener(this);
            signOut.setOnClickListener(this);
            changePhoto.setOnClickListener(this);
            saveProfilePhoto.setOnClickListener(this);
            createAdmin.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "init: " + e.getMessage());
        }
    }

    private void setViews() {
        Log.i(TAG, "setViews: started");
        accountNameET.setText(name);
        accountEmail.setText(email);
        nameHeader.setText(name);
        countryET.setText(country);
        phoneNumberET.setText(phoneNumber);
        picasso.load(photoUri).into(userProfilePhoto);
        if (type.equals("instructor")) {
            userTypeTV.setText("Instructor");
            sessionCountTV.setVisibility(View.VISIBLE);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("instructor").document(id).collection("activities")
                    .get().addOnCompleteListener(task1 -> {
                int count = 0;
                if (task1.isSuccessful() && task1.isComplete() && task1.getResult() != null) {
                    List<UserActivity> userActivities = task1.getResult().toObjects(UserActivity.class);
                    for (UserActivity activities : userActivities) {
                        if (activities.isCreated()) count++;
                    }
                    String s = "Created " + count + " Sessions";
                    sessionCountTV.setVisibility(View.VISIBLE);
                    sessionCountTV.setText(s);
                }
            });
//            String count = "Created "+sessionCount +" sessions";
//            sessionCountTV.setText(count);

        }else if(type.equals("user")){
            userTypeTV.setText("New learner");
        }else{
            userTypeTV.setText("Admin");
        }
    }

    private void loadUserData() {
        Log.i(TAG, "loadUserData: started");
        id = prefManager.readString(getResources().getString(R.string.account_id));
        name = prefManager.readString(getResources().getString(R.string.account_name));
        email = prefManager.readString(getResources().getString(R.string.account_email));
        photoUri = prefManager.readString(getResources().getString(R.string.account_photo));
        type = prefManager.readString(getResources().getString(R.string.account_type));
        country = prefManager.readString(getResources().getString(R.string.account_country));
        phoneNumber = prefManager.readString(getResources().getString(R.string.account_phone_number));
//        sessionCount = prefManager.readInt(getResources().getString(R.string.session_count));
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Log.i(TAG, "onActivityResult: uri = " + uri);
            Picasso.get().load(uri.toString()).into(userProfilePhoto);
            photoUri = uri.toString();
            changePhotoLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == signOut) signOutUser();
        else if (view.equals(changePhoto)) openGallery();
        else if (view == editNameButton) startEditAccountName();
        else if (view == editPhoneNumberButton) startEditAccountPhoneNumber();
        else if (view == editCountryButton) startEditCountry();
        else if (view == saveProfilePhoto) startChangingUserPhoto();
        else if (view == restorePhoto) restoreOldPhoto();
        else if(view == createAdmin) createDialog();

    }

    private void createDialog() {
        AddAdminDialog addAdminDialog = new AddAdminDialog();
        addAdminDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager() , addAdminDialog.getTag());
    }

    private void startChangingUserPhoto() {
        changePhotoLayout.setVisibility(View.GONE);
        saveData();
    }

    private void signOutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), WelcomeActivity.class));
    }

    private void restoreOldPhoto() {
        photoUri = prefManager.readString(getResources().getString(R.string.account_photo));
        picasso.load(photoUri).into(userProfilePhoto);
        changePhotoLayout.setVisibility(View.GONE);
    }

    private void startEditCountry() {
        if (!isEditCountryState) {
            isEditCountryState = true;
            //disable phone number edit text
            phoneNumberET.setFocusable(false);
            phoneNumberET.setFocusableInTouchMode(false);
            isEditPhoneState = false;
            editPhoneNumberButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            //disable account name edit text
            accountNameET.setFocusable(false);
            accountNameET.setFocusableInTouchMode(false);
            isEditNameState = false;
            editNameButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            //enable country edit text
            countryET.setFocusableInTouchMode(true);
            editCountryButton.setImageResource(R.drawable.ic_check_green_24dp);
        } else {
            String newCountry = countryET.getText().toString();
            if (!newCountry.equals(country) && !newCountry.equals("")) {
                country = newCountry;
                countryET.setText(country);
                saveData();
            }
            //disable country edit text
            isEditCountryState = false;
            countryET.setFocusable(false);
            countryET.setFocusableInTouchMode(false);
            editCountryButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
        }
    }

    private void startEditAccountPhoneNumber() {
        if (!isEditPhoneState) {
            isEditPhoneState = true;
            //disable country edit text
            countryET.setFocusable(false);
            countryET.setFocusableInTouchMode(false);
            isEditCountryState = false;
            editCountryButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            accountNameET.setFocusable(false);
            accountNameET.setFocusableInTouchMode(false);
            //disable account name edit text
            isEditNameState = false;
            editNameButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            //enable phone number edit text
            phoneNumberET.setFocusableInTouchMode(true);
            editPhoneNumberButton.setImageResource(R.drawable.ic_check_green_24dp);
        } else {
            isEditPhoneState = false;
            String newNumber = phoneNumberET.getText().toString();
            if (!newNumber.equals(phoneNumber)) {
                phoneNumber = newNumber;
                phoneNumberET.setText(phoneNumber);
                saveData();
            }
            //disable phone number edit text
            phoneNumberET.setFocusable(false);
            phoneNumberET.setFocusableInTouchMode(false);
            editPhoneNumberButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
        }

    }

    private void startEditAccountName() {
        if (!isEditNameState) {
            isEditNameState = true;
            // disable phone number
            phoneNumberET.setFocusable(false);
            phoneNumberET.setFocusableInTouchMode(false);
            isEditPhoneState = false;
            editPhoneNumberButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            //disable country edit
            countryET.setFocusable(false);
            countryET.setFocusableInTouchMode(false);
            isEditCountryState = false;
            editCountryButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            // enable account name edit
            accountNameET.setFocusableInTouchMode(true);
            editNameButton.setImageResource(R.drawable.ic_check_green_24dp);
        } else {
            // disable account name edit
            isEditNameState = false;
            accountNameET.setFocusable(false);
            accountNameET.setFocusableInTouchMode(false);
            editNameButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            String newName = accountNameET.getText().toString();
            if (!newName.equals("") && !newName.equals(name)) {
                name = newName;
                accountNameET.setText(name);
                nameHeader.setText(name);
                saveData();
            }
        }
    }

    private void saveData() {

        saveInFireStore();
        saveInSharedPref();
        updatePosts();
    }

    private void saveInSharedPref() {
        prefManager.saveString(getResources().getString(R.string.account_photo), photoUri);
        prefManager.saveString(getResources().getString(R.string.account_id), id);
        prefManager.saveString(getResources().getString(R.string.account_name), name);
        prefManager.saveString(getResources().getString(R.string.account_email), email);
        prefManager.saveString(getResources().getString(R.string.account_type), type);
        prefManager.saveString(getResources().getString(R.string.account_country), country);
        prefManager.saveString(getResources().getString(R.string.account_phone_number), phoneNumber);
    }

    private void saveInFireStore() {
        User user = new User(id, name, email, photoUri, type);
        user.setCountry(country);
        user.setPhoneNumber(phoneNumber);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(type).document(id).set(user)
                .addOnSuccessListener(task -> {
                    saveProfilePhoto.setVisibility(View.GONE);
                    Log.i(TAG, "saveData: on success");
                    Toast.makeText(getContext(), "your profile info has been updated", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Couldn't save new profile info", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "saveData: on failure listener exception " + e.getMessage());
        }).addOnCompleteListener(task -> {
            saveProfilePhoto.setVisibility(View.GONE);
            Log.i(TAG, "saveData: on Complete");
        });
    }

    private void updatePosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Sessions").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<String> list = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    String currID = document.get("mOwnerID").toString();
                    if(currID.equals(id)) list.add(document.getId());
                }
                updateData(list);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    private void updateData(ArrayList<String> list) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get a new write batch
        WriteBatch batch = db.batch();

        // Iterate through the list
        for (int k = 0; k < list.size(); k++) {
            // Update each list item
            DocumentReference ref = db.collection("Sessions").document(list.get(k));
            batch.update(ref, "mOwnerImage", photoUri);
            batch.update(ref, "mOwnerName" , name);
        }

        // Commit the batch
        batch.commit().addOnCompleteListener(task -> {
            // Yay its all done in one go!
        });

    }

    @Override
    public void onFocusChange(View view, boolean focused) {
        InputMethodManager keyboard = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focused && keyboard != null)
            keyboard.showSoftInput(view, 0);
        else if (keyboard != null) {
            keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
