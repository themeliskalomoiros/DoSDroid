package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public abstract class AttackRepository {
    protected OnRepositoryChangeListener repositoryListener;

    public interface OnRepositoryChangeListener {
        void onAttackUpload(Attack uploadedAttack);

        void onAttackUpdate(Attack changedAttack);

        void onAttackDelete(Attack deletedAttack);
    }

    public final void setOnRepositoryChangeListener(OnRepositoryChangeListener listener) {
        this.repositoryListener = listener;
    }

    public final void removeOnRepositoryChangeListener() {
        repositoryListener = null;
    }

    public abstract void startListenForChanges();

    public abstract void stopListenForChanges();

    public abstract void upload(Attack attack);

    public abstract void update(Attack attack);

    public abstract void delete(String attackId);
}