package com.example.beamagayver.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beamagayver.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.image_welcome)
    ImageView welcomeImg;
    @BindView(R.id.instructor_button)
    Button button;
    @BindView(R.id.new_client_button)
    Button button2;
    @BindView(R.id.linear1)
    LinearLayout linear;
    @BindView(R.id.admin_button)
    Button button3;
    Handler handler = new Handler();
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            welcomeImg.setVisibility(View.GONE);
            linear.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler.postDelayed(runnable1, 4000);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        try{
            button.setOnClickListener(this);
            button2.setOnClickListener(this);
            button3.setOnClickListener(this);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.instructor_button:
                startActivity(new Intent(WelcomeActivity.this , InstructorContractActivity.class));
                break;
            case R.id.new_client_button:
            case R.id.admin_button:
                startActivity(new Intent(WelcomeActivity.this , SignUpActivity.class));
                break;
        }

    }
}
