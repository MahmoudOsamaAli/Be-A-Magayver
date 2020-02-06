package com.example.beamagayver.Utilities;

import android.location.Location;

public interface onGetLocation{
    void onLocationChanged(Location location);
    void onProviderEnabled();
    void onProviderDisabled();
}
