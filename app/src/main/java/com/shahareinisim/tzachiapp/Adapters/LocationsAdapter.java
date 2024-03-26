package com.shahareinisim.tzachiapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shahareinisim.tzachiapp.Models.Location;
import com.shahareinisim.tzachiapp.R;

import java.util.ArrayList;

public class LocationsAdapter extends ArrayAdapter<Location> {

    Context context;
    ArrayList<Location> locations;

    public LocationsAdapter(ArrayList<Location> locations, Context context) {
        super(context, R.layout.item_location, locations);
        this.context = context;
        this.locations = locations;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        if (locations != null) return locations.size();
        return 0;
    }

    @Override
    public Location getItem(int position) {
        return locations.get(position);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_location, parent, false);
        }

        Location item = locations.get(position);
        if (item != null) {
            TextView textView = convertView.findViewById(R.id.location_name);
            if (textView != null)
                textView.setText(item.getLocationString(getContext()));
        }

        return convertView;
    }

    @NonNull
    public Context getContext() {
        return context;
    }
}
