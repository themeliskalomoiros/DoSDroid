package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.os.Bundle;

import java.util.List;
import java.util.Map;

public class ValidationUtils {

    public static boolean listHasItems(List list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean mapHasItems(Map map) {
        if (map != null && map.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean bundleIsValidAndContainsKey(Bundle bundle, String key) {
        if (bundle != null && bundle.containsKey(key)) {
            return true;
        }
        return false;
    }
}
