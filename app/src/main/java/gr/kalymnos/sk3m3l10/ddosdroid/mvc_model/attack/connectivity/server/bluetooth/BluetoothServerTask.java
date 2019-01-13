package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;

class BluetoothServerTask implements Runnable {
    private BluetoothSocket socket = null;

    BluetoothServerTask(@NonNull BluetoothSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
