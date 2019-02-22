package gr.kalymnos.sk3m3l10.ddosdroid.utils.date_time;

import java.util.Calendar;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.attack_creation.TimePicker;

public final class TimeFormatter {

    private TimeFormatter() {
    }

    public static String from(TimePicker.Time time) {
        return from(time.hourOfDay, time.minute);
    }

    public static String from(int hour, int minute) {
        return getStringOf(hour) + ":" + getStringOf(minute);
    }

    public static String from(long timestamp) {
        final Calendar cal = CalendarUtil.from(timestamp);
        return stringHourFrom(cal) + ":" + stringMinuteFrom(cal);
    }

    private static String stringHourFrom(Calendar cal) {
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return getStringOf(hour);
    }

    private static String stringMinuteFrom(Calendar cal) {
        int minutes = cal.get(Calendar.MINUTE);
        return getStringOf(minutes);
    }

    private static String getStringOf(int value) {
        if (value < 10) {
            return addZeroPrefixTo(value);
        } else {
            return String.valueOf(value);
        }
    }

    private static String addZeroPrefixTo(int value) {
        return String.valueOf("0" + value);
    }
}
