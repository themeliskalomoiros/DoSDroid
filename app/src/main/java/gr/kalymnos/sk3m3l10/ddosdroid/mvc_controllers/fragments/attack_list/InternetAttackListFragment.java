package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_ALL;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_FOLLOWING;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_NOT_FOLLOWING;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_OWNER;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.NetworkType.INTERNET;

public class InternetAttackListFragment extends AttackListFragment {

    private static final String TAG = "InternetAttackListFragm";

    @Override
    protected void startFetchingAttacks() {
        switch (getAttacksType(getArguments())) {
            case TYPE_FETCH_ALL:
                attackRepo.fetchAllAttacks(INTERNET);
                break;
            case TYPE_FETCH_FOLLOWING:
                //  TODO: when the fake attack repo is removed replace "bot3" argument with userId variable
                //  String userId = DDoSBot.getLocalUserDDoSBot().getId();
                attackRepo.fetchFollowingAttakcs("bot3", INTERNET);
                break;
            case TYPE_FETCH_OWNER:
                attackRepo.fetchOwnerAttacks(INTERNET);
                break;
            case TYPE_FETCH_NOT_FOLLOWING:
                attackRepo.fetchNotFollowingAttacks("bot3", INTERNET);
                break;
            default:
                throw new UnsupportedOperationException(TAG + ": Type of attacks to fetch not specified");
        }
    }
}
