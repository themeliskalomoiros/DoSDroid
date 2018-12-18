package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

public final class NetworkTypeTranslator {
    private static final String TAG = "NetworkTypeTranslator";

    public static String translate(int networkType) {
        switch (networkType) {
            case Constants.NetworkType.BLUETOOTH:
                return "Bluetooth";
            case Constants.NetworkType.INTERNET:
                return "Internet";
            case Constants.NetworkType.WIFI_P2P:
                return "Wifi Peer to Peer";
            case Constants.NetworkType.NSD:
                return "Network Service Discovery";
            default:
                throw new UnsupportedOperationException(TAG + ": no such network type");
        }
    }
}
