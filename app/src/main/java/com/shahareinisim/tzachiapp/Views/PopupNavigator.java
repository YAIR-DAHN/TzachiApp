package com.shahareinisim.tzachiapp.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.shahareinisim.tzachiapp.Adapters.TfilahAdapter;
import com.shahareinisim.tzachiapp.Adapters.TitleAdapter;
import com.shahareinisim.tzachiapp.R;

public class PopupNavigator extends PopupWindow {

    RecyclerView rvTitles;
    MaterialButton biggerText, smallerText;
    public MaterialButtonToggleGroup fontsChooser;

    @SuppressLint({"UseCompatLoadingForDrawables", "InflateParams"})
    public PopupNavigator(@NonNull Context context) {
        super(context);
        setContentView(LayoutInflater.from(context).inflate(R.layout.popup_navigator, null));
        getContentView().setClipToOutline(true);

        setBackgroundDrawable(context.getDrawable(R.color.transparent));
        setOutsideTouchable(true);

        fixWindowMargins();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        setWidth(width / 2);

        rvTitles = getContentView().findViewById(R.id.rv_titles);
        rvTitles.setLayoutManager(new LinearLayoutManager(context));
        biggerText = getContentView().findViewById(R.id.bigger_text);
        smallerText = getContentView().findViewById(R.id.smaller_text);
        fontsChooser = getContentView().findViewById(R.id.fonts_chooser);
    }

    public int convertToPX(int dp) {
        Resources r = getContentView().getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
    }

    public void setAdapter(TitleAdapter adapter) {
        rvTitles.setAdapter(adapter);
    }

    public void onShortcutClick(View.OnClickListener onClickListener) {
        ((Button) this.getContentView().findViewById(R.id.shortcut)).setOnClickListener(onClickListener);
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

    public void fixWindowMargins() {
        getContentView().post(() -> {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getContentView().getLayoutParams();
            params.setMargins(convertToPX(5), convertToPX(5), convertToPX(5), convertToPX(5));
            getContentView().setLayoutParams(params);
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        fixWindowMargins();
        super.showAsDropDown(anchor, xoff, yoff);
    }

}

