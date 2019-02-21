package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import java.util.Calendar;

public final class TimeFormatter {
    private TimeFormatter() {
    }

    public static String timeFrom(long timestamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
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

    private static String getValueWithZeroPrefixIfIsLessThanTen(int value) {
        if (value < 10) {
            return valueWithZeroPrefix(value);
        } else {
            return String.valueOf(value);
        }
    }

    private static String valueWithZeroPrefix(int value) {
        return String.valueOf("0" + value);
    }
}
