package com.shahareinisim.tzachiapp.Models;

import android.content.Context;

import com.shahareinisim.tzachiapp.R;

public class Location {

    String locationName;
    double latitude;
    double longitude;
    double elevation;

    public Location(String locationName, double latitude, double longitude, double elevation) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getElevation() {
        return elevation;
    }

}
