package com.shahareinisim.tzachiapp;

import static com.shahareinisim.tzachiapp.MainActivity.setCurrentTfilah;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ShortcutManager;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.shahareinisim.tzachiapp.Models.Zman;
import com.shahareinisim.tzachiapp.Models.Location;

import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.shahareinisim.tzachiapp.Fragments.TfilahFragment;
import com.shahareinisim.tzachiapp.Utils.HolidaysFinder;
import com.shahareinisim.tzachiapp.Utils.LocationSpinnerInitializer;
import com.shahareinisim.tzachiapp.Views.TfilonItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TfilonActivity extends BaseActivity {

    MaterialButton locationFinder;
    FusedLocationProviderClient fusedLocationClient;
    ActivityResultLauncher<String[]> locationPermissionRequest;
    LocationSpinnerInitializer locationSpinnerInitializer = new LocationSpinnerInitializer();
    private static final int REQUEST_CHECK_SETTINGS = 199;
    private boolean locationReqested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfilon);

        initCardList();

        AutoCompleteTextView locations = findViewById(R.id.location_spinner);
        locationSpinnerInitializer.initialize(locations, this, this::initCardList);
        locationFinder = findViewById(R.id.location_finder);
        locationFinder.setOnClickListener(v -> checkLocationSettings(false));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(TfilonActivity.this);
        registerLocationPermissionRequest();
        checkLocationSettings(true);
    }


    private void getCurrentLocation() {

        if (locationPermissionRequest == null) registerLocationPermissionRequest();

        // ...

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(new String[] {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    @SuppressLint("MissingPermission")
    public void registerLocationPermissionRequest() {
        locationPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {

                    Boolean fineLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION,false);

                    fineLocationGranted = fineLocationGranted != null && fineLocationGranted;
                    coarseLocationGranted = coarseLocationGranted != null && coarseLocationGranted;

                    if (fineLocationGranted || coarseLocationGranted) {
                        // Precise location access granted.
                        locationReqested = true;

                        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                            if (location == null) return;
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            String locationString = "";
                            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                assert addresses != null;
                                if (!addresses.isEmpty()) {
                                    for (Address address : addresses) {
                                        locationString = address.getAddressLine(0);
                                        locationString = locationString.substring(locationString.indexOf(",")+2);
                                    }
                                }
                                if (!locationString.isEmpty()) {
                                    LocationSpinnerInitializer.saveLocation(preferences.edit(), new Location(locationString.trim(), latitude, longitude));
                                    locationSpinnerInitializer.updateCurrentLocation();
                                }
                            } catch (IOException e) {
                                locationString = getString(R.string.location_unknown);
                            }

                            Log.d("TfilonActivity", "location: " + locationString);
                        });
                    } else {
                        // No location access granted.
                        locationReqested = false;
                    }
                }
        );
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkLocationSettings(boolean checkSettingsOnly) {
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
//
//        SettingsClient client = LocationServices.getSettingsClient(this);
//        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        LocationRequest.Builder locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();

        locationSettingsRequestBuilder.addLocationRequest(locationRequest.build());
        locationSettingsRequestBuilder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {
            locationFinder.setIcon(getDrawable(R.drawable.location));
            if (checkSettingsOnly) return;

            getCurrentLocation();
        });

        task.addOnFailureListener(this, e -> {
            locationFinder.setIcon(getDrawable(R.drawable.location_disabled));
            if (checkSettingsOnly) return;

            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(TfilonActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // Location settings are satisfied. The client can initialize
                Log.d("TfilonActivity", "onActivityResult: Location settings are satisfied");
                checkLocationSettings(false);
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
        ((TextView) timeItem.findViewById(R.id.time_name)).setText(timeName);
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

        checkLocationSettings(true);
    }

    public String getCurrentTfilah() {
        if (getSupportFragmentManager().getFragments().isEmpty()) return "null";
        return getSupportFragmentManager().getFragments().get(0).getTag();
    }

}