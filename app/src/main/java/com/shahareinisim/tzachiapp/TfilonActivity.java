package com.shahareinisim.tzachiapp;

import static com.shahareinisim.tzachiapp.MainActivity.setCurrentTfilah;
import static com.shahareinisim.tzachiapp.Utils.LocationFinderInitializer.REQUEST_CHECK_SETTINGS;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutManager;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.shahareinisim.tzachiapp.Models.Zman;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shahareinisim.tzachiapp.Fragments.TfilahFragment;
import com.shahareinisim.tzachiapp.Utils.HolidaysFinder;
import com.shahareinisim.tzachiapp.Utils.LocationFinderInitializer;
import com.shahareinisim.tzachiapp.Utils.LocationSpinnerInitializer;
import com.shahareinisim.tzachiapp.Views.TfilonItem;

import java.util.ArrayList;

public class TfilonActivity extends BaseActivity {


    LocationSpinnerInitializer locationSpinnerInitializer = new LocationSpinnerInitializer();
    LocationFinderInitializer locationFinderInitializer = new LocationFinderInitializer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfilon);

        initCardList();

        locationSpinnerInitializer.initialize(this, this::initCardList);
        locationFinderInitializer.initialize(this, locationSpinnerInitializer);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // Location settings are satisfied. The client can initialize
                Log.d("TfilonActivity", "onActivityResult: Location settings are satisfied");
                locationFinderInitializer.checkLocationSettings(false);
            } else {
                // Location settings are not satisfied.
                Log.d("TfilonActivity", "onActivityResult: Location settings are not satisfied");
                Snackbar.make(findViewById(R.id.location_finder), R.string.location_settings_not_satisfied, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void initCardList() {
        ((LinearLayout) findViewById(R.id.dashboard)).removeAllViews();
        ((LinearLayout) findViewById(R.id.container_zmanim)).removeAllViews();
        if (new HolidaysFinder(this).isPurim()) {
            initCardView(getString(R.string.megilat_esther), view -> tfilahFragment(TfilahFragment.Tfilah.MEGILAT_ESTHER));
        }
        initCardView(getString(R.string.shachrit), view -> tfilahFragment(TfilahFragment.Tfilah.SHACHRIT));
        initCardView(getString(R.string.mincha), view -> tfilahFragment(TfilahFragment.Tfilah.MINCHA));
        initCardView(getString(R.string.harvit), view -> tfilahFragment(TfilahFragment.Tfilah.HARVIT));
        initCardView(getString(R.string.bircat_hamazon), view -> tfilahFragment(TfilahFragment.Tfilah.BIRCAT_HAMAZON));
        initCardView(getString(R.string.birkat_men_shalosh), view -> tfilahFragment(TfilahFragment.Tfilah.BIRCAT_MEN_SHALOSH));
        initCardView(getString(R.string.tfilat_hadereh), view -> tfilahFragment(TfilahFragment.Tfilah.TFILAT_HADEREH));
        initCardView(getString(R.string.kriat_shema), view -> tfilahFragment(TfilahFragment.Tfilah.KRIAT_SHEMA));
        initCardView(getString(R.string.asher_yatzar), view -> tfilahFragment(TfilahFragment.Tfilah.ASHER_YATZAR));
        initCardView(getString(R.string.bircat_halevana), view -> tfilahFragment(TfilahFragment.Tfilah.BIRCAT_HALEVANA));
        initCardView(getString(R.string.trumot_vmeasrot), view -> tfilahFragment(TfilahFragment.Tfilah.TRUMOT_VMEASROT));
        initCardView(getString(R.string.perek_shira), view -> tfilahFragment(TfilahFragment.Tfilah.PEREK_SHIRA));
        initCardView(getString(R.string.tikun_haklali), view -> tfilahFragment(TfilahFragment.Tfilah.TIKUN_HAKLALI));

        ArrayList<Zman> zmanimFromNow = new HolidaysFinder(this).zmanimFromNow(null, 3);
        addLabel(getString(R.string.near_times_of_day), true);
        for (Zman zman : zmanimFromNow) {
            if (zman.isLabelOnly()) addLabel(zman.getLabel(), false);
            else {
                String time = new SimpleDateFormat("HH:mm:ss").format(zman.getZman());
                initTimeItem(zman.getLabel(), time);
            }

        }

        findViewById(R.id.btn_show_more).setOnClickListener(v ->
                startActivity(new Intent(TfilonActivity.this, ZmanimActivity.class)));
    }

    private void addLabel(String label, boolean isTop) {
        TextView tvLabel = new TextView(this);
        tvLabel.setText(label);
        tvLabel.setPadding(convertToPX(5), convertToPX(isTop ? 5 : 15), convertToPX(5), convertToPX(10));
        ((LinearLayout) findViewById(R.id.container_zmanim)).addView(tvLabel);
    }

    public void initCardView(String title, View.OnClickListener onClickListener) {

        TfilonItem tfilonItem = new TfilonItem(0, title, this);
        tfilonItem.setOnClickListener(onClickListener);
        ((LinearLayout) findViewById(R.id.dashboard)).addView(tfilonItem);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tfilonItem.getLayoutParams();
        params.setMargins(convertToPX(20), convertToPX(5), convertToPX(20), convertToPX(5));
    }

    @SuppressLint("InflateParams")
    public void initTimeItem(String timeName, String time) {
        LayoutInflater inflater = LayoutInflater.from(this);
        MaterialCardView timeItem = (MaterialCardView) inflater.inflate(R.layout.item_zmanim, null);
        ((TextView) timeItem.findViewById(R.id.label)).setText(timeName);
        ((TextView) timeItem.findViewById(R.id.time)).setText(time);;

        ((LinearLayout) findViewById(R.id.container_zmanim)).addView(timeItem);
    }

    public void tfilahFragment(TfilahFragment.Tfilah tfilah) {

        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // replace the FrameLayout with new Fragment
        fragmentTransaction.add(R.id.fragment_container, new TfilahFragment(tfilah), tfilah.toString());
//        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack("");


        fragmentTransaction.commit();
    }

    public void createShortcut(TfilahFragment.Tfilah tfilah, String label) {

        Intent shortcutIntent = new Intent(this, TfilonActivity.class);
        shortcutIntent.setAction(Intent.ACTION_VIEW);
        shortcutIntent.putExtra("TFILAH", tfilah.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = this.getSystemService(ShortcutManager.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (shortcutManager.isRequestPinShortcutSupported()) {
                    // Assumes there's already a shortcut with the ID "my-shortcut".
                    // The shortcut must be enabled.
                    ShortcutInfoCompat pinShortcutInfo = new ShortcutInfoCompat.Builder(this, tfilah + "-shortcut").setShortLabel(label)
                            .setLongLabel("פתח את תפילת " + label.replace("תפילת ", ""))
                            .setIcon(IconCompat.createWithResource(getApplicationContext(), R.drawable.banner_tfhilot))
                            .setIntent(shortcutIntent)
                            .build();

                    Intent pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo.toShortcutInfo());

                    PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                            pinnedShortcutCallbackIntent, PendingIntent.FLAG_IMMUTABLE);

                    shortcutManager.requestPinShortcut(pinShortcutInfo.toShortcutInfo(), successCallback.getIntentSender());
                }
            }
        } else {
            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, label);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(this, R.drawable.banner_tfhilot));

            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//        addIntent.putExtra("duplicate" , false);
