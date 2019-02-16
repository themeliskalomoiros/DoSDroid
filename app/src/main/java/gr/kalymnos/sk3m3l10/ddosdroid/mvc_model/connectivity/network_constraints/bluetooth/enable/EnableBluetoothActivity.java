package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.bluetooth.enable;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class EnableBluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ENABLE_BLUETOOTH = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_bluetooth);
        requestBluetoothEnable();
    }

    private void requestBluetoothEnable() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQUEST_CODE_ENABLE_BLUETOOTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                sendEnableBroadcast();
            }
            if (resultCode == RESULT_CANCELED) {
                sendDisableBroadcast();
            }
            finish();
        }
    }

    private void sendEnableBroadcast() {
        Intent intent = new Intent(BluetoothEnableConstraint.ACTION_BLUETOOTH_ENABLED);
        getLocalBroadcastManager().sendBroadcast(intent);
    }

    private void sendDisableBroadcast() {
        Intent intent = new Intent(BluetoothEnableConstraint.ACTION_BLUETOOTH_DISABLED);
        getLocalBroadcastManager().sendBroadcast(intent);
    }

    private LocalBroadcastManager getLocalBroadcastManager() {
        return LocalBroadcastManager.getInstance(this);
    }
}
