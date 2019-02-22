package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public final class BundleUtil {

    private BundleUtil() {
    }

    public static boolean containsKey(Bundle bundle, String key) {
        if (bundle != null && bundle.containsKey(key)) {
            return true;
        }
        return false;
    }
}
