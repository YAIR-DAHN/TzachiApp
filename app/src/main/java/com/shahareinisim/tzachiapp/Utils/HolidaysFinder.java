package com.shahareinisim.tzachiapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.kosherjava.zmanim.ZmanimCalendar;
import com.kosherjava.zmanim.hebrewcalendar.HebrewDateFormatter;
import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;
import com.kosherjava.zmanim.hebrewcalendar.TefilaRules;
import com.kosherjava.zmanim.util.GeoLocation;
import com.shahareinisim.tzachiapp.Models.Location;

import java.util.ArrayList;
import java.util.Calendar;

public class HolidaysFinder {

    ZmanimCalendar jewCal;
    TefilaRules tr;
    JewishCalendar jewishCalendar;
    HebrewDateFormatter hdf;
    public HolidaysFinder(Context context) {
        Calendar calendar = Calendar.getInstance();
        SharedPreferences sp = context.getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(context), Context.MODE_PRIVATE);

        jewCal = new ZmanimCalendar();
        GeoLocation geoLocation = jewCal.getGeoLocation();
        Location location = getLocation(sp.getString("location", "Tel Aviv"));
        geoLocation.setLocationName(location.getLocationName());
        geoLocation.setLatitude(location.getLatitude());
        geoLocation.setLongitude(location.getLongitude());
//        geoLocation.setElevation(0);
        Log.d("Holidays Finder", "sunset: " + jewCal.getSunset() + ", location: " + jewCal.getGeoLocation().getLocationName());

        tr = new TefilaRules();

        if (calendar.getTime().after(jewCal.getSunset())) {
            calendar.add(Calendar.DAY_OF_WEEK, 1);
            jewishCalendar = new JewishCalendar(calendar);
        } else jewishCalendar = new JewishCalendar(calendar);

        hdf = new HebrewDateFormatter();
//        jewishCalendar.setJewishDate(5783, JewishDate.TISHREI, 1); // Rosh Hashana
//        System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jewishCalendar));
//        jewishCalendar.setJewishDate(5783, JewishDate.ADAR, 17);
//        System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jewishCalendar));
//        tr.setTachanunRecitedWeekOfPurim(false);
//        System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jewishCalendar));
    }

    public Location getLocation(String locationName) {
        for (Location location : getLocations()) {
            if (location.getLocationName().equals(locationName)) return location;
        }
        return null;
    }

    public ArrayList<Location> getLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location("Ramat Gan", 32.0806, 34.8269));
        locations.add(new Location("Bnei Brak", 32.0806, 34.8269));
        locations.add(new Location("Tel Aviv", 32.0667, 34.7833));
        locations.add(new Location("Jerusalem", 31.7833, 35.2167));
        return locations;
    }

    public void setLocationName(String locationName) {
        jewCal.getGeoLocation().setLocationName(locationName);
    }

    public boolean isRoshChodesh() {
        return jewishCalendar.isRoshChodesh();
    }

    public boolean isPurim() {
        return tr.isAlHanissimRecited(jewishCalendar);
    }

    public YahalehVeyavoh isYahalehVeyavoRecited(JewishCalendar jewishCalendar) {
        if (isRoshChodesh()) return YahalehVeyavoh.ROSH_CHODESH;
        else if (jewishCalendar.isPesach()) return YahalehVeyavoh.PESACH;
        else if (jewishCalendar.isShavuos()) return YahalehVeyavoh.SHAVUOS;
        else if (jewishCalendar.isRoshHashana()) return YahalehVeyavoh.ROSH_HASHANA;
        else if (jewishCalendar.isYomKippur()) return YahalehVeyavoh.YOM_KIPPUR;
        else if (jewishCalendar.isSuccos()) return YahalehVeyavoh.SUCCOS;
        else if (jewishCalendar.isShminiAtzeres()) return YahalehVeyavoh.SHMINI_HATZERES;
        else if (jewishCalendar.isSimchasTorah()) return YahalehVeyavoh.SIMCHAS_TORAH;
        else return YahalehVeyavoh.NONE;
    }

    public boolean isNoTachnunRecited() {
        return jewishCalendar.isRoshChodesh() ||
                jewishCalendar.isPesach() ||
                jewishCalendar.isShavuos() ||
                jewishCalendar.isRoshHashana() ||
                jewishCalendar.isYomKippur() ||
                jewishCalendar.isSuccos() ||
                jewishCalendar.isShminiAtzeres() ||
                jewishCalendar.isSimchasTorah();
    }

    public boolean isHoliday() {
        return jewishCalendar.isRoshChodesh() ||
        jewishCalendar.isPesach() ||
        jewishCalendar.isShavuos() ||
        jewishCalendar.isRoshHashana() ||
        jewishCalendar.isYomKippur() ||
        jewishCalendar.isSuccos() ||
        jewishCalendar.isShminiAtzeres() ||
        jewishCalendar.isSimchasTorah();
    }

    public YahalehVeyavoh getYomTov() {
        switch (jewishCalendar.getYomTovIndex()) {
            case 1:
                return YahalehVeyavoh.PESACH;
            case 2:
                return YahalehVeyavoh.SHAVUOS;
            default:
                return YahalehVeyavoh.NONE;
        }
    }

    public JewishCalendar getJewishCalendar() {
        return jewishCalendar;
    }
    enum YahalehVeyavoh {NONE, PESACH, SHAVUOS, ROSH_HASHANA, YOM_KIPPUR, SUCCOS, SHMINI_HATZERES, SIMCHAS_TORAH, ROSH_CHODESH}
}
