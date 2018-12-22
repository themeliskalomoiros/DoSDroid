package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.repository;

public interface ServerStatusRepository {
    String TAG = "ServerStatusRepository";

    boolean started(String serverId);

    void setToStarted(String serverId);

    void setToStopped(String serverId);
}
