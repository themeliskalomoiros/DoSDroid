package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.support.annotation.NonNull;

import java.util.Calendar;

public final class DateFormatter {

    private DateFormatter() {
    }

    public static String dateFrom(long timestamp) {
        final Calendar cal = calendarFrom(timestamp);
        return dateFrom(cal);
    }

    @NonNull
    private static Calendar calendarFrom(long timestamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal;
    }

    public static String dateFrom(int year, int month, int dayOfMonth) {
        final Calendar cal = calendarFrom(year, month, dayOfMonth);
        return dateFrom(cal);
    }

    @NonNull
    private static Calendar calendarFrom(int year, int month, int dayOfMonth) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return cal;
    }

    @NonNull
    private static String dateFrom(Calendar cal) {
        return String.format("%d/%d/%d", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
    }

    public static String timeFrom(long timestamp) {
        final Calendar cal = calendarFrom(timestamp);
        return getHour(cal) + ":" + getMinutes(cal);
    }

    private static String getHour(Calendar cal) {
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return getValueWithZeroPrefixIfIsLessThanTen(hour);
    }

    private static String getMinutes(Calendar cal) {
        int minutes = cal.get(Calendar.MINUTE);
        return getValueWithZeroPrefixIfIsLessThanTen(minutes);
    }

    private static String getValueWithZeroPrefixIfIsLessThanTen(int minutes) {
        if (minutes < 10) {
            return valueWithZeroPrefix(minutes);
        } else {
            return String.valueOf(minutes);
        }
    }

    private static String valueWithZeroPrefix(int value) {
        return String.valueOf("0" + value);
    }
}
