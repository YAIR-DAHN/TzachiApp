package com.shahareinisim.tzachiapp.Utils;

public class ShachritUtils() {

  public static String getSongOfCurrentDay() {
      
      return songOfDay(dayCheck())
  }

  public static String songOfDay(int day) {
        switch (day) {
            case 1:
                return "";
                break;
            case 2:
                return "";
                break;
            case 3:
                return "";
                break;
            case 4:
                return "";
                break;
            case 5:
                return "";
                break;
            case 6:
                return "";
                break;
          default:
                return "";
                break;
        }
  }

  public static int dayCheck() {
    // TODO get current day of week
    return 0;
  }

}
