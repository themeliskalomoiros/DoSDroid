package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.NetworkTypes.INTERNET;

public class InternetAttackListFragment extends AttackListFragment {
    private static final String TAG = AttackListFragment.TAG + "Internet";

    @Override
    public void onAttackUpload(Attack attack) {
        if (attack.getNetworkType() == INTERNET) {
            cacheAttackAccordingToContentType(attack);
            viewMvc.bindAttacks(cachedAttacks);
        }
    }

    @Override
    public void onAttackUpdate(Attack attack) {
        if (attack.getNetworkType() == INTERNET) {
            cachedAttacks.remove(attack);
            cacheAttackAccordingToContentType(attack);
            viewMvc.bindAttacks(cachedAttacks);
        }
    }
}
