package com.shahareinisim.tzachiapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.shahareinisim.tzachiapp.Adapters.TfilahAdapter;
import com.shahareinisim.tzachiapp.Adapters.TitleAdapter;
import com.shahareinisim.tzachiapp.Models.PartIndexes;
import com.shahareinisim.tzachiapp.Models.TfilahPart;
import com.shahareinisim.tzachiapp.Models.TfilahTitlePart;
import com.shahareinisim.tzachiapp.R;
import com.shahareinisim.tzachiapp.TfilonActivity;
import com.shahareinisim.tzachiapp.Utils.Animations;
import com.shahareinisim.tzachiapp.Utils.DataManager;
import com.shahareinisim.tzachiapp.Utils.HolidaysFinder;
import com.shahareinisim.tzachiapp.Utils.SfiratAhomer;
import com.shahareinisim.tzachiapp.Utils.ShachritUtils;
import com.shahareinisim.tzachiapp.Views.PopupNavigator;
import com.shahareinisim.tzachiapp.databinding.FragmentTfilahBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class TfilahFragment extends Fragment {

    public enum Tfilah {SHACHRIT, MINCHA, HARVIT, BIRCAT_HAMAZON, BIRCAT_HALEVANA, TFILAT_HADEREH,
        KRIAT_SHEMA, BIRCAT_MEN_SHALOSH, ASHER_YATZAR, TRUMOT_VMEASROT, PEREK_SHIRA, TIKUN_HAKLALI, MEGILAT_ESTHER}

    FragmentTfilahBinding binding;
    Tfilah tfilah;
    PopupNavigator popupNav;
    Handler indicatorHandler = new Handler();

    TfilahAdapter tfilahAdapter;
    ArrayList<TfilahTitlePart> titleParts;

    public TfilahFragment() {}

    public TfilahFragment(Tfilah tfilah) {
        this.tfilah = tfilah;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTfilahBinding.inflate(inflater, container, false);
        binding.getRoot().setFocusable(true);

        binding.rvTfilah.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.rvTfilah.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (titleParts != null) {
                    for (int i = 0; i < titleParts.size(); i++) {
                        int currentPosition = ((LinearLayoutManager) Objects.requireNonNull(binding.rvTfilah.getLayoutManager())).findFirstVisibleItemPosition();
                        if (currentPosition == 0) {
                            binding.titleIndicator.setVisibility(View.GONE);
                            return;
                        }
                        boolean isBeforeNext;
                        if (i == titleParts.size()-1) isBeforeNext = true;
                        else isBeforeNext = currentPosition < titleParts.get(i+1).getPosition();
                        if (currentPosition >= titleParts.get(i).getPosition() && isBeforeNext) {
                            binding.titleIndicator.setText(titleParts.get(i).getTitle());
                            int finalI = i;
                            binding.titleIndicator.setOnClickListener(v -> {
                                Animations.show(binding.topBar);
                                binding.rvTfilah.post(() -> ((LinearLayoutManager) Objects.requireNonNull(
                                        binding.rvTfilah.getLayoutManager())).scrollToPositionWithOffset(titleParts.get(finalI).getPosition(),0));
                                indicatorHandler.removeCallbacksAndMessages(null);
                                indicatorHandler.postDelayed(() -> binding.titleIndicator.setVisibility(View.GONE), 1000);
                            });
                            binding.titleIndicator.setVisibility(View.VISIBLE);
                            indicatorHandler.removeCallbacksAndMessages(null);
                            indicatorHandler.postDelayed(() -> binding.titleIndicator.setVisibility(View.GONE), 3000);
                        }
                    }
                }

                if (dy > 10) {
                    if (binding.topBar.getVisibility() == View.VISIBLE) Animations.hide(binding.topBar);
                } else if (dy < -10) {
                    if (binding.topBar.getVisibility() == View.GONE) Animations.show(binding.topBar);
                }
            }
        });

        int tfilahFileRes = 0;

        switch (tfilah) {
            case SHACHRIT:
                tfilahFileRes = R.raw.shachrit;
                binding.title.setText(R.string.shachrit);
                break;
            case MINCHA:
                tfilahFileRes = R.raw.mincha;
                binding.title.setText(R.string.mincha);
                break;
            case HARVIT:
                tfilahFileRes = R.raw.harvit;
                binding.title.setText(R.string.harvit);
                break;
            case BIRCAT_HAMAZON:
                tfilahFileRes = R.raw.bircat_hamazon;
                binding.title.setText(R.string.bircat_hamazon);
                break;
            case BIRCAT_HALEVANA:
                tfilahFileRes = R.raw.bircat_halevana;
                binding.title.setText(R.string.bircat_halevana);
                break;
            case TFILAT_HADEREH:
                tfilahFileRes = R.raw.tfilat_hadereh;
                binding.title.setText(R.string.tfilat_hadereh);
                break;
            case KRIAT_SHEMA:
                tfilahFileRes = R.raw.kriat_shmaa;
                binding.title.setText(R.string.kriat_shema);
                break;
            case BIRCAT_MEN_SHALOSH:
                tfilahFileRes = R.raw.birkat_men_shalosh;
                binding.title.setText(R.string.bircat_mehein_shalosh);
                break;
            case ASHER_YATZAR:
                tfilahFileRes = R.raw.asher_yatzar;
                binding.title.setText(R.string.asher_yatzar);
                break;
            case TRUMOT_VMEASROT:
                tfilahFileRes = R.raw.trumot_vmeasrot;
                binding.title.setText(R.string.trumot_vmeasrot);
                break;
            case PEREK_SHIRA:
                tfilahFileRes = R.raw.perek_shira;
                binding.title.setText(R.string.perek_shira);
                break;
            case TIKUN_HAKLALI:
                tfilahFileRes = R.raw.tikun_haklali;
                binding.title.setText(R.string.tikun_haklali);
                break;
            case MEGILAT_ESTHER:
                tfilahFileRes = R.raw.megilat_esther;
                binding.title.setText(R.string.megilat_esther);
                break;
        }

        DataManager dataManager = new DataManager(requireContext());
        dataManager.open();
        dataManager.openedTfilah(tfilah.toString());

        if (dataManager.getMostUsedTfilah().equals(tfilah.toString())) {
            ((TfilonActivity) requireActivity()).addMostUsedTfilah(tfilah, binding.title.getText().toString());
        }

        dataManager.close();

        binding.title.setSelected(true);

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(requireActivity().getResources().openRawResource(tfilahFileRes));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            binding.rvTfilah.setAdapter(initTfilahAdapter(bufferedReader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return binding.getRoot();
    }

    @SuppressLint({"ResourceType"})
    public TfilahAdapter initTfilahAdapter(BufferedReader bufferedReader) throws IOException {
        titleParts = new ArrayList<>();
        ArrayList<TfilahPart> parts = new ArrayList<>();
        HolidaysFinder holidaysFinder = new HolidaysFinder(requireContext());
        Log.d("##### initTfilahAdapter #####", "is holiday: " + (holidaysFinder.isHoliday() ? "Yes" : "No"));

        String str;
        StringBuilder stringBuilder = new StringBuilder();
        while ((str = bufferedReader.readLine()) != null) stringBuilder.append(str).append("\n");

        if (tfilah.equals(Tfilah.SHACHRIT)) {
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[sod]");
            stringBuilder = new StringBuilder(
                    stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()),
                            ShachritUtils.getSongOfCurrentDay(requireActivity().getResources().openRawResource(R.raw.song_of_day)))
            );
            if (holidaysFinder.isPurim()) {
                indexes = new PartIndexes(stringBuilder.toString(), "[megilat ester]");
                stringBuilder = new StringBuilder(
                        stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()),
                                ShachritUtils.getMegilatEster(requireActivity().getResources().openRawResource(R.raw.megilat_esther)))
                );
            }
        }
        if (tfilah.equals(Tfilah.HARVIT)) {
            if (holidaysFinder.getJewishCalendar().getDayOfOmer() == -1) {
                PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[sfiratAhomer]");
                stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
            } else {
                PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[omer]");
                stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()),
                        SfiratAhomer.getSfiratAhomer(holidaysFinder.getJewishCalendar().getDayOfOmer())));
            }
        }

        if (!holidaysFinder.getJewishCalendar().isChanukah()) {
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[c]");
            stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
        }
        if (!holidaysFinder.getJewishCalendar().isCholHamoedSuccos()) {
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[succot]");
            stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
        }
        if (!holidaysFinder.isPurim()) {
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[p]");
            stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
        }
        if (!holidaysFinder.getJewishCalendar().isCholHamoedPesach()) {
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[pessach]");
            stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
        }
        if (!holidaysFinder.getJewishCalendar().isRoshChodesh()) {
            while (stringBuilder.toString().contains("[rc]")) {
                PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[rc]");
                stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
            }
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[halel]");
            stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
        }
        if (holidaysFinder.isNoTachnunRecited()) {
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[tachnun]");
            stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
        }
        if (!holidaysFinder.getTefilahRules().isYaalehVeyavoRecited(holidaysFinder.getJewishCalendar())) {
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[yv]");
            stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
        }
        if (!holidaysFinder.getTefilahRules().isMoridHatalRecited(holidaysFinder.getJewishCalendar())) {
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[summer1]");
            stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
        }
        if (!holidaysFinder.getTefilahRules().isMashivHaruachRecited(holidaysFinder.getJewishCalendar())) {
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[winter1]");
            stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
        }
        if (!holidaysFinder.getTefilahRules().isVeseinBerachaRecited(holidaysFinder.getJewishCalendar())) {
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[summer2]");
            stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
        }
        if (!holidaysFinder.getTefilahRules().isVeseinTalUmatarRecited(holidaysFinder.getJewishCalendar())) {
            PartIndexes indexes = new PartIndexes(stringBuilder.toString(), "[winter2]");
            stringBuilder = new StringBuilder(stringBuilder.toString().replace(stringBuilder.substring(indexes.getStartIndex(), indexes.getEndIndex()), ""));
        }

        // iterate over every line in stringBuilder
        for (String part : stringBuilder.toString().split("\n")) {
            TfilahPart tfilahPart = new TfilahPart(part);

            if (tfilahPart.isTitle()) titleParts.add(new TfilahTitlePart(parts.size(), part));

            if (tfilahPart.isEmpty()) {
                //if crashing here delete empty line at the top of current tfilah
                if (!parts.isEmpty() && parts.get(parts.size() - 1).isEmpty()) continue;
            }
            parts.add(tfilahPart);
        }

        Log.d("##### Tfilah Adapter #####", "font: " + getFont());

        tfilahAdapter = new TfilahAdapter(parts, getTextSize(), getFont(), isAlignedBothSides());

        if (popupNav == null) {
            popupNav = new PopupNavigator(requireContext());

            int[] fonts = {R.font.sileotsr, R.font.eft_classic, R.font.californian};

            for (int font : fonts) {
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                MaterialButton button = (MaterialButton) inflater.inflate(
                        R.layout.button_material_toogle, popupNav.getFontChooser(), false);
                button.setId(font);
                button.setTypeface(ResourcesCompat.getFont(requireContext(), font));
                button.setOnClickListener(view -> {
                    notifyTextFontChanged(font);
                    button.setChecked(true);
                });
                popupNav.getFontChooser().addView(button);
            }

            popupNav.getFontChooser().check(getFont());

            popupNav.onShortcutClick(view -> ((TfilonActivity) requireActivity())
                    .createShortcut(tfilah, binding.title.getText().toString()));

            popupNav.setBiggerTextClickListener(() -> {
                int textSize = getTextSize();
                if (textSize != TfilahAdapter.textTypes.length-1) textSize++;

                notifyTextSizeChanged(textSize);
            });

            popupNav.setSmallerTextClickListener(() -> {
                int textSize = getTextSize();
                if (textSize != 0) textSize--;

                notifyTextSizeChanged(textSize);
            });

            popupNav.setAlignJustifyClickListener(() -> notifyAlignmentChanged(true));

            popupNav.setAlignRightClickListener(() -> {
                notifyAlignmentChanged(false);
                Log.d("##### onClickAlignBothSides #####", "justifyAlignment = " + isAlignedBothSides());
            });

            popupNav.setAlignmentButtonSelected(isAlignedBothSides());

            TitleAdapter titleAdapter = new TitleAdapter((TfilonActivity) requireActivity(), titleParts);
            titleAdapter.setPostClickListener(position -> {
                binding.rvTfilah.post(() -> ((LinearLayoutManager) Objects.requireNonNull(
                        binding.rvTfilah.getLayoutManager())).scrollToPositionWithOffset(position,0));
                popupNav.dismiss();
            });

            popupNav.setAdapter(titleAdapter);
            popupNav.sizeButtonsEnabled(getTextSize());
        }

        binding.navigator.setOnClickListener(view -> popupNav.showAsDropDown(binding.navigator, 0, 10));

        tfilahAdapter.setOnItemClickListener(() -> {
            if (binding.topBar.getVisibility() == View.GONE) Animations.show(binding.topBar);
        });
        return tfilahAdapter;
    }

    public int getTextSize() {
        return ((TfilonActivity) requireActivity()).getTextSize();
    }

    public int getFont() {
        return ((TfilonActivity) requireActivity()).getFont();
    }

    public boolean isAlignedBothSides() {
        return ((TfilonActivity) requireActivity()).isAlienedBothSides();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyTextSizeChanged(int textSize) {
        ((TfilonActivity) requireActivity()).preferences.edit().putInt("textSize", textSize).apply();
        tfilahAdapter.textSize = textSize;
        tfilahAdapter.notifyDataSetChanged();
        popupNav.sizeButtonsEnabled(textSize);
        Log.d("##### notifyTextSizeChanged #####", "textSize = " + textSize);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyTextFontChanged(int font) {
        ((TfilonActivity) requireActivity()).preferences.edit().putInt("font", font).apply();
        tfilahAdapter.font = font;
        tfilahAdapter.notifyDataSetChanged();
        Log.d("##### notifyTextFontChanged #####", "textFont = " + font);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyAlignmentChanged(boolean justifyAlignment) {
        popupNav.setAlignmentButtonSelected(justifyAlignment);
        ((TfilonActivity) requireActivity()).preferences.edit().putBoolean("isAlignedBothSides", justifyAlignment).apply();
        tfilahAdapter.justifyAlignment = justifyAlignment;
        tfilahAdapter.notifyDataSetChanged();
        Log.d("##### notifyAlignmentChanged #####", "justifyAlignment = " + justifyAlignment);
    }

    @Override
    public void onDestroy() {
        if (popupNav != null) popupNav.dismiss();
        super.onDestroy();
    }

}
