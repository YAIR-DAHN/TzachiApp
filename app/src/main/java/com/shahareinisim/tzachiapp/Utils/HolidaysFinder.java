package com.shahareinisim.tzachiapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.kosherjava.zmanim.ComplexZmanimCalendar;
import com.kosherjava.zmanim.ZmanimCalendar;
import com.kosherjava.zmanim.hebrewcalendar.HebrewDateFormatter;
import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;
import com.kosherjava.zmanim.hebrewcalendar.TefilaRules;
import com.kosherjava.zmanim.util.GeoLocation;
import com.shahareinisim.tzachiapp.Models.Zman;
import com.shahareinisim.tzachiapp.Models.Location;
import com.shahareinisim.tzachiapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HolidaysFinder {

    ZmanimCalendar jewCal;
    TefilaRules tr;
    JewishCalendar jewishCalendar;
    HebrewDateFormatter hdf;

    SharedPreferences sp;

    Context context;
    public HolidaysFinder(Context context) {
        this.context = context;

        Calendar calendar = Calendar.getInstance();
        sp = context.getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(context), Context.MODE_PRIVATE);

        tr = new TefilaRules();
        jewCal = new ZmanimCalendar();
        hdf = new HebrewDateFormatter();

        GeoLocation geoLocation = new GeoLocation();
        Location location = getSavedLocation();
        geoLocation.setLocationName(location.getLocationName());
        geoLocation.setLatitude(location.getLatitude());
        geoLocation.setLongitude(location.getLongitude());

        jewCal.setGeoLocation(geoLocation);

        Log.d("Holidays Finder", "location: " + jewCal.getGeoLocation().getLocationName());

        if (calendar.getTime().after(jewCal.getSunset())) {
            calendar.add(Calendar.DAY_OF_WEEK, 1);
            jewishCalendar = new JewishCalendar(calendar);
        } else jewishCalendar = new JewishCalendar(calendar);

    }

    public Location getSavedLocation() {
        String locationName = sp.getString("location", "Tel Aviv");
        double latitude = sp.getFloat("latitude", 32.0667f);
        double longitude = sp.getFloat("longitude", 34.7833f);
        return new Location(locationName, latitude, longitude);
    }

    public static ArrayList<Location> getLocations(Context context) {
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location(context.getString(R.string.ramat_gan), 32.0806, 34.8269));
        locations.add(new Location(context.getString(R.string.bnei_brak), 32.0806, 34.8269));
        locations.add(new Location(context.getString(R.string.tel_aviv), 32.0667, 34.7833));
        locations.add(new Location(context.getString(R.string.jerusalem), 31.7833, 35.2167));
        locations.add(new Location(context.getString(R.string.haifa), 32.794044, 34.989571));
        locations.add(new Location(context.getString(R.string.tiberias), 32.7938522, 35.5328566));
        locations.add(new Location(context.getString(R.string.beer_sheva), 31.2457442,34.7925181));
        locations.add(new Location(context.getString(R.string.ashkelon), 31.6738509,34.5752428));
        locations.add(new Location(context.getString(R.string.eilat), 29.5569348,34.9497949));
        locations.add(new Location(context.getString(R.string.golders_green),51.629168, -0.155019));
        locations.add(new Location(context.getString(R.string.new_york), 40.712776, -74.005974));
        locations.add(new Location(context.getString(R.string.lakewood), 39.711000, -105.088870));

        return locations;
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

    public ArrayList<Zman> zmanimFromNow(Calendar from, int count) {
        if (from == null) from = jewishCalendar.getGregorianCalendar();

        ArrayList<Zman> zmanim = new ArrayList<>();

        for (Zman zman : getZmanim(from)) {

            Log.d("Holidays Finder", zman.getLabel() + ", " + zman.getZman() + ", " + zman.isSpecial());
            if (zman.getZman().after(new Date())) zmanim.add(zman);

            if (zmanim.size() == count) break;
        }

        // if less then #count for today add tomorrows
        if (zmanim.size() != count) {
            count++;
            from.add(Calendar.DAY_OF_MONTH, 1);
            from.set(Calendar.HOUR_OF_DAY, 0);
            from.set(Calendar.MINUTE, 0);
            from.set(Calendar.SECOND, 0);
            from.set(Calendar.MILLISECOND, 0);
            // add tomorrow label
            zmanim.add(new Zman(null, context.getString(R.string.tomorrow), false));
            zmanim.addAll(zmanimFromNow(from, count - zmanim.size()));
        }

        Log.d("Holidays Finder", "z: " + (zmanim.size()) + ", c: " + count);
        zmanim.sort((o1, o2) -> {
            // specials on top...
            if (o1.isSpecial() && !o2.isSpecial()) return -1;
            if (!o1.isSpecial() && o2.isSpecial()) return 1;
            // sort by date - excluding labels
            if (o1.getZman() != null && o2.getZman() != null) {
                if (o1.getZman().before(o2.getZman())) return -1;
                else if (o1.getZman().after(o2.getZman())) return 1;
            }
            return 0;
        });
        return zmanim;
    }

    ZmanimCalendar zmanimCalendar;

    public ArrayList<Zman> getZmanim(Calendar calendar) {
        zmanimCalendar = new ZmanimCalendar();
        zmanimCalendar.setCalendar(calendar);
        zmanimCalendar.setGeoLocation(jewCal.getGeoLocation());

        ArrayList<Zman> zmanim = new ArrayList<>();
        zmanim.add(new Zman(getZman().getAlos72(), context.getString(R.string.alos_hashachar), false));
        zmanim.add(new Zman(getZman().getSunrise(), context.getString(R.string.sunrise), false));
        zmanim.add(new Zman(getZman().getSofZmanShmaGRA(), context.getString(R.string.sof_zman_shma_gra), false));
        zmanim.add(new Zman(getZman().getSofZmanShmaMGA(), context.getString(R.string.sof_zman_shma_mga), false));
        zmanim.add(new Zman(getZman().getChatzos(), context.getString(R.string.chatzos), false));
        zmanim.add(new Zman(getZman().getSunset(), context.getString(R.string.sunset), false));

        if (jewishCalendar.getDayOfWeek() == 6) {
            zmanim.add(new Zman(getShabbatEnters(getZman().getSunset()), context.getString(R.string.shabbat_candle_lightning), true));
        } else if (jewishCalendar.getDayOfWeek() == 7) {
            zmanim.add(new Zman(getZman().getTzaisGeonim8Point5Degrees(), context.getString(R.string.shabbat_exits), true));
        }

        return zmanim;
    }

    public Date getShabbatEnters(Date sunset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sunset);
        calendar.set(Calendar.MINUTE, -20 + calendar.get(Calendar.MINUTE));
        return calendar.getTime();
    }

    public ComplexZmanimCalendar getZman() {
        ComplexZmanimCalendar czc = new ComplexZmanimCalendar(zmanimCalendar.getGeoLocation());
        czc.setCalendar(zmanimCalendar.getCalendar());
        return czc;
    }

    public JewishCalendar getJewishCalendar() {
        return jewishCalendar;
    }

}
