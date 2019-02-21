package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.content.res.Configuration;
import android.support.v4.os.ConfigurationCompat;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateFormatter {

    private DateFormatter() {
    }

    public static String dateFrom(long timeMillis, Configuration configuration) {
        Date date = new Date(timeMillis);
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.DEFAULT, getCurrentLocale(configuration));
        return formatter.format(date);
    }

    private static Locale getCurrentLocale(Configuration configuration) {
        return ConfigurationCompat.getLocales(configuration).get(0);
    }
}
