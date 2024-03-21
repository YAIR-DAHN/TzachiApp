package com.shahareinisim.tzachiapp.Utils;

import android.util.Log;

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
        StringBuilder sod = new StringBuilder();
        String dayKey = "[" + day + "]";
        try {
            String part;
            while ((part = bufferedReader.readLine()) != null) {
                String key = "";
                if (part.length() > 3) key = part.substring(0, 3);
                else if (part.isEmpty()) continue;
                if (key.equals(dayKey)) {
                    if (sod.length() != 0) sod.append("\n\n");
                    sod.append(part.replace(dayKey, ""));
                }
                Log.d("TAG", "getSongFromFile: " + part);
            }
        } catch (IOException ignored) {}

        if (sod.length() == 0) return "Unable to determine day of week or it's Shabbat day";

        return sod.toString();
    }

    public static String getMegilatEster(InputStream megilatEsterFile) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(megilatEsterFile));
        StringBuilder megilatEster = new StringBuilder();
        try {
            String part;
            while ((part = bufferedReader.readLine()) != null) {
                if (megilatEster.length() != 0) megilatEster.append("\n\n");
                megilatEster.append(part);
            }
        } catch (IOException ignored) {}

        if (megilatEster.length() == 0) return "Unable to read Megilat Ester";

        return megilatEster.toString();
    }

}
