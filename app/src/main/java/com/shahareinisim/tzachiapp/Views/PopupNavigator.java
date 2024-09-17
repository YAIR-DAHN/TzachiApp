package com.shahareinisim.tzachiapp.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.shahareinisim.tzachiapp.Adapters.TfilahAdapter;
import com.shahareinisim.tzachiapp.Adapters.TitleAdapter;
import com.shahareinisim.tzachiapp.R;

public class PopupNavigator extends PopupWindow {

    Context mContext;
    BottomNavigationView bottomNavigationView;
    LinearLayout placeHolder, navigationLayout, preferencesLayout;
    RecyclerView rvTitles;
    MaterialButton biggerText, smallerText, alignJustify, alignRight;
    public MaterialButtonToggleGroup fontsChooser;
    public boolean isShown = false;
    private static final int NAVIGATION = R.id.navigation, PREFERENCES = R.id.preferences;

    @SuppressLint({"UseCompatLoadingForDrawables", "InflateParams"})
    public PopupNavigator(@NonNull Context context) {
        super(context);
        mContext = context;
        setContentView(LayoutInflater.from(context).inflate(R.layout.navigator_popup_menu, null));
        getContentView().setClipToOutline(true);

        setBackgroundDrawable(context.getDrawable(R.color.transparent));
        setOutsideTouchable(true);

        fixWindowMargins();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        setWidth(width / 2);

        initViews();
        initBottomNavigation();

        setOnDismissListener(() -> new Handler().postDelayed(() -> isShown = false, 100));
    }

    private void initViews() {
        placeHolder = getContentView().findViewById(R.id.placeholder);
        navigationLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_navigation, null, false);
        preferencesLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_preferences, null, false);

        rvTitles = navigationLayout.findViewById(R.id.rv_titles);
        rvTitles.setLayoutManager(new LinearLayoutManager(getContext()));
        biggerText = preferencesLayout.findViewById(R.id.bigger_text);
        smallerText = preferencesLayout.findViewById(R.id.smaller_text);
        fontsChooser = preferencesLayout.findViewById(R.id.fonts_chooser);
        alignJustify = preferencesLayout.findViewById(R.id.align_justify);
        alignRight = preferencesLayout.findViewById(R.id.align_right);
    }

    public void initBottomNavigation() {
        bottomNavigationView = getContentView().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(selectedItem -> {

            if (placeHolder == null) initViews();
            placeHolder.removeAllViews();

            if (selectedItem.getItemId() == PREFERENCES) {
                placeHolder.addView(preferencesLayout);
            } else if (selectedItem.getItemId() == NAVIGATION) {
                placeHolder.addView(navigationLayout);
            }

            selectedItem.setChecked(true);

            return false;
        });
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
        preferencesLayout.findViewById(R.id.shortcut).setOnClickListener(onClickListener);
    }

    public void setBiggerTextClickListener(Runnable biggerTextClickListener) {
        biggerText.setOnClickListener(view -> biggerTextClickListener.run());
    }

    public void setSmallerTextClickListener(Runnable smallerTextClickListener) {
        smallerText.setOnClickListener(view -> smallerTextClickListener.run());
    }

    public void setAlignJustifyClickListener(Runnable alignJustifyClickListener) {
        alignJustify.setOnClickListener(view -> alignJustifyClickListener.run());
    }

    public void setAlignRightClickListener(Runnable alignRightClickListener) {
        alignRight.setOnClickListener(view -> alignRightClickListener.run());
    }

    public void setAlignmentButtonSelected(boolean justifyAlignment) {
        alignJustify.setChecked(justifyAlignment);
        alignRight.setChecked(!justifyAlignment);
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
        if (isShown) return;
        bottomNavigationView.setSelectedItemId(NAVIGATION);
        fixWindowMargins();
        isShown = true;
        super.showAsDropDown(anchor, xoff, yoff);
    }

    public Context getContext() {
        return mContext;
    }
}

