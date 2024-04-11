package com.shahareinisim.tzachiapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.AutoCompleteTextView;

import com.shahareinisim.tzachiapp.Adapters.LocationsAdapter;
import com.shahareinisim.tzachiapp.Models.Location;

import java.util.ArrayList;

public class LocationSpinnerInitializer {

    Context context;
    AutoCompleteTextView locationSpinner;
    Runnable onLocationChange;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    LocationsAdapter adapter;
    ArrayList<Location> locations;

    public void initialize(AutoCompleteTextView locationSpinner, Context context, Runnable onLocationChange) {
        this.context = context;
        this.locationSpinner = locationSpinner;
        this.onLocationChange = onLocationChange;
        this.locations = HolidaysFinder.getLocations(context);
        this.sp = context.getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(getContext()), Context.MODE_PRIVATE);
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
        return context;
    }
}
