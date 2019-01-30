package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.bluetooth.BluetoothSocket;

class BluetoothConnectionThread extends Thread {
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

    }
}
