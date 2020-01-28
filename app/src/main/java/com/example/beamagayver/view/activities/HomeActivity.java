package com.example.beamagayver.view.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.beamagayver.R;
import com.example.beamagayver.view.adapters.MyPagerAdapter;
import com.example.beamagayver.view.fragments.ActivitiesFragment;
import com.example.beamagayver.view.fragments.SessionsFragment;
import com.example.beamagayver.view.fragments.ProfileFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity  {


    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        try {
            adapter = new MyPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
