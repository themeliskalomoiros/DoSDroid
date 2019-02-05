package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository;

/*  Reports when a change occured in the attack repository.
 * For example an attack was added, changed, removed, etc...*/

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public abstract class AttackRepositoryReporter {
    private OnAttackNodeListener onAttackNodeListener;

    private interface OnAttackNodeListener {
        void onAttackAdded(Attack attack);

        void onAttackChanged(Attack changedAttack);

        void onAttackDeleted(Attack deletedAttack);
    }

    public final void setOnAttackNodeListener(OnAttackNodeListener listener) {
        this.onAttackNodeListener = listener;
    }

    public abstract void attach();

    public abstract void detach();
}
