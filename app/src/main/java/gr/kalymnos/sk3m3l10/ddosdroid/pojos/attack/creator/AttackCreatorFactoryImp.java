package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.WIFI_P2P;

class AttackCreatorFactoryImp implements AttackCreatorFactory {
    private static final String TAG = "AttackCreatorFactoryImp";

    @Override
    public AttackCreator build(int networkType, String uuid) {
        switch (networkType) {
            case INTERNET:
                return new InternetCreator(uuid);
            case BLUETOOTH:
                return new BluetoothCreator(uuid);
            case WIFI_P2P:
                return new WifiP2pCreator(uuid);
            case NSD:
                return new NsdCreator(uuid);
            default:
                throw new UnsupportedOperationException(TAG + ": Unknown network type");
        }
    }
}
