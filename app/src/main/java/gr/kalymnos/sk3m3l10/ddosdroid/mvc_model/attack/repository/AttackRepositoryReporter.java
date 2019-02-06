package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository;

/*  Reports when a change occured in the attack repository.
 * For example an attack was added, changed, removed, etc...*/

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public abstract class AttackRepositoryReporter {
    protected OnAttackNodeListener onAttackNodeListener;

    public interface OnAttackNodeListener {
        void onAttackAddedToRepository(Attack attack);

        void onAttackChangedInRepository(Attack changedAttack);

        void onAttackDeletedFromRepository(Attack deletedAttack);
    }

    public final void setOnAttackNodeListener(OnAttackNodeListener listener) {
        this.onAttackNodeListener = listener;
    }

    public abstract void attach();

    public abstract void detach();

    public abstract void upload(Attack attack);

    public abstract void update(Attack attack);

    public abstract void delete(Attack attack);
}
