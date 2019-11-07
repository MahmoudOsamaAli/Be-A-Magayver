package com.example.beamagayver.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beamagayver.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstructorContractActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ok_button)
    Button okButton;
    @BindView(R.id.not_okey_button)
    Button notOkeyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_contract);
        ButterKnife.bind(this);
        okButton.setOnClickListener(this);
        notOkeyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(InstructorContractActivity.this , SignUpActivity.class));
    }
}
