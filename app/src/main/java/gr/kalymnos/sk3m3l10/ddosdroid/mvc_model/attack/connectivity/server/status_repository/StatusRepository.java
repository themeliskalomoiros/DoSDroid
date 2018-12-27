package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status_repository;

public interface StatusRepository {
    String TAG = "StatusRepository";

    boolean isStarted(String serverId);

    void setToStarted(String serverId);

    void setToStopped(String serverId);
}
