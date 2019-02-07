package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository;

/*  Reports when a change occured in the attack repository.
 * For example an attack was added, changed, removed, etc...*/

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public abstract class AttackRepositoryReporter {
    protected OnRepositoryChangeListener onRepositoryChangeListener;

    public interface OnRepositoryChangeListener {
        void onAttackUpload(Attack attack);

        void onAttackUpdate(Attack changedAttack);

        void onAttackDelete(Attack deletedAttack);
    }

    public final void addOnRepositoryChangeListener(OnRepositoryChangeListener listener) {
        this.onRepositoryChangeListener = listener;
    }

    public final void removeOnRepositoryChangeListener() {
        onRepositoryChangeListener = null;
    }

    public abstract void startListenForChanges();

    public abstract void stopListenForChanges();

    public abstract void upload(Attack attack);

    public abstract void update(Attack attack);

    public abstract void delete(String attackId);
}
