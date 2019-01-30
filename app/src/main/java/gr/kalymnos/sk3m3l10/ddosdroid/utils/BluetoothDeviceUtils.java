package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
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

    private static String getLocalMacAddress() {
        //  Workaround from https://stackoverflow.com/questions/33103798/how-to-get-wi-fi-mac-address-in-android-marshmallow
        //  Reason at https://developer.android.com/about/versions/marshmallow/android-6.0-changes#behavior-hardware-id
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
}
