package com.example.beamagayver.view.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.beamagayver.R;
import com.example.beamagayver.Utilities.CustomDialog;
import com.example.beamagayver.Utilities.DatePicker;
import com.example.beamagayver.Utilities.DialogListener;
import com.example.beamagayver.Utilities.GetLocation;
import com.example.beamagayver.Utilities.MapBottomSheet;
import com.example.beamagayver.Utilities.NumberUtils;
import com.example.beamagayver.Utilities.TimePicker;
import com.example.beamagayver.data.FireStoreProcess;
import com.example.beamagayver.data.PrefManager;
import com.example.beamagayver.pojo.JoinedModel;
import com.example.beamagayver.pojo.LikesModel;
import com.example.beamagayver.pojo.LocationModel;
import com.example.beamagayver.pojo.Post;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener
        , TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener
        , DialogListener, LocationListener {
    private static final String TAG = "AddPostActivity";
    private static final int REQUEST_LOCATION_CODE = 99;
    public static Location mCurrentLocation;
    @BindView(R.id.delete_car_details)
    ImageView deleteCarDetails;
    @BindView(R.id.delete_duration)
    ImageView deleteDuration;
    @BindView(R.id.delete_start_date)
    ImageView deleteStartDate;
    @BindView(R.id.delete_start_time)
    ImageView deleteStartTime;
    @BindView(R.id.delete_phone_number)
    ImageView deletePhoneNumber;
    @BindView(R.id.delete_price)
    ImageView deletePrice;
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
    @BindView(R.id.price_label)
    TextView priceLabel;
    @BindView(R.id.price_tv)
    TextView priceTv;
    @BindView(R.id.delete_location_ic)
    ImageView deleteLocationIcon;
    private PrefManager manager;
    private Post mEditPost;
    private LocationModel locationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ButterKnife.bind(this);
        init();
    }
    private void init() {
        try {
            GetLocation getLocation = new GetLocation(this, this, this);
            manager = new PrefManager(this);
            mEditPost = (Post) getIntent().getSerializableExtra("EditPost");
            if (mEditPost != null) {
                extractPost(mEditPost);
            }
            actionBarConfig();
            setUserInfo();
            postCaption.setOnClickListener(this);
            deleteCarDetails.setOnClickListener(this);
            deleteDuration.setOnClickListener(this);
            deleteStartDate.setOnClickListener(this);
            deleteStartTime.setOnClickListener(this);
            deletePhoneNumber.setOnClickListener(this);
            deletePrice.setOnClickListener(this);
            deleteLocationIcon.setOnClickListener(this);
            priceLabel.setOnClickListener(this);
            durationLabel.setOnClickListener(this);
            startTimeLabel.setOnClickListener(this);
            startDateLabel.setOnClickListener(this);
            carDetailsLabel.setOnClickListener(this);
            phoneNumberLabel.setOnClickListener(this);
            addLocationLabel.setOnClickListener(this);
            locationIcon.setOnClickListener(this);
        } catch (Exception e) {
            Log.i(TAG, "init: exception  ,  " + e.getMessage());
        }
    }

    private void extractPost(Post post) {
        postCaption.setText(post.getmPostCaption());
        postCaption.setVisibility(View.VISIBLE);
        carDetailsTV.setText(post.getmCarDetails());
        carDetailsTV.setVisibility(View.VISIBLE);
        deleteCarDetails.setVisibility(View.VISIBLE);
        String duration = post.getmDuration();
        sessionDurationTV.setText(duration);
        sessionDurationTV.setVisibility(View.VISIBLE);
        deleteDuration.setVisibility(View.VISIBLE);
        startDateTV.setText(post.getmStartDate());
        startDateTV.setVisibility(View.VISIBLE);
        deleteStartDate.setVisibility(View.VISIBLE);
        startTimeTV.setText(post.getmStartTime());
        startTimeTV.setVisibility(View.VISIBLE);
        deleteStartTime.setVisibility(View.VISIBLE);
        phoneNumber.setText(post.getmPhoneNumber());
        phoneNumber.setVisibility(View.VISIBLE);
        deletePhoneNumber.setVisibility(View.VISIBLE);
        String price = post.getmPrice();
        priceTv.setText(price);
        deletePrice.setVisibility(View.VISIBLE);
        priceTv.setVisibility(View.VISIBLE);
        locationModel = post.getmLocation();
        String location = locationModel.getStreet() + ", " + locationModel.getCity() + ", " + locationModel.getCountry();
        locationTV.setText(location);
        locationIcon.setVisibility(View.VISIBLE);
    }

    private void setUserInfo() {
        postOwnerNameTv.setText(manager.readString(getResources().getString(R.string.account_name)));
        Picasso.get().load(manager.readString(getResources().getString(R.string.account_photo))).into(postOwnerImage);
    }

    private void actionBarConfig() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && mEditPost == null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
            actionBar.setTitle(getResources().getString(R.string.create_session));
        }else if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
            actionBar.setTitle(getResources().getString(R.string.edit_session));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (mEditPost != null) inflater.inflate(R.menu.update_post, menu);
        else inflater.inflate(R.menu.add_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.update) {
            updatePost();
        }
        if (item.getItemId() == R.id.post) {
            createPost();
            Log.i(TAG, "onOptionsItemSelected: finish the activity");
        } else if (item.getItemId() == android.R.id.home) {
            Log.i(TAG, "onOptionsItemSelected: return to home");
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static Location getLocation() {
        return mCurrentLocation;
    }

    private void updatePost() {
        if (checkViews()) {
            String caption = Objects.requireNonNull(postCaption.getText()).toString();
            String carDetails = carDetailsTV.getText().toString();
            String duration = sessionDurationTV.getText().toString();
            String startDate = startDateTV.getText().toString();
            String startTime = startTimeTV.getText().toString();
            String number = phoneNumber.getText().toString();
            String price = priceTv.getText().toString();
            mEditPost.setmPostCaption(caption);
            mEditPost.setmCarDetails(carDetails);
            mEditPost.setmDuration(duration);
            mEditPost.setmStartDate(startDate);
            mEditPost.setmStartTime(startTime);
            mEditPost.setmPhoneNumber(number);
            mEditPost.setmPrice(price);
            mEditPost.setmLocation(locationModel);
            mEditPost.setDistance(locationModel.getDistance());
            FireStoreProcess.updatePost(mEditPost);
            Toast.makeText(this, "Updated post", Toast.LENGTH_SHORT).show();
            finish();

        }
    }

    private void createPost() {
        try {
            if (checkViews()) {
                String id = manager.readString(getResources().getString(R.string.account_id));
                String ownerName = manager.readString(getResources().getString(R.string.account_name));
                String ownerImage = manager.readString(getResources().getString(R.string.account_photo));
                int sessionCount = manager.readInt(getResources().getString(R.string.session_count));
                String postTime = DateFormat.format("dd/MM/yyyy hh:mm a", new Date()).toString();
                Log.i(TAG, "createPost: post time" + postTime);
                String caption = Objects.requireNonNull(postCaption.getText()).toString();
                String carDetails = carDetailsTV.getText().toString();
                String duration = sessionDurationTV.getText().toString();
                String startDate = startDateTV.getText().toString();
                String startTime = startTimeTV.getText().toString();
                String number = phoneNumber.getText().toString();
                String price = priceTv.getText().toString();
                LikesModel likes = new LikesModel(0, new ArrayList<>());
                JoinedModel joined = new JoinedModel(0, new ArrayList<>());
                Post post = new Post(ownerName, id, ownerImage, postTime, caption, carDetails, duration
                        , startDate, startTime, joined, likes, number, locationModel, price);
                post.setDistance(String.valueOf(NumberUtils.distance(locationModel.getLatitude(), locationModel.getLongitude(), mCurrentLocation)));
                FireStoreProcess.addPostToUser(post);
                manager.saveInt(getResources().getString(R.string.session_count), sessionCount + 1);
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
        } else if (locationTV.getText().toString().isEmpty() || locationModel == null) {
            Log.i(TAG, "checkViews: location model is null");
            addLocationLabel.setError("Required");
            return false;
        } else if (Objects.requireNonNull(postCaption.getText()).toString().isEmpty()) {
            postCaption.setError("Required");
            return false;
        } else if (Objects.requireNonNull(priceTv.getText()).toString().isEmpty()) {
            priceLabel.setError("Required");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        CustomDialog dialog;
        switch (view.getId()) {
            case R.id.start_time_label:
                Log.i(TAG, "onClick: time picker");
                DialogFragment timePicker = new TimePicker();
                timePicker.show(getSupportFragmentManager(), timePicker.getTag());
                break;
            case R.id.start_date_label:
                Log.i(TAG, "onClick: date picker");
                DialogFragment datePicker = new DatePicker();
                datePicker.show(getSupportFragmentManager(), datePicker.getTag());
                break;
            case R.id.car_details_button:
                dialog = new CustomDialog("Car Details");
                dialog.show(getSupportFragmentManager(), dialog.getTag());
                break;
            case R.id.phone_number_label:
                dialog = new CustomDialog("Phone Number");
                dialog.show(getSupportFragmentManager(), dialog.getTag());
                break;
            case R.id.add_location_label:
                MapBottomSheet map = new MapBottomSheet();
                map.show(getSupportFragmentManager(), map.getTag());
                break;
            case R.id.session_duration_label:
                dialog = new CustomDialog("Session Duration");
                dialog.show(getSupportFragmentManager(), dialog.getTag());
                break;
            case R.id.price_label:
                dialog = new CustomDialog("price");
                dialog.show(getSupportFragmentManager(), dialog.getTag());
                break;
            case R.id.delete_location_ic:
                locationTV.setText("");
                locationModel = null;
                locationIcon.setVisibility(View.GONE);
                break;
            case R.id.delete_car_details:
                carDetailsTV.setText("");
                carDetailsTV.setVisibility(View.GONE);
                deleteCarDetails.setVisibility(View.GONE);
                break;
            case R.id.delete_duration:
                sessionDurationTV.setText("");
                sessionDurationTV.setVisibility(View.GONE);
                deleteDuration.setVisibility(View.GONE);
                break;
            case R.id.delete_start_date:
                startDateTV.setText("");
                startDateTV.setVisibility(View.GONE);
                deleteStartDate.setVisibility(View.GONE);
                break;
            case R.id.delete_start_time:
                startTimeTV.setText("");
                startTimeTV.setVisibility(View.GONE);
                deleteStartTime.setVisibility(View.GONE);
                break;
            case R.id.delete_phone_number:
                phoneNumber.setText("");
                phoneNumber.setVisibility(View.GONE);
                deletePhoneNumber.setVisibility(View.GONE);
                break;
            case R.id.delete_price:
                priceTv.setText("");
                priceTv.setVisibility(View.GONE);
                deletePrice.setVisibility(View.GONE);
                break;
            case R.id.post_edit_text:
                postCaption.setFocusableInTouchMode(true);
                break;
            case R.id.locations_icon:
                if (locationModel != null) {
                    MapBottomSheet sheet = new MapBottomSheet(locationModel);
                    sheet.show(getSupportFragmentManager(), sheet.getTag());
                }
                break;
        }
    }

    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int hour, int min) {
        String time = hour + ":" + min;
        startTimeTV.setVisibility(View.VISIBLE);
        startTimeTV.setText(time);
        deleteStartTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        String date = day + " / " + (month + 1) + " / " + year;
        startDateTV.setVisibility(View.VISIBLE);
        startDateTV.setText(date);
        deleteStartDate.setVisibility(View.VISIBLE);
    }

    @Override
    public void applyCarDetails(String text) {
        carDetailsTV.setVisibility(View.VISIBLE);
        carDetailsTV.setText(text);
        deleteCarDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void applyPhoneNumber(String number) {
        phoneNumber.setVisibility(View.VISIBLE);
        phoneNumber.setText(number);
        deletePhoneNumber.setVisibility(View.VISIBLE);
    }

    @Override
    public void applyDurationText(String duration) {
        String text = duration + " hours";
        sessionDurationTV.setText(text);
        sessionDurationTV.setVisibility(View.VISIBLE);
        deleteDuration.setVisibility(View.VISIBLE);
    }

    @Override
    public void applyPriceText(String price) {
        String p = price + " EGP";
        priceTv.setText(p);
        priceTv.setVisibility(View.VISIBLE);
        deletePrice.setVisibility(View.VISIBLE);
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

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "getting your location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Open GPS to get your location", Toast.LENGTH_SHORT).show();
    }
}
