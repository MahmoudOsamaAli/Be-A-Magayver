package com.example.beamagayver.view.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.beamagayver.view.fragments.ActivitiesFragment;
import com.example.beamagayver.view.fragments.ProfileFragment;
import com.example.beamagayver.view.fragments.SessionsFragment;

public class MyPagerAdapter extends FragmentStatePagerAdapter {


    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SessionsFragment();
            case 1:
                return new ActivitiesFragment();
            case 2:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Sessions";
            case 1:
                return "Activities";
            case 2:
                return "Profile";
            default:
                return null;
        }
    }
}
