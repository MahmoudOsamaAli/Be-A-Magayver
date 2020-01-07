package com.example.beamagayver.view.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.beamagayver.R;
import com.example.beamagayver.Utilities.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterMethodsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RegisterMethodsFragment";
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
    Handler handler = new Handler();
    Runnable runnableLogIn = new Runnable() {
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
        ButterKnife.bind(this , view);
        init();
    }

    private void init() {
        try {
            methods.setVisibility(View.GONE);
            handler.postDelayed(runnableLogIn, 2500);
            instructorButton.setOnClickListener(this);
            newClientButton.setOnClickListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.instructor_button:
                Utilities.setFragments(getContext() ,new ContractFragment() , Utilities.RIGHT_TO_LEFT);
                break;
            case R.id.new_client_button:
                Utilities.setFragments(getContext() , new SignUpFragment() , Utilities.RIGHT_TO_LEFT);
                break;

        }
    }

}
