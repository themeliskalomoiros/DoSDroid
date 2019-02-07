package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;

public class BluetoothAttackListFragment extends AttackListFragment {
    private static final String TAG = AttackListFragment.TAG + "Bluetooth";

    @Override
    public void onAttackUpload(Attack attack) {
        if (attack.getNetworkType() == BLUETOOTH) {
            cacheAttackAccordingToContentType(attack);
            bindAttacks();
        }
    }

    @Override
    public void onAttackUpdate(Attack changedAttack) {
        if (changedAttack.getNetworkType() == BLUETOOTH) {
            deleteFromCacheAttackWith(changedAttack.getPushId());
            cacheAttackAccordingToContentType(changedAttack);
            bindAttacks();
        }
    }
}
