package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository;

public interface AttackStatusRepository {
    String TAG = "AttackStatusRepository";

    boolean isActive(String attackid);

    void setStatusOf(String attackId, boolean isActive);
}
