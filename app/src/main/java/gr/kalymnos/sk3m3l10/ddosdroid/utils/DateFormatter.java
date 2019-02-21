package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import java.util.Calendar;

public final class DateFormatter {

    private DateFormatter() {
    }

    public static String dateFrom(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return String.format("%d/%d/%d", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
    }

    public static String timeFrom(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return getHour(cal) + ":" + cal.get(Calendar.MINUTE);
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
