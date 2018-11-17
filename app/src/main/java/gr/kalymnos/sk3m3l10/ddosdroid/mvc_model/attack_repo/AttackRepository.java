package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_repo;

import android.app.Activity;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;


/*
 * This repository stores all the DDoS attack data
 * */

public abstract class AttackRepository {
    protected static final String TAG = "AttackRepository";

    public interface OnAttacksFetchListener {
        void attacksFetchedSuccess(List<DDoSAttack> attacks);

        void attacksFetchedFail(String msg);
    }

    protected Activity controller;
    protected OnAttacksFetchListener callback;

    public AttackRepository(Activity controller) {
        this.controller = controller;
    }

    public abstract void fetchAllAttacks();

    public abstract void fetchAllAttacksOf(int networkType);

    public abstract void fetchJoinedAttakcsOf(String botId);

    public abstract void fetchJoinedAttakcsOf(String botId, int networkType);

    public abstract void fetchNotJoinedAttacksOf(String botId);

    public abstract void fetchNotJoinedAttacksOf(String botId, int networkType);

    public abstract void fetchLocalOwnerAttacks();

    public abstract void fetchLocalOwnerAttacksOf(int networkType);

    public final void registerOnAttacksFetchListener(OnAttacksFetchListener listener) {
        callback = listener;
    }

    public final void unRegisterOnAttacksFetchListenerAndController() {
        callback = null;
        controller = null;
    }

}
