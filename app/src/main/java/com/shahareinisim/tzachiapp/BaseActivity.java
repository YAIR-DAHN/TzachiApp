package com.shahareinisim.tzachiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;

public class BaseActivity extends AppCompatActivity {

    boolean nightMode;
    public int textSize;
    public SharedPreferences preferences;
    public OnTextSizeChanged changed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getPreferences(MODE_PRIVATE);
        preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, s) -> initPreferences());
        initPreferences();

        if (changed!=null)
            changed = () -> {};
    }

    private void initPreferences() {
        nightMode = preferences.getBoolean("nightMode", false);
        textSize = preferences.getInt("textSize", 0);
        enableNightMode(nightMode);
        if (changed != null) changed.changeTextSize();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        if (newConfig.uiMode == isNightMode()) enableNightMode(nightMode);
        newConfig.uiMode = isNightMode();
        super.onConfigurationChanged(newConfig);
    }

    private void enableNightMode(boolean night){
        AppCompatDelegate.setDefaultNightMode(night ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private int isNightMode(){
        return nightMode ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
    }

    public int convertToPX(int dp) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
    }
}