package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.persistance.job;

public abstract class JobRepository {
    protected static final String FILE_NAME = "JobRepository";

    protected OnJobRepositoryListener listener;

    public interface OnJobRepositoryListener {
        void onJobRepositoryAdded(String jobTag);

        void onJobRepositoryAddedError(String jobTag);

        void onJobRepositoryRemoved(String jobTag);
    }

    public final void setListener(OnJobRepositoryListener listener) {
        this.listener = listener;
    }

    public abstract boolean exists(String jobTag);

    public abstract void add(String jobTag);

    public abstract void remove(String jobTag);
}
