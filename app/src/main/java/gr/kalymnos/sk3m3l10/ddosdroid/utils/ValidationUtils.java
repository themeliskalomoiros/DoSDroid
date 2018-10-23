package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.os.Bundle;

import java.util.List;

public class ValidationUtils {

    public static boolean listHasItems(List list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean bundleContainsKey(Bundle bundle, String key) {
        if (bundle.containsKey(key)) {
            return true;
        }
        return false;
    }
}