//        getApplicationContext().sendBroadcast(addIntent);
            sendBroadcast(addIntent);
        }
    }

    public void addMostUsedTfilah(TfilahFragment.Tfilah tfilah, String label) {

        setCurrentTfilah(tfilah.toString());
        if (tfilah.toString().equals(getCurrentTfilah())) return;

        Intent shortcutIntent = new Intent(this, TfilonActivity.class);
        shortcutIntent.setAction(Intent.ACTION_VIEW);
        shortcutIntent.putExtra("TFILAH", tfilah.toString());

        ShortcutInfoCompat pinShortcutInfo = new ShortcutInfoCompat.Builder(this, "most used-shortcut").setShortLabel(label)
                .setLongLabel("פתח את תפילת " + label.replace("תפילת ", ""))
                .setIcon(IconCompat.createWithResource(getApplicationContext(), R.drawable.banner_tfhilot))
                .setIntent(shortcutIntent)
                .build();

        ShortcutManagerCompat.pushDynamicShortcut(getApplicationContext(), pinShortcutInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String tfilahString = getIntent().getStringExtra("TFILAH");
        if (tfilahString != null) {
            if (tfilahString.equals(MainActivity.currentTfilah)) finish();
            tfilahFragment(TfilahFragment.Tfilah.valueOf(tfilahString));
        }

        locationFinderInitializer.checkLocationSettings(true);
    }

    public String getCurrentTfilah() {
        if (getSupportFragmentManager().getFragments().isEmpty()) return "null";
        return getSupportFragmentManager().getFragments().get(0).getTag();
    }

}