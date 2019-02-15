package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

public interface Constants {
    //  Search for "Android E/Parcel: Class not found when unmarshalling (only Samsung Tab3) on StackOverflow"
    String BUNDLE_SAMSUNG_BUG_KEY = "bundle_bug_key_for_samsung_phones";

    interface ContentType {
        int FETCH_ONLY_USER_JOINED_ATTACKS = 102;
        int FETCH_ONLY_USER_NOT_JOINED_ATTACKS = 103;
        int FETCH_ONLY_USER_OWN_ATTACKS = 104;
        int INVALID_CONTENT_TYPE = -1;
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
        String EXTRA_ATTACK_STARTED = "extra_attack_started";
        String EXTRA_ATTACK_HOST_UUID = "extra_attack_host_uuid";
        String EXTRA_DEVICE_NAME = "extra_device_name";
        String EXTRA_SERVICE_NAME = "extra_service_name";
        String EXTRA_SERVICE_TYPE = "extra_service_type";
        String EXTRA_CONTENT_TYPE = "extra_attack type_key";
        String EXTRA_LOCAL_PORT = "extra_local_port";
        String EXTRA_MAC_ADDRESS = "extra_mac_address";
    }
}
