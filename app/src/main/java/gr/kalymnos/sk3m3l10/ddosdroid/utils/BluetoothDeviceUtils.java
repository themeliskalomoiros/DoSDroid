package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

public class BluetoothDeviceUtils {
    private static final String TAG = "BluetoothDeviceUtils";

    public static boolean isThisDevicePairedWith(String deviceAddress /*MAC Address*/) {
        for (BluetoothDevice device : getPairedDevices()) {
            if (device.getAddress().equals(deviceAddress)) {
                return true;
            }
        }
        return false;
    }

    public static BluetoothDevice getPairedBluetoothDeviceOf(String deviceAddress) {
        for (BluetoothDevice device : getPairedDevices()) {
            if (device.getAddress().equals(deviceAddress)) {
                return device;
            }
        }
        throw new UnsupportedOperationException(TAG + ": Cound not find bluetoothDevice");
    }

    private static Set<BluetoothDevice> getPairedDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.getBondedDevices();
    }
}
