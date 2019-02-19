package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.status.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefPersistance implements ServerStatusPersistance {
    private SharedPreferences sharedPreferences;

    public SharedPrefPersistance(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isStarted(String serverWebsite) {
        return sharedPreferences.getBoolean(serverWebsite, false);
    }

    @Override
    public void setToStarted(String serverWebsite) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(serverWebsite, true);
        editor.apply();
    }

    @Override
    public void setToStopped(String serverWebsite) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(serverWebsite, false);
        editor.apply();
    }
}
