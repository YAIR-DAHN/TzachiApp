package com.shahareinisim.tzachiapp;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shahareinisim.tzachiapp.Fragments.TfilahFragment;
import com.shahareinisim.tzachiapp.Views.TfilonItem;

public class TfilonActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfilon);

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
        fragmentTransaction.add(R.id.fragment_container, new TfilahFragment(tfilah), "tag");
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
            tfilahFragment(TfilahFragment.Tfilah.valueOf(tfilahString));
        }
    }

}