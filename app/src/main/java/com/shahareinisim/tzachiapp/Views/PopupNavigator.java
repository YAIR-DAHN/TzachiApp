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
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.shahareinisim.tzachiapp.Adapters.TfilahAdapter;
import com.shahareinisim.tzachiapp.Adapters.TitleAdapter;
import com.shahareinisim.tzachiapp.R;
import com.shahareinisim.tzachiapp.databinding.LayoutNavigationBinding;
import com.shahareinisim.tzachiapp.databinding.LayoutPreferencesBinding;
import com.shahareinisim.tzachiapp.databinding.NavigatorPopupMenuBinding;

public class PopupNavigator extends PopupWindow {

    private static final int NAVIGATION = R.id.navigation, PREFERENCES = R.id.preferences;
    NavigatorPopupMenuBinding binding;
    LayoutNavigationBinding navigationLayout;
    LayoutPreferencesBinding preferencesLayout;
    public boolean isShown = false;

    @SuppressLint({"UseCompatLoadingForDrawables", "InflateParams"})
    public PopupNavigator(@NonNull Context context) {
        super(context);
        binding = NavigatorPopupMenuBinding.inflate(LayoutInflater.from(context), null, false);
        setContentView(binding.getRoot());
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
        navigationLayout = LayoutNavigationBinding.inflate(LayoutInflater.from(getContext()), null, false);
        preferencesLayout = LayoutPreferencesBinding.inflate(LayoutInflater.from(getContext()), null, false);

        navigationLayout.rvTitles.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void initBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(selectedItem -> {

            binding.placeholder.removeAllViews();

            if (selectedItem.getItemId() == PREFERENCES) {
                binding.placeholder.addView(preferencesLayout.getRoot());
            } else if (selectedItem.getItemId() == NAVIGATION) {
                binding.placeholder.addView(navigationLayout.getRoot());
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
        navigationLayout.rvTitles.setAdapter(adapter);
    }

    public void onShortcutClick(View.OnClickListener onClickListener) {
        preferencesLayout.shortcut.setOnClickListener(onClickListener);
    }

    public void setBiggerTextClickListener(Runnable biggerTextClickListener) {
        preferencesLayout.biggerText.setOnClickListener(view -> biggerTextClickListener.run());
    }

    public void setSmallerTextClickListener(Runnable smallerTextClickListener) {
        preferencesLayout.smallerText.setOnClickListener(view -> smallerTextClickListener.run());
    }

    public void setAlignJustifyClickListener(Runnable alignJustifyClickListener) {
        preferencesLayout.alignJustify.setOnClickListener(view -> alignJustifyClickListener.run());
    }

    public void setAlignRightClickListener(Runnable alignRightClickListener) {
        preferencesLayout.alignRight.setOnClickListener(view -> alignRightClickListener.run());
    }

    public void setAlignmentButtonSelected(boolean justifyAlignment) {
        preferencesLayout.alignJustify.setChecked(justifyAlignment);
        preferencesLayout.alignRight.setChecked(!justifyAlignment);
    }

    public void sizeButtonsEnabled(int textSize) {
        preferencesLayout.biggerText.setEnabled(textSize != TfilahAdapter.textTypes.length-1);
        preferencesLayout.smallerText.setEnabled(textSize != 0);
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
        binding.bottomNavigation.setSelectedItemId(NAVIGATION);
        fixWindowMargins();
        isShown = true;
        super.showAsDropDown(anchor, xoff, yoff);
    }

    public MaterialButtonToggleGroup getFontChooser() {
        return preferencesLayout.fontsChooser;
    }

    public Context getContext() {
        return getContentView().getContext();
    }
}

