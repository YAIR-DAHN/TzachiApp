package com.shahareinisim.tzachiapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shahareinisim.tzachiapp.TfilonActivity;
import com.shahareinisim.tzachiapp.Views.PopupNavigator;
import com.shahareinisim.tzachiapp.R;
import com.shahareinisim.tzachiapp.Adapters.TfilahAdapter;
import com.shahareinisim.tzachiapp.Models.TfilahPart;
import com.shahareinisim.tzachiapp.Models.TfilahTitlePart;
import com.shahareinisim.tzachiapp.Adapters.TitleAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class TfilahFragment extends Fragment {

    public enum Tfilah {SHACHRIT, MINCHA, HARVIT, BIRCAT_HAMAZON, BIRCAT_HALEVANA, TFILAT_HADEREH, KRIAT_SHMAA}

    Tfilah tfilah;
    FloatingActionButton navigator;
    RecyclerView rvTfilah;
    TextView title;
    PopupNavigator popupNav;
    public  TfilahFragment(Tfilah tfilah) {
        this.tfilah = tfilah;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parent = inflater.inflate(R.layout.fragment_tfilah, container, false);
        parent.setFocusable(true);

        rvTfilah = parent.findViewById(R.id.rv_tfilah);
        rvTfilah.setLayoutManager(new LinearLayoutManager(requireContext()));

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
                title.setText(R.string.birkat_hamazon);
                break;
            case BIRCAT_HALEVANA:
                tfilahFileRes = R.raw.bircat_halevana;
                title.setText(R.string.birkat_halevana);
                break;
            case TFILAT_HADEREH:
                tfilahFileRes = R.raw.tfilat_hadereh;
                title.setText(R.string.tfilat_hadereh);
                break;
            case KRIAT_SHMAA:
                tfilahFileRes = R.raw.kriat_shmaa;
                title.setText(R.string.kriat_Shmaa);
                break;
        }

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(requireActivity().getResources().openRawResource(tfilahFileRes));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            rvTfilah.setAdapter(initTfilahAdapter(bufferedReader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MaterialButton biggerText = parent.findViewById(R.id.bigger_text);
        MaterialButton smallerText = parent.findViewById(R.id.smaller_text);
        biggerText.setOnClickListener(view -> {
            int textSize = ((TfilonActivity) requireActivity()).textSize;
            if (textSize < 2) textSize++;
            ((TfilonActivity) requireActivity()).preferences.edit().putInt("textSize", textSize).apply();
            biggerText.setChecked(false);
        });
        smallerText.setOnClickListener(view -> {
            int textSize = ((TfilonActivity) requireActivity()).textSize;
            if (textSize > 0) textSize--;

            ((TfilonActivity) requireActivity()).preferences.edit().putInt("textSize", textSize).apply();
        });

//        MaterialButtonToggleGroup group = parent.findViewById(R.id.text_size_group);
//        group.addOnButtonCheckedListener((group1, checkedId, isChecked) -> {
//            MaterialButton button = parent.findViewById(checkedId);
//            button.setChecked(false);
//        });

        return parent;
    }

    @SuppressLint({"InflateParams", "NotifyDataSetChanged"})
    public TfilahAdapter initTfilahAdapter(BufferedReader bufferedReader) throws IOException {
        ArrayList<TfilahPart> parts = new ArrayList<>();
        ArrayList<TfilahTitlePart> titleParts = new ArrayList<>();

        String part;
        while ((part = bufferedReader.readLine()) != null) {
            String key = "";
            if (part.length()>3) key = part.substring(0,3);
            else if (part.equals("")) key = "[e]";
            if (key.equals("[t]")) {
                titleParts.add(new TfilahTitlePart(parts.size(), part));
            }

            parts.add(new TfilahPart(key, part));
        }

        TfilahAdapter tfilahAdapter = new TfilahAdapter(parts, ((TfilonActivity) requireActivity()).textSize);

        if (popupNav == null) {
            popupNav = new PopupNavigator(requireContext(), navigator);
            TitleAdapter titleAdapter = new TitleAdapter((TfilonActivity) requireActivity(), titleParts);
            titleAdapter.setPostClickListener(position -> {
                ((LinearLayoutManager) Objects.requireNonNull(
                        rvTfilah.getLayoutManager())).scrollToPositionWithOffset(position,0);
                popupNav.dismiss();
            });
            popupNav.setAdapter(titleAdapter);

            ((TfilonActivity) requireActivity()).changed = () -> {
                tfilahAdapter.textSize = ((TfilonActivity) requireActivity()).textSize;
                tfilahAdapter.notifyDataSetChanged();
            };
        }

        navigator.setOnClickListener(view -> popupNav.show());

        return tfilahAdapter;
    }

    @Override
    public void onDestroy() {
        if (popupNav != null) popupNav.dismiss();
        super.onDestroy();
    }
}