package gr.kalymnos.sk3m3l10.ddosdroid.constants;

public final class ContentTypes {
    public static final int FETCH_ONLY_USER_JOINED_ATTACKS = 102;
    public static final int FETCH_ONLY_USER_NOT_JOINED_ATTACKS = 103;
    public static final int FETCH_ONLY_USER_OWN_ATTACKS = 104;
    public static final int INVALID_CONTENT_TYPE = -1;

    private ContentTypes() {
    }

    public static boolean joinedAttacks(int contentType) {
        return contentType == FETCH_ONLY_USER_JOINED_ATTACKS;
    }

    public static boolean notJoinedAttacks(int contentType) {
        return contentType == FETCH_ONLY_USER_NOT_JOINED_ATTACKS;
    }

    public static boolean ownAttacks(int contentType) {
        return contentType == FETCH_ONLY_USER_OWN_ATTACKS;
    }
}
