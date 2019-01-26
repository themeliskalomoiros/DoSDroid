package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

public interface Constants {
    int ATTACK_STARTED = 1010;

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
        String EXTRA_ATTACK = "extra_attacks";
        String EXTRA_ATTACKS = "extra_attacks_key";
        String EXTRA_TYPE = "extra_attack type_key";
        String EXTRA_ATTACK_HOST_UUID = "extra_attack_host_uuid";
        String EXTRA_SERVICE_NAME = "extra_service_name";
        String EXTRA_SERVICE_TYPE = "extra_service_type";
        String EXTRA_DEVICE_NAME = "extra_device_name";
        String EXTRA_DEVICE_ADDRESS = "extra_device_address";
        String EXTRA_ATTACK_STARTED = "extra_attack_started";
        String EXTRA_LOCAL_PORT = "extra_local_port";
    }
}
