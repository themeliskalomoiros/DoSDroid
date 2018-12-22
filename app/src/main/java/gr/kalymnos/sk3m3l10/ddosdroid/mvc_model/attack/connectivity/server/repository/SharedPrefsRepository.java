package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsRepository implements ServerStatusRepository {
    private SharedPreferences sharedPreferences;

    public SharedPrefsRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    @Override
    public boolean started(String serverId) {
        return sharedPreferences.getBoolean(serverId, false);
    }

    @Override
    public void setToStarted(String serverId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(serverId, true);
        editor.apply();
    }

    @Override
    public void setToStopped(String serverId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(serverId, false);
        editor.apply();
    }
}
