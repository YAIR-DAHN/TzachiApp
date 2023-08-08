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

    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferences = getPreferences(MODE_PRIVATE);
        enableNightMode(getNightMode());

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        if (newConfig.uiMode == isNightMode()) enableNightMode(getNightMode());
        newConfig.uiMode = isNightMode();

        super.onConfigurationChanged(newConfig);
    }

    private void enableNightMode(boolean night){
        AppCompatDelegate.setDefaultNightMode(night ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private int isNightMode(){
        return getNightMode() ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
    }

    public int convertToPX(int dp) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
    }

    public boolean getNightMode() {
        return preferences.getBoolean("nightMode", false);
    }

    public int getTextSize() {
        return preferences.getInt("textSize", 1);
    }
}