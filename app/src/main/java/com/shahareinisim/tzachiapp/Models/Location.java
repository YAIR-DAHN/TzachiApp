package com.shahareinisim.tzachiapp.Models;

public class Location {

    String locationName;
    double latitude;
    double longitude;

    public Location(String locationName, double latitude, double longitude) {
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
}
