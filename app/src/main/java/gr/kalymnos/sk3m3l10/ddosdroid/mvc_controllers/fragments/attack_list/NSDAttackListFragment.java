package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Bot;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.AttackType.TYPE_FETCH_ALL;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.AttackType.TYPE_FETCH_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.AttackType.TYPE_FETCH_NOT_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.AttackType.TYPE_FETCH_OWNER;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants.NetworkType.NSD;

public class NSDAttackListFragment extends AttackListFragment {

    private static final String TAG = "NSDAttackListFragment";

    @Override
    protected void fetchAttacksAccordingToType() {
        switch (getAttacksType(getArguments())) {
            case TYPE_FETCH_ALL:
                attackRepo.fetchAllAttacksOf(NSD);
                break;
            case TYPE_FETCH_JOINED:
                //  TODO: when the fake attack repo is removed replace "bot3" argument with userId variable
                //  String userId = Bot.getLocalUserDDoSBot().getId();
                attackRepo.fetchJoinedAttakcsOf(Bot.getLocalUserDDoSBot().getId(), NSD);
                break;
            case TYPE_FETCH_OWNER:
                attackRepo.fetchLocalOwnerAttacksOf(NSD);
                break;
            case TYPE_FETCH_NOT_JOINED:
                attackRepo.fetchNotJoinedAttacksOf(Bot.getLocalUserDDoSBot().getId(), NSD);
                break;
            default:
                throw new UnsupportedOperationException(TAG + ": Type of attacks to fetch not specified");
        }
    }
}
