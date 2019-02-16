package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.bluetooth.discoverable;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

/*
 * The plan was to ask for infinite discoverability, but apart from the security hole there
 * is also one in the API. I asked at StackOverflow. Long story short DURATION
 * and RESULT_CANCELED are both zero which results in a compilation error for switch statement.
 * */

public class BluetoothDiscoverableActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothDiscoverabilit";
    private static final int RC = 1313;
    private static final int DURATION = 3600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestDiscoverability();
    }

    private void requestDiscoverability() {
        startActivityForResult(getDiscoverabilityIntent(), RC);
    }

    @NonNull
    private Intent getDiscoverabilityIntent() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DURATION);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC) {
            switch (resultCode) {
                case DURATION:
                    broadcastDiscoverability();
                    break;
                case RESULT_CANCELED:
                    broadcastDiscoverabilityFailure();
                    break;
                default:
                    throw new IllegalArgumentException(TAG + "Unknown result code");
            }
        }
        finish();
    }

    private void broadcastDiscoverability() {
        Intent intent = new Intent(BluetoothDiscoverableConstraint.ACTION_DISCOVERABILITY_ENABLED);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void broadcastDiscoverabilityFailure() {
        Intent intent = new Intent(BluetoothDiscoverableConstraint.ACTION_DISCOVERABILITY_DISABLED);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent);
    }
}
