package com.example.beamagayver.Utilities;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.beamagayver.R;
import com.example.beamagayver.view.activities.WelcomeActivity;

public class Utilities {

    public static Fragment currFragment;
    private static final String TAG = "Utilities";
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    public static final String LEFT_TO_RIGHT = "LEFT_TO_RIGHT";
    public static final String RIGHT_TO_LEFT = "RIGHT_TO_LEFT";
    public static final String OUT_UP = "OUT_UP";

    public static void setFragments(Context context , Fragment fragment ,String state){
        try {
            currFragment = fragment;
            Log.i(TAG, "setFragments: current fragment = " + currFragment.getClass().getCanonicalName());
            FragmentTransaction transaction = ((WelcomeActivity)context).getSupportFragmentManager().beginTransaction();
            if (state.equals(LEFT_TO_RIGHT))
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            else if (state.equals(RIGHT_TO_LEFT))
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            else if(state.equals(OUT_UP))
                transaction.setCustomAnimations(R.anim.slide_in_down , R.anim.slide_out_up);

            transaction.replace(R.id.fragment_container, fragment, TAG_FRAGMENT);
            transaction.addToBackStack(null);
            transaction.commit();
            Log.i(TAG, "setFragments: fragment tag = " + TAG_FRAGMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
