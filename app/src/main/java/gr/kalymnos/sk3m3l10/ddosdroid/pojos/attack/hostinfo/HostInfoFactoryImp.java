package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.WIFI_P2P;

class HostInfoFactoryImp implements HostInfoFactory {
    private static final String TAG = "HostInfoFactoryImp";

    @Override
    public HostInfo build(int networkType, String uuid) {
        switch (networkType) {
            case INTERNET:
                return new InternetHostInfo(uuid);
            case BLUETOOTH:
                return new BluetoothHostInfo(uuid);
            case WIFI_P2P:
                return new WifiP2pHostInfo(uuid);
            case NSD:
                return new NsdHostInfo(uuid);
            default:
                throw new UnsupportedOperationException(TAG + ": Unknown network type");
        }
    }
}
