package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth.constraints;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BluetoothDiscoverabilityActivity extends AppCompatActivity {
    private static final int DISCOVERABILITY_REQUEST_CODE = 1313;
    private static final int ALWAYS_DISCOVERABLE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestDiscoverability();
    }

    private void requestDiscoverability() {
        startActivityForResult(getDiscoverabilityIntent(),DISCOVERABILITY_REQUEST_CODE);
    }

    @NonNull
    private Intent getDiscoverabilityIntent() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, ALWAYS_DISCOVERABLE);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        boolean discoverabilityEnabled = requestCode == DISCOVERABILITY_REQUEST_CODE &&
                resultCode == DISCOVERABILITY_REQUEST_CODE;
        if (discoverabilityEnabled){

        }else if (resultCode==RESULT_CANCELED){

        }
    }
}
