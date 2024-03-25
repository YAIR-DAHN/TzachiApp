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

    SharedPreferences sp;
    public HolidaysFinder(Context context) {
        Calendar calendar = Calendar.getInstance();
        sp = context.getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(context), Context.MODE_PRIVATE);

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

    public static ArrayList<Location> getLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location("Ramat Gan", 32.0806, 34.8269));
        locations.add(new Location("Bnei Brak", 32.0806, 34.8269));
        locations.add(new Location("Tel Aviv", 32.0667, 34.7833));
        locations.add(new Location("Jerusalem", 31.7833, 35.2167));
        return locations;

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
    }

    public boolean isPurim() {
        if (sp.getString("location", "Tel Aviv").equals("Jerusalem")) {
            return new JewishCalendar(calendarMinus(getJewishCalendar().getGregorianCalendar(), Calendar.DAY_OF_MONTH, 1)).isPurim();
        }

        return getJewishCalendar().isPurim();
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

    public Calendar calendarMinus(Calendar calendar, int type, int amount) {
        calendar.set(type, calendar.get(type)-amount);
        return calendar;
    }

    public JewishCalendar getJewishCalendar() {
        return jewishCalendar;
    }

}
