package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class EnableBluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ENABLE_BLUETOOTH = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_bluetooth);
    }
}
