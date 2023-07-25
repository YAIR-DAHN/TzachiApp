package com.shahareinisim.tzachiapp.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;

import java.util.Objects;

public class PopupNavigator extends ListPopupWindow {

    private final Context context;

    @SuppressLint("UseCompatLoadingForDrawables")
    public PopupNavigator(@NonNull Context context, View view) {
        super(context);

        this.context = context;
//        setBackgroundDrawable(context.getDrawable(R.drawable.round));
        setAnchorView(view);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        int dpi = dm.densityDpi;

        int width = dm.widthPixels;

        setWidth(width / 2);
//        layout = findViewById(R.id.layout);
//
//        list = findViewById(R.id.list);
//        list.setLayoutManager(new LinearLayoutManager(context));
//        list.setClipToOutline(true);
//
//        final float[] pos = {0, 0};
//        view.post(() -> {
//            pos[0] = view.getX();
//            pos[1] = view.getY();
//        });
//        layout.setX(pos[0]);
//        layout.setY(pos[1]);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void show() {
        super.show();
//        Objects.requireNonNull(getListView())
//                .setBackground(context.getDrawable(R.drawable.round));
        getListView().setClipToOutline(true);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

