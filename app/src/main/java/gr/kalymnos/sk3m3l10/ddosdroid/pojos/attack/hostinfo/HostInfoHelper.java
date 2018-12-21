package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.instance_id.FirebaseInstanceId;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.instance_id.InstanceIdProvider;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.WIFI_P2P;

public final class HostInfoHelper {
    private static final String TAG = "HostInfoHelper";

    public static HostInfo getLocalHostInfo(int networkType) {
        InstanceIdProvider idProvider = new FirebaseInstanceId();
        String uuid = idProvider.getInstanceId();
        return new HostInfoFactoryImp().build(networkType, uuid);
    }

    public static Class<? extends HostInfo> getClassFrom(int networkType) {
        switch (networkType) {
            case INTERNET:
                return InternetHostInfo.class;
            case BLUETOOTH:
                return BluetoothHostInfo.class;
            case WIFI_P2P:
                return WifiP2pHostInfo.class;
            case NSD:
                return NsdHostInfo.class;
            default:
                throw new IllegalArgumentException(TAG + ": Unknown attack network type");
        }
    }
}
