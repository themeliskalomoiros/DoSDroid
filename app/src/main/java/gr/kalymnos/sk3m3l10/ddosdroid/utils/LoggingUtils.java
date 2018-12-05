package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack.AttackType.TYPE_FETCH_ALL;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack.AttackType.TYPE_FETCH_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack.AttackType.TYPE_FETCH_NOT_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack.AttackType.TYPE_FETCH_OWNER;

public class LoggingUtils {

    public static String getAttackTypeName(int type) {
        switch (type) {
            case TYPE_FETCH_ALL:
                return "TYPE_FETCH_ALL";
            case TYPE_FETCH_JOINED:
                return "TYPE_FETCH_JOINED";
            case TYPE_FETCH_NOT_JOINED:
                return "TYPE_FETCH_NOT_JOINED";
            case TYPE_FETCH_OWNER:
                return "TYPE_FETCH_OWNER";
            default:
                return "TYPE_NONE";
        }
    }
}
