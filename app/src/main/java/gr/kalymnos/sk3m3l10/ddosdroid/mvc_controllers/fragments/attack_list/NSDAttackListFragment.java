package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.NSD;

public class NSDAttackListFragment extends AttackListFragment {

    @Override
    public void onAttackUpload(Attack attack) {
        if (attack.getNetworkType() == NSD) {
            cacheAttackAccordingToContentType(attack);
            viewMvc.bindAttacks(cachedAttacks);
        }
    }

    @Override
    public void onAttackUpdate(Attack attack) {
        if (attack.getNetworkType() == NSD) {
            cachedAttacks.remove(attack);
            cacheAttackAccordingToContentType(attack);
            viewMvc.bindAttacks(cachedAttacks);
        }
    }
}
