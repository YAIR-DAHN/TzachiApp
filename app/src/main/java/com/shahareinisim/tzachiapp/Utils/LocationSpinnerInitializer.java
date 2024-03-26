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
    ArrayList<Location> locations;

    public void initialize(AutoCompleteTextView locationSpinner, Context context, Runnable onLocationChange) {
        this.context = context;
        this.locationSpinner = locationSpinner;
        this.onLocationChange = onLocationChange;
        this.locations = HolidaysFinder.getLocations();
        this.sp = context.getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(getContext()), Context.MODE_PRIVATE);
        this.editor = sp.edit();

        initLocations();
    }

    private void initLocations() {
        LocationsAdapter adapter = new LocationsAdapter(locations, getContext());
        locationSpinner.setAdapter(adapter);

        locationSpinner.setText(locations.get(getSavedLocation()).getLocationString(getContext()));

        locationSpinner.setOnItemClickListener((parent, view, position, id) -> {
            editor.putString("location", locations.get(position).getLocationName());
            editor.apply();
            locationSpinner.setText(locations.get(position).getLocationString(getContext()));
            onLocationChange.run();
        });
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
