package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ContentType.FETCH_ALL_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ContentType.FETCH_ONLY_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ContentType.FETCH_ONLY_NOT_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ContentType.FETCH_ONLY_OWNER_ATTACKS;

public final class LoggingUtils {

    public static String getAttackTypeName(int type) {
        switch (type) {
            case FETCH_ALL_ATTACKS:
                return "FETCH_ALL_ATTACKS";
            case FETCH_ONLY_JOINED_ATTACKS:
                return "FETCH_ONLY_JOINED_ATTACKS";
            case FETCH_ONLY_NOT_JOINED_ATTACKS:
                return "FETCH_ONLY_NOT_JOINED_ATTACKS";
            case FETCH_ONLY_OWNER_ATTACKS:
                return "FETCH_ONLY_OWNER_ATTACKS";
            default:
                return "INVALID_CONTENT_TYPE";
        }
    }
}
