package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.status.repository;

public interface StatusRepository {
    String TAG = "StatusRepository";

    boolean isStarted(String serverWebsite);

    void setToStarted(String serverWebsite);

    void setToStopped(String serverWebsite);
}
