package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.util.Log;

public class ThreadUtils {
    private static final String TAG = "ThreadUtils";

    public static void sleepOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error while sleeping", e);
        }
    }
}
