package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.WIFI_P2P;

public class WiFiP2PAttackListFragment extends AttackListFragment {
    private static final String TAG = AttackListFragment.TAG + "WiFiP2P";

    @Override
    public void onAttackUpload(Attack attack) {
        if (attack.getNetworkType() == WIFI_P2P) {
            cacheAttackAccordingToContentType(attack);
            bindAttacks();
        }
    }

    @Override
    public void onAttackUpdate(Attack changedAttack) {
        if (changedAttack.getNetworkType() == WIFI_P2P) {
            deleteFromCacheAttackWith(changedAttack.getPushId());
            cacheAttackAccordingToContentType(changedAttack);
            bindAttacks();
        }
    }
}
