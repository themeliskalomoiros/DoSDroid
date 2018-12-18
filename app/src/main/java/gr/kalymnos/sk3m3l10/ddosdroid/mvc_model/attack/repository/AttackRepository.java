package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;


/*
 * This repository stores all the DDoS attack data
 * */

public abstract class AttackRepository {
    protected static final String TAG = "AttackRepository";

    public interface OnAttacksFetchListener {
        void attacksFetched(List<Attack> attacks);

        void attacksFetchedFail(String msg);
    }

    public interface OnAttackUploadedListener {
        void onAttackUploaded(Attack attack);
    }

    protected OnAttacksFetchListener onAttacksFetchListener;
    protected OnAttackUploadedListener onAttackUploadedListener;

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
        onAttacksFetchListener = listener;
    }

    public final void removeOnAttacksFetchListener() {
        onAttacksFetchListener = null;
    }

    public final void addOnAttackUploadedListener(OnAttackUploadedListener listener) {
        onAttackUploadedListener = listener;
    }

    public final void removeOnAttackUploadedListener() {
        onAttackUploadedListener = null;
    }

}
