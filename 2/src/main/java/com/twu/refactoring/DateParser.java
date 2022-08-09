package com.twu.refactoring;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class DateParser {
    private final String dateAndTimeString;
    private static final HashMap<String, TimeZone> KNOWN_TIME_ZONES = new HashMap<>();

    public static final String UTC = "UTC";

    static {
        KNOWN_TIME_ZONES.put(UTC, TimeZone.getTimeZone(UTC));
    }

    /**
     * Takes a date in ISO 8601 format and returns a date
     *
     * @param dateAndTimeString - should be in format ISO 8601 format
     *                          examples -
     *                          2012-06-17 is 17th June 2012 - 00:00 in UTC TimeZone
     *                          2012-06-17TZ is 17th June 2012 - 00:00 in UTC TimeZone
     *                          2012-06-17T15:00Z is 17th June 2012 - 15:00 in UTC TimeZone
     */
    public DateParser(String dateAndTimeString) {
        this.dateAndTimeString = dateAndTimeString;
    }

    public Date parse() {
        int year;
        int month;
        int date;
        int hour;
        int minute;

        year = getDateFromTimeString(0,4,"Year string is less than 4 characters","Year is not an integer",2000,2012,"Year cannot be less than 2000 or more than 2012");

        month = getDateFromTimeString(5, 7, "Month string is less than 2 characters", "Month is not an integer", 1, 12, "Month cannot be less than 1 or more than 12");

        date = getDateFromTimeString(8, 10, "Date string is less than 2 characters", "Date is not an integer", 1, 31, "Date cannot be less than 1 or more than 31");

        if (dateAndTimeString.charAt(11) == 'Z') {
            hour = 0;
            minute = 0;
        } else {
            hour = getDateFromTimeString(11, 13, "Hour string is less than 2 characters", "Hour is not an integer", 0, 23, "Hour cannot be less than 0 or more than 23");

            minute = getDateFromTimeString(14, 16, "Minute string is less than 2 characters", "Minute is not an integer", 0, 59, "Minute cannot be less than 0 or more than 59");

        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(year, month - 1, date, hour, minute, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private int getDateFromTimeString(int beginIndex, int endIndex, String s, String errorAlert, int minTime, int maxTime, String illegalArgumentMessage) {
        int result;
        try {
            String monthString = dateAndTimeString.substring(beginIndex, endIndex);
            result = Integer.parseInt(monthString);
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorAlert);
        }
        checkDateIfLegal(result, minTime, maxTime, illegalArgumentMessage);
        return result;
    }

    private static void checkDateIfLegal(int date, int minDate, int maxDate, String errorMessage) {
        if (date < minDate || date > maxDate)
            throw new IllegalArgumentException(errorMessage);
    }

}
