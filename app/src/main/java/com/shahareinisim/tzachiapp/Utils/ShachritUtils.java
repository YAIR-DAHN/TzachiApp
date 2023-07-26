package com.shahareinisim.tzachiapp.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

public class ShachritUtils {

    public static String getSongOfCurrentDay(InputStream sodFile) {

        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sodFile));

        return getSongFromFile(bufferedReader, currentDay);
    }

    public static String getSongFromFile(BufferedReader bufferedReader, int day) {
        String sod = "", dayKey = "[" + day + "]";
        try {
            String part;
            while ((part = bufferedReader.readLine()) != null) {
                String key = "";
                if (part.length() > 3) key = part.substring(0, 3);
                else if (part.equals("")) continue;
                if (key.equals(dayKey)) {
                    if (!sod.equals("")) sod += "\n";
                    sod += part.replace(dayKey, "");
                }
            }
        } catch (IOException ignored) {}

        if (sod.equals("")) return "Unable to determine day of week or it's Shabbat day";

        return sod;
    }

}
