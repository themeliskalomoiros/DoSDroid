package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.persistance.job;

public interface JobPersistance {
    public static final String FILE_NAME = "JobPersistance";

    public abstract boolean has(String jobTag);

    public abstract void save(String jobTag);

    public abstract void delete(String jobTag);
}
