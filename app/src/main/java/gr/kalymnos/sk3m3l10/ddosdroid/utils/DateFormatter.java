package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.support.annotation.NonNull;

import java.util.Calendar;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.attack_creation.DatePicker;

public final class DateFormatter {

    private DateFormatter() {
    }

    public static String from(DatePicker.Date date) {
        return from(date.year, date.month, date.dayOfMonth);
    }

    public static String from(int year, int month, int dayOfMonth) {
        final Calendar cal = CalendarUtil.from(year, month, dayOfMonth);
        return from(cal);
    }

    public static String from(long timestamp) {
        final Calendar cal = CalendarUtil.from(timestamp);
        return from(cal);
    }

    @NonNull
    private static String from(Calendar cal) {
        return String.format("%d/%d/%d", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
    }
}
