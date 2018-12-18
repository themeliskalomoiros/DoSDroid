package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.AttackConstants;

public final class AttackNetworkTypeTranslator {
    private static final String TAG = "AttackNetworkTypeTranslator";

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
