package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

import gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes;

public final class NetworkTypeTranslator {
    private static final String TAG = "NetworkTypeTranslator";

    private NetworkTypeTranslator() {
    }

    public static String translate(int networkType) {
        switch (networkType) {
            case NetworkTypes.BLUETOOTH:
                return "Bluetooth";
            case NetworkTypes.INTERNET:
                return "Internet";
            case NetworkTypes.WIFI_P2P:
                return "Wifi Peer to Peer";
            case NetworkTypes.NSD:
                return "Network Service Discovery";
            default:
                throw new UnsupportedOperationException(TAG + ": no such network type");
        }
    }
}
