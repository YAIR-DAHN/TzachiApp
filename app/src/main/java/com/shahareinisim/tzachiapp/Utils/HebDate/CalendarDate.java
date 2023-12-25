package com.shahareinisim.tzachiapp.Utils.HebDate;

import java.util.Calendar;

public class CalendarDate {
    CalendarDate(int day, int month, int year) {
        this.day = day; this.month = month; this.year = year;
    }
    CalendarDate(CalendarDate date) {
        this.day = date.getDay(); this.month = date.getMonth(); this.year = date.getYear();
    }

    public CalendarDate() {
        Calendar cal = Calendar.getInstance();
        this.day = cal.get(Calendar.DAY_OF_MONTH); this.month = cal.get(Calendar.MONTH); this.year = cal.get(Calendar.YEAR);
    }

    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }

    public void setDay(int day) { this.day = day; }
    public void setMonth(int month) { this.month = month; }
    public void setYear(int year) { this.year = year; }

    public boolean areDatesEqual(CalendarDate date) {
        if ((day == date.getDay()) &&
                (month == date.getMonth()) &&
                (year == date.getYear()))
            return true;
        else
            return false;
    }

    public int getHashCode() {
        return (year - 1583) * 366 + month * 31 + day;
    }

    public String toString() {
        return day + "." + month + "." + year;
    }

    private int day;
    private int month;
    private int year;
}
