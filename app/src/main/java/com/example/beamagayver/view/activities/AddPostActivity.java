package com.example.beamagayver.view.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.beamagayver.R;
import com.example.beamagayver.Utilities.DatePickerFragment;
import com.example.beamagayver.Utilities.Dialog;
import com.example.beamagayver.Utilities.MapSheet;
import com.example.beamagayver.Utilities.TimePickerFragment;
import com.example.beamagayver.data.FireStoreProcess;
import com.example.beamagayver.pojo.LocationModel;
import com.example.beamagayver.pojo.Post;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener
        , TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener
        , Dialog.DialogListener, MapSheet.onGetLocation {
    private static final String TAG = "AddPostActivity";
    FirebaseUser user;
    @BindView(R.id.start_time_label)
    TextView startTimeLabel;
    @BindView(R.id.start_date_label)
    TextView startDateLabel;
    @BindView(R.id.start_date)
    TextView startDateTV;
    @BindView(R.id.start_time)
    TextView startTimeTV;
    @BindView(R.id.car_details_button)
    TextView carDetailsLabel;
    @BindView(R.id.car_details_tv)
    TextView carDetailsTV;
    @BindView(R.id.session_duration_label)
    TextView durationLabel;
    @BindView(R.id.session_duration_tv)
    TextView sessionDurationTV;
    @BindView(R.id.post_edit_text)
    TextInputEditText postCaption;
    @BindView(R.id.post_owner_imaage)
    CircleImageView postOwnerImage;
    @BindView(R.id.post_owner_name_tv)
    TextView postOwnerNameTv;
    @BindView(R.id.phone_number_label)
    TextView phoneNumberLabel;
    @BindView(R.id.phone_number)
    TextView phoneNumber;
    @BindView(R.id.locations_tv)
    TextView locationTV;
    @BindView(R.id.add_location_label)
    TextView addLocationLabel;
    @BindView(R.id.locations_icon)
    ConstraintLayout locationIcon;
    LocationModel locationModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        try {
            user = FirebaseAuth.getInstance().getCurrentUser();
            actionBarConfig();
            setUserInfo();
            durationLabel.setOnClickListener(this);
            startTimeLabel.setOnClickListener(this);
            startDateLabel.setOnClickListener(this);
            carDetailsLabel.setOnClickListener(this);
            phoneNumberLabel.setOnClickListener(this);
            addLocationLabel.setOnClickListener(this);
        } catch (Exception e) {
            Log.i(TAG, "init: exception  ,  " + e.getMessage());
        }
    }

    private void setUserInfo() {
        postOwnerNameTv.setText(user.getDisplayName());
        Picasso.get().load(user.getPhotoUrl()).into(postOwnerImage);
    }

    private void actionBarConfig() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.create_session));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.post) {
            createPost();
            Log.i(TAG, "onOptionsItemSelected: finish the activity");
        } else if (item.getItemId() == android.R.id.home) {
            Log.i(TAG, "onOptionsItemSelected: return to home");
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createPost() {
        try {
            if (user != null && checkViews()) {
                String ownerName = user.getDisplayName();
                String ownerImage = Objects.requireNonNull(user.getPhotoUrl()).toString();
                String postTime = DateFormat.format("dd/MM/yyyy hh:mm a", new Date()).toString();
                Log.i(TAG, "createPost: post time" + postTime);
                String caption = Objects.requireNonNull(postCaption.getText()).toString();
                String carDetails = carDetailsTV.getText().toString();
                String duration = sessionDurationTV.getText().toString();
                String startDate = startDateTV.getText().toString();
                String startTime = startTimeTV.getText().toString();
                String id = user.getUid();
                String number = phoneNumber.getText().toString();
                Post post = new Post(ownerName, id, ownerImage, postTime, caption, carDetails, duration
                        , startDate, startTime, 0, 0, number, locationModel);
                FireStoreProcess.addPostToUser(user, post);
                Log.i(TAG, "createPost: successfully");
                Toast.makeText(this, "Session has been added Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "createPost: " + e.getMessage());
        }
    }

    private boolean checkViews() {
        if (carDetailsTV.getText().toString().isEmpty()) {
            carDetailsLabel.setError("Required");
            return false;
        } else if (sessionDurationTV.getText().toString().isEmpty()) {
            durationLabel.setError("Required");
            return false;
        } else if (startDateTV.getText().toString().isEmpty()) {
            startDateLabel.setError("Required");
            return false;
        } else if (startTimeTV.getText().toString().isEmpty()) {
            startDateLabel.setError("Required");
            return false;
        } else if (phoneNumber.getText().toString().isEmpty()) {
            phoneNumberLabel.setError("Required");
            return false;
        } else if (locationTV.getText().toString().isEmpty()) {
            addLocationLabel.setError("Required");
            return false;
        } else if (Objects.requireNonNull(postCaption.getText()).toString().isEmpty()) {
            postCaption.setError("Required");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        Dialog dialog;
        switch (view.getId()) {
            case R.id.start_time_label:
                Log.i(TAG, "onClick: time picker");
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time Picker");
                break;
            case R.id.start_date_label:
                Log.i(TAG, "onClick: date picker");
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
                break;
            case R.id.car_details_button:
                dialog = new Dialog("Car Details");
                dialog.show(getSupportFragmentManager(), "Car Details");
                break;
            case R.id.phone_number_label:
                dialog = new Dialog("Phone Number");
                dialog.show(getSupportFragmentManager(), "Phone Number");
                break;
            case R.id.add_location_label:
                MapSheet map = new MapSheet();
                map.show(getSupportFragmentManager(), map.getTag());
                break;
            case R.id.session_duration_label:
                dialog = new Dialog("Session Duration");
                dialog.show(getSupportFragmentManager(), "Session Duration");
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        String time = hour + ":" + min;
        startTimeTV.setVisibility(View.VISIBLE);
        startTimeTV.setText(time);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = day + " / " + (month + 1) + " / " + year;
        startDateTV.setVisibility(View.VISIBLE);
        startDateTV.setText(date);
    }

    @Override
    public void applyCarDetails(String text) {
        if (text.isEmpty()) carDetailsLabel.setError("Required");
        else {
            carDetailsTV.setVisibility(View.VISIBLE);
            carDetailsTV.setText(text);
        }
    }

    @Override
    public void applyPhoneNumber(String number) {
        phoneNumber.setVisibility(View.VISIBLE);
        phoneNumber.setText(number);
    }

    @Override
    public void applyDurationText(String duration) {
        String text = duration + " hours";
        sessionDurationTV.setText(text);
        sessionDurationTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetLocationListener(LocationModel model) {
        locationModel = model;
        String city = model.getCity();
        String street = model.getStreet();
        String country = model.getCountry();
        String address = street + " , " + city + " , " + country;
        locationTV.setText(address);
        locationIcon.setVisibility(View.VISIBLE);
    }
}
