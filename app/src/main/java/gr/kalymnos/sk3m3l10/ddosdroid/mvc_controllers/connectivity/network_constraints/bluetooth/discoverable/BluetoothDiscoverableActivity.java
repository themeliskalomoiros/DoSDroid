package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.network_constraints.bluetooth.discoverable;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
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
    public static final String ACTION_DISCOVERABLE = "action discoverability enabled";
    public static final String ACTION_NOT_DISCOVERABLE = "action discoverability disabled";

    public static void startInstance(Context context) {
        context.startActivity(getIntent(context));
    }

    @NonNull
    private static Intent getIntent(Context context) {
        Intent intent = new Intent(context, BluetoothDiscoverableActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

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
                    broadcastDiscoverable();
                    break;
                case RESULT_CANCELED:
                    broadcastFailure();
                    break;
                default:
                    throw new IllegalArgumentException(TAG + "Unknown result code");
            }
        }
        finish();
    }

    private void broadcastDiscoverable() {
        Intent intent = new Intent(ACTION_DISCOVERABLE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastFailure() {
        Intent intent = new Intent(ACTION_NOT_DISCOVERABLE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
