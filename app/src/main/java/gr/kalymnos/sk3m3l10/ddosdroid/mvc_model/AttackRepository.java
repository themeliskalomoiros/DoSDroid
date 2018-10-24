package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;


/*
 * This repository stores all the DDoS attack data
 * */

public interface AttackRepository {

    void fetchAllAttacks();

    void fetchFollowingAttakcs(String botId);

}
