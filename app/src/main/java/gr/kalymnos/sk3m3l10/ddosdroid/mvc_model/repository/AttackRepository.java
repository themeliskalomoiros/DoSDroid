package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.repository;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public abstract class AttackRepository {
    protected OnRepositoryChangeListener repositoryListener;

    public interface OnRepositoryChangeListener {
        void onAttackUpload(Attack attack);

        void onAttackUpdate(Attack attack);

        void onAttackDelete(Attack attack);
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

    public abstract void removeLocalBotAndUpdate(String attackId);

    public abstract void delete(String attackId);
}
