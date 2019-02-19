package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.client.bluetooth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

public class RequestLocationPermissionForBluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_COARSE_LOCATION = 1313;
    public static final String ACTION_PERMISSION_GRANTED = "action_permission_for_bluetooth_scanning_granted";
    public static final String ACTION_PERMISSION_DENIED = "action_permission_for_bluetooth_scanning_denied";

    private LocalBroadcastManager manager;

    public static void requestPermission(Context context) {
        Intent intent = new Intent(context, RequestLocationPermissionForBluetoothActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = LocalBroadcastManager.getInstance(this);
        checkLocationPermissionToStartDiscoverDevices();
    }

    private void checkLocationPermissionToStartDiscoverDevices() {
        boolean permissionNotGranted = ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (permissionNotGranted) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_COARSE_LOCATION);
        } else {
            broadcastPermissionGranted();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_COARSE_LOCATION) {
            boolean permissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (permissionGranted) {
                broadcastPermissionGranted();
            } else {
                broadcastPermissionDenied();
            }
            finish();
        }
    }

    private void broadcastPermissionGranted() {
        Intent intent = new Intent(ACTION_PERMISSION_GRANTED);
        manager.sendBroadcast(intent);
    }

    private void broadcastPermissionDenied() {
        Intent intent = new Intent(ACTION_PERMISSION_DENIED);
        manager.sendBroadcast(intent);
    }
}
