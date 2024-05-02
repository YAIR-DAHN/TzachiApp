package com.shahareinisim.tzachiapp;

import static android.graphics.Color.BLUE;
import static android.view.Gravity.CENTER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.shahareinisim.tzachiapp.Fragments.WebViewFragment;
import com.shahareinisim.tzachiapp.Views.MainItem;

public class MainActivity extends BaseActivity {

    public static final String MAIN_WEB_LINK = "https://63863ecdd7031.site123.me/";
    public static final String DONATION_LINK = "https://www.matara.pro/nedarimplus/online/?mosad=7001292";
    static final String ZMANEI_TFILOT_LINK = "https://63863ecdd7031.site123.me/#section-64a56d581a3bc";
    public static String currentTfilah = "";

    public static void setCurrentTfilah(String currentTfilah) {
        MainActivity.currentTfilah = currentTfilah;
    }

    @SuppressLint({"SetJavaScriptEnabled", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWVCardView(getString(R.string.title_main_website), R.drawable.banner_main_website, MAIN_WEB_LINK);
        initCardView(getString(R.string.title_tfilon), R.drawable.banner_tfhilon, view ->
                startActivity(new Intent(MainActivity.this, TfilonActivity.class)));
        initWVCardView(getString(R.string.title_zmanei_tfilot), R.drawable.banner_tfhilot, ZMANEI_TFILOT_LINK);
        initWVCardView(getString(R.string.title_donations), R.drawable.banner_donations, DONATION_LINK);

        Chip about = findViewById(R.id.about);
        about.setOnClickListener(v -> {
            LinearLayout dialogRootView = new LinearLayout(this);
            dialogRootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            dialogRootView.setPadding(0, convertToPX(20), 0, 0);
            dialogRootView.setOrientation(LinearLayout.VERTICAL);
            dialogRootView.addView(getDialogItem(getString(R.string.developer), "יעקב א.", false));
            dialogRootView.addView(getDialogItem(getString(R.string.contact), "Jacobel640@gmail.com", true));
            TextView version = new TextView(this);
            version.setPadding(convertToPX(20), convertToPX(30), convertToPX(20), 0);
            version.setText(getString(R.string.version, BuildConfig.VERSION_NAME));
            version.setGravity(CENTER);
            dialogRootView.addView(version);

            new MaterialAlertDialogBuilder(this)
                    .setView(dialogRootView)
                    .setTitle(R.string.app_name)
                    .setIcon(R.drawable.app_icon)
                    .setPositiveButton(R.string.close, (dialog, which) -> dialog.dismiss())
                    .show();
        });

        checkForUpdate();
    }

    public LinearLayout getDialogItem(String title, String text, boolean contact) {
        @SuppressLint("InflateParams") LinearLayout dialogItem = (LinearLayout) getLayoutInflater().inflate(R.layout.item_about, null, false);
        ((TextView) dialogItem.findViewById(R.id.title)).setText(title);
        TextView textView = dialogItem.findViewById(R.id.text);
        textView.setText(text);

        if (contact) {
            textView.setTextColor(BLUE);
            textView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(android.net.Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"Jacobel640@gmail.com"});
                startActivity(intent);
            });
        }
        return dialogItem;
    }

    public void initWVCardView(String title, int bannerImg, String url) {

        MainItem mainItem = new MainItem(0, title, bannerImg, this);
        mainItem.setOnClickListener(view -> webFragment(url));
        ((LinearLayout) findViewById(R.id.dashboard)).addView(mainItem);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mainItem.getLayoutParams();
        params.setMargins(convertToPX(20), convertToPX(5), convertToPX(20), convertToPX(5));
    }

    public void initCardView(String title, int bannerImg, View.OnClickListener onClickListener) {

        MainItem mainItem = new MainItem(0, title, bannerImg, this);
        mainItem.setOnClickListener(onClickListener);
        ((LinearLayout) findViewById(R.id.dashboard)).addView(mainItem);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mainItem.getLayoutParams();
        params.setMargins(convertToPX(20), convertToPX(5), convertToPX(20), convertToPX(5));
    }

    public void webFragment(String url) {

        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // replace the FrameLayout with new Fragment
        fragmentTransaction.add(R.id.fragment_container, WebViewFragment.newInstance(url, ""), "tag");
//        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack("");

        fragmentTransaction.commit();
    }

    // self update check with google play store
    private void checkForUpdate() {
        // Create an instance of the AppUpdateManager
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

        // Check for updates
        // Check for updates
        Log.d("MainActivity", "Update check started!");
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            100);
                } catch (IntentSender.SendIntentException e) {
                    Log.d("MainActivity", "Update check failed!", e);
                }
            } else {
                Log.d("MainActivity", "No update available or update not allowed.");
            }
        }).addOnFailureListener(e -> Log.d("MainActivity", "Update check failed!", e));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Log.d("MainActivity", "Success! Result code: " + resultCode);
                Snackbar.make(findViewById(R.id.fragment_container), R.string.message_new_version, Snackbar.LENGTH_LONG)
                        .setAction(R.string.button_update, view -> {
                            // intent to app on play store
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.shahareinisim.tzachiapp"));
                            startActivity(intent);
                        }).show();
            } else Log.d("MainActivity", "Update flow failed! Result code: " + resultCode);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        int stackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (stackCount > 0) {
            try {
                WebViewFragment fragment  = (WebViewFragment) getSupportFragmentManager().getFragments().get(stackCount-1);
                if ((keyCode == KeyEvent.KEYCODE_BACK) && fragment.canGoBack()) {
                    fragment.goBack();
                    return true;
                }
            } catch (IndexOutOfBoundsException exception) {
                Log.d("IndexOutOfBoundsException", exception.toString());
            }
        }

        Log.d("", "stackCount="+stackCount);
        return super.onKeyDown(keyCode, event);
    }

}