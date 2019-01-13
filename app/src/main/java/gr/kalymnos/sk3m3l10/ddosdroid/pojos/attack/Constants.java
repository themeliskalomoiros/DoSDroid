package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

public interface Constants {
    String TAG = "Constants";

    interface AttackType {
        int TYPE_FETCH_ALL = 101;
        int TYPE_FETCH_JOINED = 102;
        int TYPE_FETCH_NOT_JOINED = 103;
        int TYPE_FETCH_OWNER = 104;
        int TYPE_NONE = -1;
    }

    interface NetworkType {
        int INTERNET = 0;
        int WIFI_P2P = 1;
        int NSD = 2;
        int BLUETOOTH = 3;
    }

    interface Extra {
        String EXTRA_ATTACK = TAG + "extra attacks";
        String EXTRA_ATTACKS = TAG + "caching attacks key";
        String EXTRA_TYPE = TAG + "attack type key";
        String EXTRA_UUID = TAG + "extra uuid";
        String EXTRA_SERVICE_NAME = TAG + "extra service name";
        String EXTRA_SERVICE_TYPE = TAG + "extra service type";
    }
}
