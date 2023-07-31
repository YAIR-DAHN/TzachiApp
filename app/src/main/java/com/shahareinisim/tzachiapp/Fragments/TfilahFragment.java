package com.shahareinisim.tzachiapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.shahareinisim.tzachiapp.Utils.ShachritUtils;
import com.shahareinisim.tzachiapp.Views.PopupNavigator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class TfilahFragment extends Fragment {

    public enum Tfilah {SHACHRIT, MINCHA, HARVIT, BIRCAT_HAMAZON, BIRCAT_HALEVANA, TFILAT_HADEREH, KRIAT_SHEMA}

    Tfilah tfilah;
    FloatingActionButton navigator;
    RecyclerView rvTfilah;
    TextView title;
    PopupNavigator popupNav;

    TfilahAdapter tfilahAdapter;

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
        }

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

        String part;
        while ((part = bufferedReader.readLine()) != null) {
            String key = "";
            if (part.startsWith("[")) key = part.substring(part.indexOf("["),part.indexOf("]")+1);
            else if (part.equals("")) key = "[e]";
            if (key.equals("[t]")) {
                titleParts.add(new TfilahTitlePart(parts.size(), part));
            } else if(key.equals("[sod]")) {
                part = ShachritUtils.getSongOfCurrentDay(requireActivity().getResources().openRawResource(R.raw.song_of_day));
            }

            parts.add(new TfilahPart(key, part));
        }

        tfilahAdapter = new TfilahAdapter(parts, getTextSize());

        if (popupNav == null) {
            popupNav = new PopupNavigator(requireContext());
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
