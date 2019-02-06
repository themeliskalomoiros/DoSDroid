package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import android.util.Log;

import java.util.Iterator;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_ALL;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_NOT_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_OWNER;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.BLUETOOTH;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;

public class BluetoothAttackListFragment extends AttackListFragment {
    private static final String TAG = AttackListFragment.TAG + "Bluetooth";

    @Override
    public void onAttackUpload(Attack attack) {
        if (attack.getNetworkType() == BLUETOOTH) {
            cacheAttackAndBind(attack);
        }
    }

    @Override
    public void onAttackUpdate(Attack changedAttack) {
        if (changedAttack.getNetworkType() == BLUETOOTH) {
            deleteFromCacheAttackWith(changedAttack.getPushId());
            cacheAttackAndBind(changedAttack);
        }
    }

    @Override
    public void onAttackDelete(Attack deletedAttack) {
        if (deletedAttack.getNetworkType() == BLUETOOTH) {
            deleteFromCacheAttackWith(deletedAttack.getPushId());
            bindAttacks();
        }
    }
}
