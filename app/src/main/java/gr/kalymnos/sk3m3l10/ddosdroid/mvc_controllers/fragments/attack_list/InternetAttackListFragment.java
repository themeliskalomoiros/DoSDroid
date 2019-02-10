package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;

public class InternetAttackListFragment extends AttackListFragment {
    private static final String TAG = AttackListFragment.TAG + "Internet";

    @Override
    public void onAttackUpload(Attack attack) {
        Log.d(TAG, "onAttackUpload()");
        if (attack.getNetworkType() == INTERNET) {
            cacheAttackAccordingToContentType(attack);
            displayAttacks();
        }
    }

    @Override
    public void onAttackUpdate(Attack changedAttack) {
        Log.d(TAG, "OnAttackUpdate()");
        if (changedAttack.getNetworkType() == INTERNET) {
            deleteFromCacheAttackWith(changedAttack.getPushId());
            cacheAttackAccordingToContentType(changedAttack);
            displayAttacks();
        }
    }
}
