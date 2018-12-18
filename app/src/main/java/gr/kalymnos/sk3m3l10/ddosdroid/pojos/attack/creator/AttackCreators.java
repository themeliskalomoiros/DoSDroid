package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.instance_id.FirebaseInstanceId;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.instance_id.InstanceIdProvider;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.NSD;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.WIFI_P2P;

public final class AttackCreators {
    private static final String TAG = "AttackCreators";

    public static AttackCreator getLocalAttackCreatorInstance(int networkType) {
        InstanceIdProvider idProvider = new FirebaseInstanceId();
        String uuid = idProvider.getInstanceId();
        return new AttackCreatorFactoryImp().build(networkType, uuid);
    }

    public static Class<? extends AttackCreator> getClassFrom(int networkType) {
        switch (networkType) {
            case INTERNET:
                return InternetCreator.class;
            case BLUETOOTH:
                return BluetoothCreator.class;
            case WIFI_P2P:
                return WifiP2pCreator.class;
            case NSD:
                return NsdCreator.class;
            default:
                throw new IllegalArgumentException(TAG + ": Unknown attack network type");
        }
    }
}
