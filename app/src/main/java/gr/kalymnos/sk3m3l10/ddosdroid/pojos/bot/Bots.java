package gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot;

public final class Bots {

    private Bots() {
    }

    public static Bot local() {
        return new Bot(getLocalFirebaseInstanceId());
    }

    public static String localId() {
        return getLocalFirebaseInstanceId();
    }

    private static String getLocalFirebaseInstanceId() {
        return com.google.firebase.iid.FirebaseInstanceId.getInstance().getId();
    }
}
