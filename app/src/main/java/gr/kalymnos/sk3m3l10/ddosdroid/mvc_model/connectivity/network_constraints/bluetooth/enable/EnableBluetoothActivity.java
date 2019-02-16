package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.bluetooth.enable;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

public class EnableBluetoothActivity extends AppCompatActivity {
    private static final int RC = 1010;
    public static final String ACTION_ENABLED = "action bluetooth enabled";
    public static final String ACTION_DISABLED = "action bluetooth disabled";

    public static void startInstance(Context context) {
        context.startActivity(activityIntent(context));
    }

    private static Intent activityIntent(Context context) {
        Intent enableIntent = new Intent(context, EnableBluetoothActivity.class);
        enableIntent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return enableIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestBluetoothEnable();
    }

    private void requestBluetoothEnable() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, RC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC) {
            if (resultCode == RESULT_OK) {
                broadcastEnabled();
            }
            if (resultCode == RESULT_CANCELED) {
                broadcastDisabled();
            }
            finish();
        }
    }

    private void broadcastEnabled() {
        Intent intent = new Intent(ACTION_ENABLED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastDisabled() {
        Intent intent = new Intent(ACTION_DISABLED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
