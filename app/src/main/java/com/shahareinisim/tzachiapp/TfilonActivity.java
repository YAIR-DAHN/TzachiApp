package com.shahareinisim.tzachiapp;

import static com.shahareinisim.tzachiapp.MainActivity.setCurrentTfilah;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.shahareinisim.tzachiapp.Fragments.TfilahFragment;
import com.shahareinisim.tzachiapp.Utils.HolidaysFinder;
import com.shahareinisim.tzachiapp.Views.TfilonItem;

import java.util.ArrayList;

public class TfilonActivity extends BaseActivity {

    SharedPreferences sp;
    ArrayList<String> locationsList = getLocationsList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfilon);

        sp = getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(this), MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();

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

        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, locationsList);

        AppCompatSpinner locations = findViewById(R.id.location_spinner);
        locations.setAdapter(adapter);

        int savedLocation = getSavedLocation();
        locations.setSelection(savedLocation);


        locations.setOnItemSelectedListener(new AppCompatSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                editor.putString("location", locationsList.get(position));
                editor.apply();
                if (getSavedLocation() != savedLocation) recreate();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private int getSavedLocation() {
        for (String location : locationsList) {
            if (location.equals(sp.getString("location", "Tel Aviv"))) return locationsList.indexOf(location);
        }

        return 0;
    }

    @NonNull
    private static ArrayList<String> getLocationsList() {
        ArrayList<String> locationsList = new ArrayList<>();
        locationsList.add("Ramat Gan");
        locationsList.add("Bnei Brak");
        locationsList.add("Tel Aviv");
        locationsList.add("Jerusalem");
//        locationsList.add("Haifa");
//        locationsList.add("Be'er Sheva");
//        locationsList.add("Eilat");
//        locationsList.add("Tiberias");
//        locationsList.add("Safed");
//        locationsList.add("Ashdod");
//        locationsList.add("Netanya");
//        locationsList.add("Rishon LeZion");
//        locationsList.add("Petah Tikva");
//        locationsList.add("Holon");
//        locationsList.add("Rehovot");
//        locationsList.add("Bat Yam");
//        locationsList.add("Ashkelon");
//        locationsList.add("Jaffa");
//        locationsList.add("Modi'in-Maccabim-Re'ut");
//        locationsList.add("Herzliya");
//        locationsList.add("Kfar Saba");
//        locationsList.add("Ra'anana");
//        locationsList.add("Bet Shemesh");
//        locationsList.add("Lod");
//        locationsList.add("Ramla");
//        locationsList.add("Nahariya");
//        locationsList.add("Kiryat Ata");
//        locationsList.add("Giv'atayim");
//        locationsList.add("Hadera");
        return locationsList;
    }

    public void initCardView(String title, View.OnClickListener onClickListener) {

        TfilonItem tfilonItem = new TfilonItem(0, title, this);
        tfilonItem.setOnClickListener(onClickListener);
        ((LinearLayout) findViewById(R.id.dashboard)).addView(tfilonItem);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tfilonItem.getLayoutParams();
        params.setMargins(convertToPX(20), convertToPX(5), convertToPX(20), convertToPX(5));
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
    }

    public String getCurrentTfilah() {
        if (getSupportFragmentManager().getFragments().isEmpty()) return "null";
        return getSupportFragmentManager().getFragments().get(0).getTag();
    }

}