package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks.attack_repo;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack;


/*
 * This repository stores all the DDoS attack data
 * */

public abstract class AttackRepository {
    protected static final String TAG = "AttackRepository";

    public interface OnAttacksFetchListener {
        void attacksFetchedSuccess(List<Attack> attacks);

        void attacksFetchedFail(String msg);
    }

    protected OnAttacksFetchListener callback;

    public abstract void fetchAllAttacks();

    public abstract void fetchAllAttacksOf(int networkType);

    public abstract void fetchJoinedAttakcsOf(String botId);

    public abstract void fetchJoinedAttakcsOf(String botId, int networkType);

    public abstract void fetchNotJoinedAttacksOf(String botId);

    public abstract void fetchNotJoinedAttacksOf(String botId, int networkType);

    public abstract void fetchLocalOwnerAttacks();

    public abstract void fetchLocalOwnerAttacksOf(int networkType);

    public abstract void uploadAttack(Attack attack);

    public abstract void updateAttack(Attack attack);

    public final void addOnAttacksFetchListener(OnAttacksFetchListener listener) {
        callback = listener;
    }

    public final void removeOnAttacksFetchListener() {
        callback = null;
    }

}
