package com.shahareinisim.tzachiapp.Utils;

import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.shahareinisim.tzachiapp.BaseActivity;
import com.shahareinisim.tzachiapp.Models.Location;
import com.shahareinisim.tzachiapp.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationFinderInitializer {

    private AppCompatActivity activity;
    private LocationSpinnerInitializer locationSpinnerInitializer;

    MaterialButton locationFinder;
    FusedLocationProviderClient fusedLocationClient;
    ActivityResultLauncher<String[]> locationPermissionRequest;
    SharedPreferences preferences;

    public static final int REQUEST_CHECK_SETTINGS = 199;

    public void initialize(AppCompatActivity activity, LocationSpinnerInitializer lsi) {
        this.activity = activity;
        this.locationSpinnerInitializer = lsi;
        this.preferences = ((BaseActivity) activity).preferences;

        locationFinder = activity.findViewById(R.id.location_finder);
        locationFinder.setOnClickListener(v -> checkLocationSettings(false));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        registerLocationPermissionRequest();
        checkLocationSettings(true);

    }

    private void getCurrentLocation() {

        if (locationPermissionRequest == null) registerLocationPermissionRequest();

        locationPermissionRequest.launch(new String[] {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    @SuppressLint("MissingPermission")
    public void registerLocationPermissionRequest() {
        locationPermissionRequest = activity.registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {

                    Boolean fineLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION,false);

                    fineLocationGranted = fineLocationGranted != null && fineLocationGranted;
                    coarseLocationGranted = coarseLocationGranted != null && coarseLocationGranted;

                    if (fineLocationGranted || coarseLocationGranted) {

                        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                            if (location == null) return;
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            String locationString = "";
                            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                assert addresses != null;
                                if (!addresses.isEmpty()) {
                                    for (Address address : addresses) {
                                        locationString = address.getAddressLine(0);
                                        locationString = locationString.substring(locationString.indexOf(",")+2);
                                    }
                                }
                                if (!locationString.isEmpty()) {
                                    LocationSpinnerInitializer.saveLocation(preferences.edit(), new Location(locationString.trim(), latitude, longitude));
                                    locationSpinnerInitializer.updateCurrentLocation();
                                }
                            } catch (IOException e) {
                                locationString = getString(R.string.location_unknown);
                            }

                            Log.d("TfilonActivity", "location: " + locationString);
                        });
                    }
                }
        );
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void checkLocationSettings(boolean checkSettingsOnly) {

        LocationRequest.Builder locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();

        locationSettingsRequestBuilder.addLocationRequest(locationRequest.build());
        locationSettingsRequestBuilder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());

        task.addOnSuccessListener(activity, locationSettingsResponse -> {
            locationFinder.setIcon(getDrawable(R.drawable.location));
            if (checkSettingsOnly) return;

            getCurrentLocation();
        });

        task.addOnFailureListener(activity, e -> {
            locationFinder.setIcon(getDrawable(R.drawable.location_disabled));
            if (checkSettingsOnly) return;

            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getDrawable(int id) {
        return activity.getDrawable(id);
    }

    private String getString(int id) {
        return activity.getString(id);
    }
}
