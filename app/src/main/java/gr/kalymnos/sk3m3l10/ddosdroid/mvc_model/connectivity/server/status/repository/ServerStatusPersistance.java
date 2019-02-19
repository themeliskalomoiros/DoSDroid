package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.status.repository;

public interface ServerStatusPersistance {
    String FILE_NAME = "ServerStatusPersistance";

    boolean isStarted(String serverWebsite);

    void setToStarted(String serverWebsite);

    void setToStopped(String serverWebsite);
}
