package com.shahareinisim.tzachiapp.Fragments;

import static com.shahareinisim.tzachiapp.Models.TfilahPart.Key.HANUKKAH;
import static com.shahareinisim.tzachiapp.Models.TfilahPart.Key.HOLIDAY;
import static com.shahareinisim.tzachiapp.Models.TfilahPart.Key.PURIM;
import static com.shahareinisim.tzachiapp.Models.TfilahPart.Key.ROSH_CHODESH;
import static com.shahareinisim.tzachiapp.Models.TfilahPart.Key.TACHNUN;
import static com.shahareinisim.tzachiapp.Models.TfilahPart.Key.YAHALEH_VEYAVO;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shahareinisim.tzachiapp.Adapters.TfilahAdapter;
import com.shahareinisim.tzachiapp.Adapters.TitleAdapter;
import com.shahareinisim.tzachiapp.Models.TfilahPart;
import com.shahareinisim.tzachiapp.Models.TfilahTitlePart;
import com.shahareinisim.tzachiapp.R;
import com.shahareinisim.tzachiapp.TfilonActivity;
import com.shahareinisim.tzachiapp.Utils.Animations;
import com.shahareinisim.tzachiapp.Utils.DataManager;
import com.shahareinisim.tzachiapp.Utils.HolidaysFinder;
import com.shahareinisim.tzachiapp.Utils.ShachritUtils;
import com.shahareinisim.tzachiapp.Views.PopupNavigator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class TfilahFragment extends Fragment {

    public enum Tfilah {SHACHRIT, MINCHA, HARVIT, BIRCAT_HAMAZON, BIRCAT_HALEVANA, TFILAT_HADEREH, KRIAT_SHEMA, BIRCAT_MEN_SHALOSH, ASHER_YATZAR, TRUMOT_VMEASROT, PEREK_SHIRA, TIKUN_HAKLALI}

    Tfilah tfilah;
    FloatingActionButton navigator;
    RecyclerView rvTfilah;
    TextView title;
    PopupNavigator popupNav;

    TfilahAdapter tfilahAdapter;

    TfilahPart.Key[] timesKeys = {HOLIDAY, YAHALEH_VEYAVO, ROSH_CHODESH, TACHNUN, HANUKKAH, PURIM};

    public TfilahFragment() {}

    public TfilahFragment(Tfilah tfilah) {
        this.tfilah = tfilah;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View parent = inflater.inflate(R.layout.fragment_tfilah, container, false);
        parent.setFocusable(true);

        CoordinatorLayout topBar = parent.findViewById(R.id.top_bar);

        rvTfilah = parent.findViewById(R.id.rv_tfilah);
        rvTfilah.setLayoutManager(new LinearLayoutManager(requireContext()));

        rvTfilah.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 10) {
                    if (topBar.getVisibility() == View.VISIBLE) Animations.hide(topBar);
                } else if (dy < -10) {
                    if (topBar.getVisibility() == View.GONE) Animations.show(topBar);
                }
            }
        });

        navigator = parent.findViewById(R.id.navigator);

        title = parent.findViewById(R.id.title);

        int tfilahFileRes = 0;

        switch (tfilah) {
            case SHACHRIT:
                tfilahFileRes = R.raw.shachrit;
                title.setText(R.string.shachrit);
                break;
            case MINCHA:
                tfilahFileRes = R.raw.mincha;
                title.setText(R.string.mincha);
                break;
            case HARVIT:
                tfilahFileRes = R.raw.harvit;
                title.setText(R.string.harvit);
                break;
            case BIRCAT_HAMAZON:
                tfilahFileRes = R.raw.bircat_hamazon;
                title.setText(R.string.bircat_hamazon);
                break;
            case BIRCAT_HALEVANA:
                tfilahFileRes = R.raw.bircat_halevana;
                title.setText(R.string.bircat_halevana);
                break;
            case TFILAT_HADEREH:
                tfilahFileRes = R.raw.tfilat_hadereh;
                title.setText(R.string.tfilat_hadereh);
                break;
            case KRIAT_SHEMA:
                tfilahFileRes = R.raw.kriat_shmaa;
                title.setText(R.string.kriat_shema);
                break;
            case BIRCAT_MEN_SHALOSH:
                tfilahFileRes = R.raw.birkat_men_shalosh;
                title.setText(R.string.birkat_men_shalosh);
                break;
            case ASHER_YATZAR:
                tfilahFileRes = R.raw.asher_yatzar;
                title.setText(R.string.asher_yatzar);
                break;
            case TRUMOT_VMEASROT:
                tfilahFileRes = R.raw.trumot_vmeasrot;
                title.setText(R.string.trumot_vmeasrot);
                break;
            case PEREK_SHIRA:
                tfilahFileRes = R.raw.perek_shira;
                title.setText(R.string.perek_shira);
                break;
            case TIKUN_HAKLALI:
                tfilahFileRes = R.raw.tikun_haklali;
                title.setText(R.string.tikun_haklali);
                break;
        }

        DataManager dataManager = new DataManager(requireContext());
        dataManager.open();
        dataManager.openedTfilah(tfilah.toString());

        if (dataManager.getMostUsedTfilah().equals(tfilah.toString())) {
            ((TfilonActivity) requireActivity()).addMostUsedTfilah(tfilah, title.getText().toString());
        }

        dataManager.close();

        title.setSelected(true);

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(requireActivity().getResources().openRawResource(tfilahFileRes));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            rvTfilah.setAdapter(initTfilahAdapter(bufferedReader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return parent;
    }

    public TfilahAdapter initTfilahAdapter(BufferedReader bufferedReader) throws IOException {
        ArrayList<TfilahPart> parts = new ArrayList<>();
        ArrayList<TfilahTitlePart> titleParts = new ArrayList<>();
        HolidaysFinder holidaysFinder = new HolidaysFinder();
        Log.d("##### initTfilahAdapter #####", "is holiday: " + (holidaysFinder.isHoliday() ? "Yes" : "No"));

        String part;
        TfilahPart.Key currentKey = null;
        while ((part = bufferedReader.readLine()) != null) {
            TfilahPart tfilahPart = new TfilahPart(part);

            if (tfilahPart.getKeys().contains(TfilahPart.Key.SOD)) {
                tfilahPart.setPart(ShachritUtils.getSongOfCurrentDay(requireActivity().getResources().openRawResource(R.raw.song_of_day)));
            } else {

                for (TfilahPart.Key key : timesKeys) {
                    if (tfilahPart.getKeys().contains(key)) {
                        if (currentKey == key) currentKey = null;
                        else currentKey = key;
                    }
                }

                if (currentKey != null) {
                    switch (currentKey) {
                        case HANUKKAH:
                            if (!holidaysFinder.jewishCalendar.isChanukah()) continue;
                            break;
                        case PURIM:
                            if (!holidaysFinder.jewishCalendar.isPurim()) continue;
                            break;
                        case HOLIDAY:
                            if (!holidaysFinder.isHoliday()) continue;
                            break;
                        case YAHALEH_VEYAVO:
                        case ROSH_CHODESH:
                            if (!holidaysFinder.jewishCalendar.isRoshChodesh()) continue;
                            break;
                        case TACHNUN:
                            if (holidaysFinder.isNoTachnunRecited()) continue;
                    }
                }

            }

            if (tfilahPart.isTitle()) titleParts.add(new TfilahTitlePart(parts.size(), part));

            if (tfilahPart.isEmpty()) {
                //if crashing here delete empty line at the top of current tfilah
                if (parts.get(parts.size()-1).isEmpty()) continue;
            }
            parts.add(tfilahPart);
        }

        tfilahAdapter = new TfilahAdapter(parts, getTextSize());

        if (popupNav == null) {
            popupNav = new PopupNavigator(requireContext());

            popupNav.onShortcutClick(view -> ((TfilonActivity) requireActivity())
                    .createShortcut(tfilah, title.getText().toString()));

            popupNav.setBiggerTextClickListener(() -> {
                Log.d("##### onclickBig #####", "textSize = " + getTextSize());
                int textSize = getTextSize();
                if (textSize != TfilahAdapter.textTypes.length-1) textSize++;

                notifyTextSizeChanged(textSize);
                popupNav.sizeButtonsEnabled(getTextSize());
            });

            popupNav.setSmallerTextClickListener(() -> {
                Log.d("##### onClickSmall #####", "textSize = " + getTextSize());
                int textSize = getTextSize();
                if (textSize != 0) textSize--;

                notifyTextSizeChanged(textSize);
                popupNav.sizeButtonsEnabled(getTextSize());
            });

            TitleAdapter titleAdapter = new TitleAdapter((TfilonActivity) requireActivity(), titleParts);
            titleAdapter.setPostClickListener(position -> {
                rvTfilah.post(() -> ((LinearLayoutManager) Objects.requireNonNull(
                        rvTfilah.getLayoutManager())).scrollToPositionWithOffset(position,0));
                popupNav.dismiss();
            });

            popupNav.setAdapter(titleAdapter);
            popupNav.sizeButtonsEnabled(getTextSize());
        }

        navigator.setOnClickListener(view -> popupNav.showAsDropDown(navigator, 0, 10));

        return tfilahAdapter;
    }

    public int getTextSize() {
        return ((TfilonActivity) requireActivity()).getTextSize();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyTextSizeChanged(int textSize) {
        ((TfilonActivity) requireActivity()).preferences.edit().putInt("textSize", textSize).apply();
        tfilahAdapter.textSize = textSize;
        tfilahAdapter.notifyDataSetChanged();
        Log.d("##### notifyTextSizeChanged #####", "textSize = " + textSize);
    }

    @Override
    public void onDestroy() {
        if (popupNav != null) popupNav.dismiss();
        super.onDestroy();
    }
}
