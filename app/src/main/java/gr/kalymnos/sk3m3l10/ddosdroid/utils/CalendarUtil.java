package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.support.annotation.NonNull;

import java.util.Calendar;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.attack_creation.DatePicker;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.attack_creation.TimePicker;

public class CalendarUtil {
    @NonNull
    public static Calendar from(int year, int month, int dayOfMonth) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return cal;
    }

    @NonNull
    public static Calendar from(long timestamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal;
    }

    public static Calendar from(DatePicker.Date date, TimePicker.Time time) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, date.year);
        cal.set(Calendar.MONTH, date.month);
        cal.set(Calendar.DAY_OF_MONTH, date.dayOfMonth);
        cal.set(Calendar.HOUR_OF_DAY, time.hourOfDay);
        cal.set(Calendar.MINUTE, time.minute);
        return cal;
    }
}
