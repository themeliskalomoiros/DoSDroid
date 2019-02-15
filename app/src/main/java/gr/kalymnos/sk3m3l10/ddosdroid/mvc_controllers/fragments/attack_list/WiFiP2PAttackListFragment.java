package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.WIFI_P2P;

public class WiFiP2PAttackListFragment extends AttackListFragment {

    @Override
    public void onAttackUpload(Attack uploadedAttack) {
        if (uploadedAttack.getNetworkType() == WIFI_P2P) {
            cacheAttackAccordingToContentType(uploadedAttack);
            viewMvc.bindAttacks(cachedAttacks);
        }
    }

    @Override
    public void onAttackUpdate(Attack changedAttack) {
        if (changedAttack.getNetworkType() == WIFI_P2P) {
            cachedAttacks.remove(changedAttack);
            cacheAttackAccordingToContentType(changedAttack);
            viewMvc.bindAttacks(cachedAttacks);
        }
    }
}
