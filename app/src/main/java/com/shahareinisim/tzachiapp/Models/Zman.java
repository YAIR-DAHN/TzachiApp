package com.shahareinisim.tzachiapp.Models;

import java.util.Date;

public class Zman  extends com.kosherjava.zmanim.util.Zman {

    boolean special;
    public Zman(Date date, String label, boolean isSpecial) {
        super(date, label);
        special = isSpecial;
    }

    public boolean isSpecial() {
        return special;
    }

}