package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.persistance.job;

import java.util.Map;

public interface JobPersistance {
    String FILE_NAME = "JobPersistance";

    int size();

    boolean has(String jobTag);

    void save(String jobTag);

    void delete(String jobTag);

    void clear();

    Map<String,?> map();
}