package com.example.beamagayver.view.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.Utilities.DatePickerFragment;
import com.example.beamagayver.Utilities.Dialog;
import com.example.beamagayver.Utilities.TimePickerFragment;
import com.example.beamagayver.data.FireStoreProcess;
import com.example.beamagayver.pojo.Post;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener
        , TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener
        , Dialog.DialogListener, AdapterView.OnItemSelectedListener{
    private static final String TAG = "AddPostActivity";
    public Location mCurrentLocation;
    private LatLng mSelectedLocation = null;
    FirebaseUser user;
    @BindView(R.id.start_time_label)
    Button startTimeButton;
    @BindView(R.id.start_date_label)
    Button startDateButton;
    @BindView(R.id.start_time)
    TextView startTimeTV;
    @BindView(R.id.start_date)
    TextView startDateTV;
    @BindView(R.id.car_details_button)
    Button carBrandButton;
    @BindView(R.id.car_details_tv)
    TextView carDetailsTV;
    @BindView(R.id.session_duration_spinner)
    Spinner durationSpinner;
    @BindView(R.id.session_duration_tv)
    TextView sessionDurationTV;
    @BindView(R.id.post_edit_text)
    TextInputEditText postCaption;
    @BindView(R.id.post_owner_imaage)
    CircleImageView postOwnerImaage;
    @BindView(R.id.post_owner_name_tv)
    TextView postOwnerNameTv;
    @BindView(R.id.phone_number_label)
    Button phoneNumberButton;
    @BindView(R.id.phone_number)
    TextView phoneNumber;
    @BindView(R.id.add_location_fab)
    FloatingActionButton addLocationFab;
    ImageView mUndoMarker;
    @BindView(R.id.fragment_add_place_locations_rv)
    RecyclerView mLocationsRV;

    Button mConfirmLocation;


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
            durationSpinner.setPrompt("Session Duration");
            durationSpinner.setOnItemSelectedListener(this);
            startTimeButton.setOnClickListener(this);
            startDateButton.setOnClickListener(this);
            carBrandButton.setOnClickListener(this);
            phoneNumberButton.setOnClickListener(this);
            addLocationFab.setOnClickListener(this);
            mUndoMarker.setOnClickListener(this);
            mConfirmLocation.setOnClickListener(this);
        } catch (Exception e) {
            Log.i(TAG, "init: " + e.getMessage());
        }
    }


    private void setUserInfo() {
        postOwnerNameTv.setText(user.getDisplayName());
        Picasso.get().load(user.getPhotoUrl()).into(postOwnerImaage);
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
            finish();
            Log.i(TAG, "onOptionsItemSelected: finish the activity");
        } else if (item.getItemId() == android.R.id.home) {
            Log.i(TAG, "onOptionsItemSelected: return to home");
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createPost() {
        try {
            if (user != null) {

                String ownerName = user.getDisplayName();
                String ownerImage = user.getPhotoUrl().toString();
                String postTime = getTime();
                Log.i(TAG, "createPost: post time" + postTime);
                String caption = postCaption.getText().toString();
                String carDetails = carDetailsTV.getText().toString();
                String duration = sessionDurationTV.getText().toString();
                String startDate = startDateTV.getText().toString();
                String startTime = startTimeTV.getText().toString();
                String id = user.getUid();
                String number = phoneNumber.getText().toString();
                Post post = new Post(ownerName, id, ownerImage, postTime, caption, carDetails, duration
                        , startDate, startTime, 0, 0, number , mSelectedLocation);
                FireStoreProcess.addPostToUser(user, post);

                Log.i(TAG, "createPost: successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "createPost: " + e.getMessage());
        }
    }


    private String getTime() {
        return DateFormat.format("dd/MM/yyyy hh:mm a", new Date()).toString();
    }

    @Override
    public void onClick(View view) {
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
                Dialog dialog = new Dialog("Car Details");
                dialog.show(getSupportFragmentManager(), "Car Details");
                break;
            case R.id.phone_number_label:
                Dialog dialog1 = new Dialog("Phone Number");
                dialog1.show(getSupportFragmentManager(), "Phone Number");
                break;
            case  R.id.add_location_fab:
                startActivity(new Intent(this , MapsActivity.class));
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        String time = hour + ":" + min;
        startTimeTV.setText(time);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = day + " / " + (month + 1) + " / " + year;
        startDateTV.setText(date);
    }

    @Override
    public void applyCarDetails(String text) {
        carDetailsTV.setText(text);
    }

    @Override
    public void applyPhoneNumber(String number) {
        phoneNumber.setText(number);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String text = parent.getItemAtPosition(i).toString();
        sessionDurationTV.setText(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
