package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;


/*
 * This repository stores all the DDoS attack data
 * */

public interface AttackRepository {

    interface OnAttacksFetchListener {
        void attacksFetchedSuccess(List<DDoSAttack> attacks);

        void attacksFetchedFail(List<DDoSAttack> attacks);
    }

    void fetchAllAttacks();

    void fetchFollowingAttakcs(String botId);

    void registerOnAttacksFetchListener(OnAttacksFetchListener listener);

    void unRegisterOnAttacksFetchListener();

}
