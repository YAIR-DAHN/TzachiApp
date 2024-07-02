package com.shahareinisim.tzachiapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.AutoCompleteTextView;

import com.shahareinisim.tzachiapp.Adapters.LocationsAdapter;
import com.shahareinisim.tzachiapp.Models.Location;
import com.shahareinisim.tzachiapp.R;

import java.util.ArrayList;

public class LocationSpinnerInitializer {

    Activity activity;
    AutoCompleteTextView locationSpinner;
    Runnable onLocationChange;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    LocationsAdapter adapter;
    ArrayList<Location> locations;

    public void initialize(Activity activity, Runnable onLocationChange) {
        this.activity = activity;
        this.locationSpinner = activity.findViewById(R.id.location_spinner);
        this.onLocationChange = onLocationChange;
        this.locations = HolidaysFinder.getLocations(activity);
        this.sp = activity.getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(getContext()), Context.MODE_PRIVATE);
        this.editor = sp.edit();

        sp.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            assert key != null;
            if (key.equals("location")) updateCurrentLocation();
        });

        initLocations();
    }

    public void updateCurrentLocation() {
        locationSpinner.setText(locations.get(getSavedLocation()).getLocationName());
        locationSpinner.dismissDropDown();
        adapter.setSelectedPosition(getSavedLocation());
        onLocationChange.run();
    }

    private void initLocations() {
        adapter = new LocationsAdapter(locations, getContext());
        locationSpinner.setAdapter(adapter);

        locationSpinner.setText(locations.get(getSavedLocation()).getLocationName());

        locationSpinner.setOnItemClickListener((parent, view, position, id) -> {
            saveLocation(editor, locations.get(position));
            updateCurrentLocation();
        });
        updateCurrentLocation();
    }

    public static void saveLocation(SharedPreferences.Editor editor, Location location) {
        editor.putString("location", location.getLocationName());
        editor.putFloat("latitude", (float) location.getLatitude());
        editor.putFloat("longitude", (float) location.getLongitude());
        editor.apply();
    }

    private int getSavedLocation() {
        String savedLocationName = sp.getString("location", "Tel Aviv");
        for (Location location : locations) {
            if (location.getLocationName().equals(savedLocationName))
                return locations.indexOf(location);
        }

        return 0;
    }

    public Context getContext() {
        return activity;
    }
}
