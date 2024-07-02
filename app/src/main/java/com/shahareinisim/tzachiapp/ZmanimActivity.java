package com.shahareinisim.tzachiapp;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.shahareinisim.tzachiapp.Adapters.ZmanimAdapter;
import com.shahareinisim.tzachiapp.Models.Zman;
import com.shahareinisim.tzachiapp.Utils.HolidaysFinder;
import com.shahareinisim.tzachiapp.Utils.LocationFinderInitializer;
import com.shahareinisim.tzachiapp.Utils.LocationSpinnerInitializer;
import com.shahareinisim.tzachiapp.Utils.SfiratAhomer;

import java.util.ArrayList;
import java.util.Calendar;

public class ZmanimActivity extends BaseActivity {

    RecyclerView rvZmanim;
    LocationSpinnerInitializer locationSpinnerInitializer = new LocationSpinnerInitializer();
    LocationFinderInitializer locationFinderInitializer = new LocationFinderInitializer();

    HolidaysFinder holidaysFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_zmanim);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvZmanim = findViewById(R.id.rv_zmanim);
        rvZmanim.setLayoutManager(new LinearLayoutManager(this));

        locationSpinnerInitializer.initialize(this, this::initZmanimList);
        locationFinderInitializer.initialize(this, locationSpinnerInitializer);

        initZmanimList();
    }

    private void initZmanimList() {
        if (rvZmanim != null) {
            rvZmanim.setAdapter(null);
        }
        holidaysFinder = new HolidaysFinder(this);
        ArrayList<Zman> zmanim = holidaysFinder.getZmanim(Calendar.getInstance());
        rvZmanim.setAdapter(new ZmanimAdapter(zmanim));

        String date = holidaysFinder.getHdf().format(holidaysFinder.getJewishCalendar());
        String day = holidaysFinder.getHdf().formatDayOfWeek(holidaysFinder.getJewishCalendar());
        ((TextView) findViewById(R.id.title)).setText(String.format(getString(R.string.zmanim_title), day, date));
        if (holidaysFinder.getJewishCalendar().getDayOfOmer() != -1) {
            ((TextView) findViewById(R.id.specials)).setText(SfiratAhomer
                    .getSfiratAhomer(holidaysFinder.getJewishCalendar().getDayOfOmer(), false));
        } else findViewById(R.id.specials).setVisibility(View.GONE);
    }
}