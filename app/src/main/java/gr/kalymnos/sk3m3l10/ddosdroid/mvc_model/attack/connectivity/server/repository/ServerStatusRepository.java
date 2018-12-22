package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.repository;

public interface ServerStatusRepository {
    String TAG = "ServerStatusRepository";

    boolean isActive(String attackid);

    void setToActive(String attackId);

    void setToInActive(String attackId);
}
