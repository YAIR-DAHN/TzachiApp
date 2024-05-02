package com.shahareinisim.tzachiapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shahareinisim.tzachiapp.Models.Location;
import com.shahareinisim.tzachiapp.R;

import java.util.ArrayList;

public class LocationsAdapter extends ArrayAdapter<Location> {

    Context context;
    ArrayList<Location> locations;
    int selectedPosition;

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

            if (textView != null) textView.setText(item.getLocationName());

            ImageView selectedLocation = convertView.findViewById(R.id.selected_location);
            if (position == selectedPosition) selectedLocation.setVisibility(View.VISIBLE);
            else selectedLocation.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    @NonNull
    public Context getContext() {
        return context;
    }
}
