package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

class BluetoothConnectionThread extends Thread {
    private static final String TAG = "BluetoothConnectionThre";

    private BluetoothSocket bluetoothSocket;
    private OnBluetoothConnectionListener onBluetoothConnectionListener;

    interface OnBluetoothConnectionListener {
        void onBluetoothConnectionSuccess();

        void onBluetoothConnectionFailure();
    }

    public BluetoothConnectionThread(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }

    public void setOnBluetoothConnectionListener(OnBluetoothConnectionListener onBluetoothConnectionListener) {
        this.onBluetoothConnectionListener = onBluetoothConnectionListener;
    }

    @Override
    public void run() {
        if (connectedToServer()) {

        } else {

        }
    }

    private boolean connectedToServer() {
        try {
            bluetoothSocket.connect();
            return true;
        } catch (IOException e) {
            Log.d(TAG, "Error when bluetoothSocket.connect()", e);
            return false;
        }
    }
}
