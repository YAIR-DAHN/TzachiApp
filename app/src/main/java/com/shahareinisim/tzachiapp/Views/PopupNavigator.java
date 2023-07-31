package com.shahareinisim.tzachiapp.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.shahareinisim.tzachiapp.Adapters.TfilahAdapter;
import com.shahareinisim.tzachiapp.Adapters.TitleAdapter;
import com.shahareinisim.tzachiapp.R;

public class PopupNavigator extends PopupWindow {

    RecyclerView rvTitles;
    MaterialButton biggerText, smallerText;

    @SuppressLint({"UseCompatLoadingForDrawables", "InflateParams"})
    public PopupNavigator(@NonNull Context context) {
        super(context);
        setContentView(LayoutInflater.from(context).inflate(R.layout.popup_navigator, null));

        setBackgroundDrawable(context.getDrawable(R.color.transparent));
        setOutsideTouchable(true);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        setWidth(width / 2);

        rvTitles = getContentView().findViewById(R.id.rv_titles);
        rvTitles.setLayoutManager(new LinearLayoutManager(context));
        biggerText = getContentView().findViewById(R.id.bigger_text);
        smallerText = getContentView().findViewById(R.id.smaller_text);

        biggerText.setWidth(width / 5);
        smallerText.setWidth(width / 5);
    }

    public void setAdapter(TitleAdapter adapter) {
        rvTitles.setAdapter(adapter);
    }

    public void setBiggerTextClickListener(Runnable biggerTextClickListener) {
        biggerText.setOnClickListener(view -> biggerTextClickListener.run());
    }

    public void setSmallerTextClickListener(Runnable smallerTextClickListener) {
        smallerText.setOnClickListener(view -> smallerTextClickListener.run());
    }

    public void sizeButtonsEnabled(int textSize) {
        biggerText.setEnabled(textSize != TfilahAdapter.textTypes.length-1);
        smallerText.setEnabled(textSize != 0);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

