package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public abstract class AttackDeletionReporter {
    private AttackDeletionListener callback;

    public interface AttackDeletionListener {
        void onAttackDeleted(Attack attack);
    }

    public final void setAttackDeletionListener(AttackDeletionListener listener) {
        this.callback = listener;
    }

    public abstract void attach();

    public abstract void dettach();
}
