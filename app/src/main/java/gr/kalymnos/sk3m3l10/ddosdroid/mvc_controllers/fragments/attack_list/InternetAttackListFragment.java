package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_ALL;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_NOT_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_OWNER;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.NetworkType.INTERNET;

public class InternetAttackListFragment extends AttackListFragment {

    private static final String TAG = "InternetAttackListFragm";

    @Override
    protected void fetchAttacksAccordingToType() {
        switch (getAttacksType(getArguments())) {
            case TYPE_FETCH_ALL:
                attackRepo.fetchAllAttacksOf(INTERNET);
                break;
            case TYPE_FETCH_JOINED:
                attackRepo.fetchJoinedAttakcsOf(Bots.getLocalUser().getUuid(), INTERNET);
                break;
            case TYPE_FETCH_OWNER:
                attackRepo.fetchLocalOwnerAttacksOf(INTERNET);
                break;
            case TYPE_FETCH_NOT_JOINED:
                attackRepo.fetchNotJoinedAttacksOf(Bots.getLocalUser().getUuid(), INTERNET);
                break;
            default:
                throw new UnsupportedOperationException(TAG + ": Type of attacks to fetch not specified. Type is " + getAttacksType(getArguments()));
        }
    }
}
