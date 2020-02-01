package com.example.beamagayver.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefManager {

    public static final String KEY_HEADS = "pref_heads";
    public static final String KEY_ONE_GROUP = "pref_one_group";
    public static final String KEY_GROUP_RELATION = "pref_group_relation";
    public static final String KEY_NEW_PROBLEM_ORIGIN = "pref_new_problem_origin";
    public static final String KEY_NEW_GCD = "pref_gcd";
    public static final String KEY_OLD_PERSON_SHARE_PERCENT_NUMERATOR = "pref_old_person_share_percent_numerator";
    public static final String KEY_OLD_PERSON_SHARE_PERCENT_DENOMINATOR = "pref_old_person_share_percent_denominator";
    public static final String KEY_NEW_PERSON_SHARE_PERCENT_NUMERATOR = "pref_new_person_share_percent_numerator";
    public static final String KEY_NEW_PERSON_SHARE_PERCENT_DENOMINATOR = "pref_new_person_share_percent_denominator";

    private SharedPreferences prefs;

    public PrefManager(Context c) {
        prefs = PreferenceManager.getDefaultSharedPreferences(c);
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public void saveLong(String key, long value) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public void saveBoolean(String key, boolean b) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(key, b);
        edit.apply();
    }

    public void saveInt(String key, int b) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(key, b);
        edit.apply();
    }

    public String readString(String key) {
        return prefs.getString(key, "");
    }

    public boolean readBoolean(String key) {
        return prefs.getBoolean(key, false);
    }

    public int readInt(String key) {
        return prefs.getInt(key, 0);
    }
}
