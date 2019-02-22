package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.persistance.job;

public abstract class JobPersistance {
    protected static final String FILE_NAME = "JobPersistance";

    protected OnJobPersistanceListener listener;

    public interface OnJobPersistanceListener {
        void onJobSave(String jobTag);

        void onJobSaveError(String jobTag);

        void onJobDelete(String jobTag);
    }

    public final void setListener(OnJobPersistanceListener listener) {
        this.listener = listener;
    }

    public abstract boolean exists(String jobTag);

    public abstract void save(String jobTag);

    public abstract void delete(String jobTag);
}
