package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class CollectionUtils {

    public static boolean hasItems(Collection collection) {
        if (collection != null && collection.size() > 0) {
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
