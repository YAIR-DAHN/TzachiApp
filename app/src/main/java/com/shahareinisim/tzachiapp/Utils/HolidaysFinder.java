package com.shahareinisim.tzachiapp.Utils;

import com.kosherjava.zmanim.ZmanimCalendar;
import com.kosherjava.zmanim.hebrewcalendar.HebrewDateFormatter;
import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;
import com.kosherjava.zmanim.hebrewcalendar.JewishDate;
import com.kosherjava.zmanim.hebrewcalendar.TefilaRules;

public class HolidaysFinder {

    ZmanimCalendar jewCal;
    TefilaRules tr;
    public JewishCalendar jewishCalendar;
    HebrewDateFormatter hdf;
    public HolidaysFinder() {
        jewCal = new ZmanimCalendar();

        tr = new TefilaRules();
        jewishCalendar = new JewishCalendar();
        hdf = new HebrewDateFormatter();
//        jewishCalendar.setJewishDate(5783, JewishDate.TISHREI, 1); // Rosh Hashana
//        System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jewishCalendar));
//        jewishCalendar.setJewishDate(5783, JewishDate.ADAR, 17);
//        System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jewishCalendar));
//        tr.setTachanunRecitedWeekOfPurim(false);
//        System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jewishCalendar));


    }

    public boolean isRoshChodesh() {
        return jewishCalendar.isRoshChodesh();
    }

    public YahalehVeyavoh isYaalehVeyavoRecited(JewishCalendar jewishCalendar) {
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

    enum YahalehVeyavoh {NONE, PESACH, SHAVUOS, ROSH_HASHANA, YOM_KIPPUR, SUCCOS, SHMINI_HATZERES, SIMCHAS_TORAH, ROSH_CHODESH}
}
