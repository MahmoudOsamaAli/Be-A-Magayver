package com.example.beamagayver.view.activities;

import android.os.Bundle;
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
import com.example.beamagayver.Utilities.ChangeFragments;
import com.example.beamagayver.view.fragments.LoginFragment.RegisterMethodsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContractFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ContractFragment";
    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";
    @BindView(R.id.header_text)
    TextView headerText;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.qualifications_tv)
    TextView qualificationsTv;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.ok_button)
    Button okButton;
    @BindView(R.id.not_okey_button)
    Button notOkeyButton;
    @BindView(R.id.linear2)
    LinearLayout linear2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contract, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        okButton.setOnClickListener(this);
        notOkeyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ChangeFragments.setFragments(getActivity() , new RegisterMethodsFragment(), ChangeFragments.RIGHT_TO_LEFT);
    }
}
