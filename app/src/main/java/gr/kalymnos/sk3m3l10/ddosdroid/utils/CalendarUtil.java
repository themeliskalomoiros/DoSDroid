package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.support.annotation.NonNull;

import java.util.Calendar;

public class CalendarUtil {
    @NonNull
    static Calendar from(int year, int month, int dayOfMonth) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return cal;
    }

    @NonNull
    static Calendar from(long timestamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal;
    }
}
