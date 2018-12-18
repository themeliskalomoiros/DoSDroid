package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

public final class NetworkTypeTranslator {
    private static final String TAG = "NetworkTypeTranslator";

    public static String translate(int networkType) {
        switch (networkType) {
            case AttackConstants.NetworkType.BLUETOOTH:
                return "Bluetooth";
            case AttackConstants.NetworkType.INTERNET:
                return "Internet";
            case AttackConstants.NetworkType.WIFI_P2P:
                return "Wifi Peer to Peer";
            case AttackConstants.NetworkType.NSD:
                return "Network Service Discovery";
            default:
                throw new UnsupportedOperationException(TAG + ": no such network type");
        }
    }
}
