package com.shahareinisim.tzachiapp.Models;

import android.util.Log;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class Zman  extends com.kosherjava.zmanim.util.Zman {

    boolean special, isLabelOnly;

    public Zman(Date date, String label, boolean isSpecial) {
        super(date, label);
        this.special = isSpecial;
        setLabelOnly();
    }

    public boolean isSpecial() {
        return special;
    }

    public void setLabelOnly() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getZman());
        Log.d("Zman", "setLabelOnly: " + getLabel() + ", " + calendar.getTime());
        isLabelOnly = /*(calendar.get(Calendar.HOUR_OF_DAY) == 0) && */(calendar.get(Calendar.MINUTE) == 0) && (calendar.get(Calendar.SECOND) == 0) && (calendar.get(Calendar.MILLISECOND) == 0);
    }

    public void setLabelOnly(boolean labelOnly) {
        isLabelOnly = labelOnly;
    }

    public boolean isLabelOnly() {
        return isLabelOnly;
    }

    public static final Comparator<Zman> DATE_ORDER = (zman1, zman2) -> {
        if (zman1.isLabelOnly() || zman2.isLabelOnly()) return 0;
        // specials on top...
        if (zman1.isSpecial() && !zman2.isSpecial()) return -1;
        if (!zman1.isSpecial() && zman2.isSpecial()) return 1;
        // sort by date// sort by date - excluding labels
        if (zman1.getZman() != null && zman2.getZman() != null) {
            if (zman1.getZman().before(zman2.getZman())) return -1;
            else if (zman1.getZman().after(zman2.getZman())) return 1;
        }

        return 0;
    };

}