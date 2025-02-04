package com.shahareinisim.tzachiapp;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shahareinisim.tzachiapp.Adapters.ZmanimAdapter;
import com.shahareinisim.tzachiapp.Models.Zman;
import com.shahareinisim.tzachiapp.Utils.HolidaysFinder;
import com.shahareinisim.tzachiapp.Utils.LocationFinderInitializer;
import com.shahareinisim.tzachiapp.Utils.LocationSpinnerInitializer;
import com.shahareinisim.tzachiapp.Utils.SfiratAhomer;
import com.shahareinisim.tzachiapp.databinding.ActivityZmanimBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class ZmanimActivity extends BaseActivity {

    ActivityZmanimBinding binding;
    LocationSpinnerInitializer locationSpinnerInitializer = new LocationSpinnerInitializer();
    LocationFinderInitializer locationFinderInitializer = new LocationFinderInitializer();

    HolidaysFinder holidaysFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityZmanimBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.rvZmanim.setLayoutManager(new LinearLayoutManager(this));

        locationSpinnerInitializer.initialize(this, this::initZmanimList);
        locationFinderInitializer.initialize(this, locationSpinnerInitializer);

        initZmanimList();
    }

    private void initZmanimList() {
        if (binding.rvZmanim.getAdapter() != null) {
            binding.rvZmanim.setAdapter(null);
        }
        holidaysFinder = new HolidaysFinder(this);
        ArrayList<Zman> zmanim = holidaysFinder.getZmanim(Calendar.getInstance());
        binding.rvZmanim.setAdapter(new ZmanimAdapter(zmanim));

        String date = holidaysFinder.getHdf().format(holidaysFinder.getJewishCalendar());
        String day = holidaysFinder.getHdf().formatDayOfWeek(holidaysFinder.getJewishCalendar());
        binding.title.setText(String.format(getString(R.string.zmanim_title), day, date));
        if (holidaysFinder.getJewishCalendar().getDayOfOmer() != -1) {
            binding.specials.setText(SfiratAhomer.getSfiratAhomer(
                    holidaysFinder.getJewishCalendar().getDayOfOmer(), false));
        } else binding.specials.setVisibility(View.GONE);
    }
}