package com.shahareinisim.tzachiapp.Utils;

public class SfiratAhomer {

    public static String[] numberOfDays = {"אֶחָד", "שְׁנֵי", "שְׁלֹשָׁה", "אַרְבָּעָה", "חֲמִשָּׁה", "שִׁשָּׁה", "שִׁבְעָה", "שְׁמוֹנָה", "תִּשְׁעָה"};
    public static String[] numberOfDaysTens = {"עֲשָׂרָה", "עֶשְׂרִים", "שְׁלֹשִׁים", "אַרְבָּעִים"};

    public static final String TENS_REPLACEMENT = "עָשָׂר";
    public static final String TWO_REPLACEMENT = "שְׁנַיִם";
    public static final String NusachSfiratAhomer = "הַיּוֹם %s %s לָעֹֽמֶר";
    public static final String Extansion = ", שֶׁהֵם %s";
    public static final String DAY = "יוֹם";
    public static final String DAYS = "יָמִים";
    public static final String WEEK = "שָׁבֽוּעַ";
    public static final String WEEKS = "שָׁבוּעוֹת";
    public static final String CONNECTOR = " וְ", SECOND_CONNECTOR = " וּ", THIRD_CONNECTOR = " וֵ", SPACE = " ";

    public static String getDay(int day) {
        if (day < 1) return "null";

        if (day <= 9) {
            return numberOfDays[day-1];
        } else if (day % 10 == 0) {
            return numberOfDaysTens[(day / 10)-1];
        } else {
            int days = day % 10;
            int tens = (day-days) / 10;

            String daysString = numberOfDays[days-1];
            String tensString = numberOfDaysTens[tens-1];

            if (tens == 1) {
                tensString = SPACE + TENS_REPLACEMENT;
            } else {
                tensString = getConnector(tens*10) + tensString;
            }

            if (days == 2) {
                if (tens == 1) daysString += "ם";
                else daysString = TWO_REPLACEMENT;
            }

            return daysString + tensString;
        }
    }

    public static String getSfiratAhomer(int days) {
        return getSfiratAhomer(days, true);
    }

    public static String getSfiratAhomer(int days, boolean withExtension) {

        if (days == 1) {
            return String.format(NusachSfiratAhomer, DAY, getDay(days));
        }
        return String.format(NusachSfiratAhomer, getDay(days), (days > 10 ? DAY : DAYS)) + (days >= 7 && withExtension ? getExtension(days) : "");
    }

    public static String getExtension(int days) {
        String weeks = getWeeks(days);
        return String.format(Extansion, weeks) + getDays(days);
    }

    public static String getWeeks(int days) {
        int weeks = days / 7;

        if (weeks == 0) return "";

        if (weeks == 1) {
            return WEEK + SPACE + numberOfDays[weeks-1];
        }
        return numberOfDays[weeks-1] + SPACE + WEEKS;
    }

    public static String getDays(int days) {
        int day = days % 7;

        if (day == 0) return "";

        if (day == 1) {
            return CONNECTOR + DAY + SPACE + numberOfDays[day-1];
        }

        return getConnector(day) + numberOfDays[day-1] + SPACE + DAYS;
    }

    public static String getConnector(int days) {
        switch (days) {
            case 2:
            case 3:
            case 30:
                return SECOND_CONNECTOR;
            case 5:
                return THIRD_CONNECTOR;
            default:
                return CONNECTOR;
        }
    }
}
