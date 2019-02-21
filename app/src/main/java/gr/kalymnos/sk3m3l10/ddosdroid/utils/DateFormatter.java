package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.support.annotation.NonNull;

import java.util.Calendar;

public final class DateFormatter {

    private DateFormatter() {
    }

    public static String dateFrom(long timestamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return dateFrom(cal);
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
}
