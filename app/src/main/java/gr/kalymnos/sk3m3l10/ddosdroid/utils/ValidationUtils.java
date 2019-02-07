package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.os.Bundle;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public final class ValidationUtils {

    public static boolean collectionHasItems(Collection collection) {
        if (collection != null && collection.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean bundleContainsKey(Bundle bundle, String key) {
        if (bundle != null && bundle.containsKey(key)) {
            return true;
        }
        return false;
    }

    public static <T> T getItemFromLinkedHashSet(LinkedHashSet<T> set, int index) {
        if (index < 0 || index >= set.size())
            throw new IllegalArgumentException("Invalid index: " + index + " while set's size is " + set.size() + ".");

        T item = null;
        int i = 0;
        for (Iterator<T> iterator = set.iterator(); iterator.hasNext(); i++) {
            item = iterator.next();
            if (i == index)
                break;
        }
        return item;
    }
}
