package com.example.beamagayver.Utilities;

import com.example.beamagayver.pojo.LocationModel;

public interface DialogListener {
    void applyCarDetails(String brand);

    void applyPhoneNumber(String number);

    void applyDurationText(String duration);

    void applyPriceText(String price);

    void onGetLocationListener(LocationModel model);
}
