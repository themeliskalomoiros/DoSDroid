package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import java.util.List;

public class ListUtils {

    public static boolean listHasItems(List list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }
}
