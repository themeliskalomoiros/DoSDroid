package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

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

    private static final String TAG = "BluetoothAttackListFrag";

    @Override
    protected void fetchAttacksAccordingToType() {
        switch (getAttacksType(getArguments())) {
            case TYPE_FETCH_ALL:
                attackRepo.fetchAllAttacksOf(BLUETOOTH);
                break;
            case TYPE_FETCH_JOINED:
                attackRepo.fetchJoinedAttakcsOf(Bots.getLocalUser().getId(), BLUETOOTH);
                break;
            case TYPE_FETCH_OWNER:
                attackRepo.fetchLocalOwnerAttacksOf(BLUETOOTH);
                break;
            case TYPE_FETCH_NOT_JOINED:
                attackRepo.fetchNotJoinedAttacksOf(Bots.getLocalUser().getId(), BLUETOOTH);
                break;
            default:
                throw new UnsupportedOperationException(TAG + ": Type of attacks to fetch not specified");
        }
    }

    @Override
    public void onAttackAddedToRepository(Attack attack) {
        boolean isNotJoinedAttackListFragment = getAttacksType(getArguments()) == TYPE_FETCH_NOT_JOINED;
        boolean isBluetoothAttack = attack.getNetworkType() == BLUETOOTH;
        if (isNotJoinedAttackListFragment && isBluetoothAttack) {
            cachedAttacks.add(attack);
            viewMvc.bindAttacks(cachedAttacks);
        }
    }

    @Override
    public void onAttackChangedInRepository(Attack changedAttack) {
        if (changedAttack.getNetworkType() == BLUETOOTH) {
            int attacksType = getAttacksType(getArguments());
            boolean shouldDisplayAttackForTypeFetchJoined = attacksType == TYPE_FETCH_JOINED && Attacks.includes(changedAttack, Bots.getLocalUser());
            boolean shouldDisplayAttackForTypeFetchNotJoined = attacksType == TYPE_FETCH_NOT_JOINED && !Attacks.includes(changedAttack, Bots.getLocalUser());

            if (shouldDisplayAttackForTypeFetchJoined || shouldDisplayAttackForTypeFetchNotJoined) {
                displayChangedAttack(changedAttack);
            }
        }
    }

    private void displayChangedAttack(Attack changedAttack) {
        deleteAttackWithSameIdAs(changedAttack);
        cachedAttacks.add(changedAttack);
        viewMvc.bindAttacks(cachedAttacks);
    }

    private void deleteAttackWithSameIdAs(Attack attack) {
        Iterator<Attack> iterator = cachedAttacks.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getPushId().equals(attack.getPushId())) {
                iterator.remove();
                break;
            }
        }
    }
}
