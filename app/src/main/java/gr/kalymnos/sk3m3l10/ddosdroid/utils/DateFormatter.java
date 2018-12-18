package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.content.res.Configuration;
import android.support.v4.os.ConfigurationCompat;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateFormatter {

    public static String getDate(Configuration configuration, long timeMillis) {
        Date today = new Date(timeMillis);
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, getCurrentLocale(configuration));
        return dateFormatter.format(today);
    }

    private static Locale getCurrentLocale(Configuration configuration){
        return ConfigurationCompat.getLocales(configuration).get(0);
    }
}
