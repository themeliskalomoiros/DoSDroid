package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo;

import android.os.Parcel;
import android.os.Parcelable;

final class BluetoothHostInfo extends HostInfo {
   
    public BluetoothHostInfo() {
    }

    public BluetoothHostInfo(String uuid) {
        super(uuid);
    }

    public static final Parcelable.Creator<BluetoothHostInfo> CREATOR = new Creator<BluetoothHostInfo>() {
        @Override
        public BluetoothHostInfo createFromParcel(Parcel parcel) {
            return new BluetoothHostInfo(parcel);
        }

        @Override
        public BluetoothHostInfo[] newArray(int size) {
            return new BluetoothHostInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private BluetoothHostInfo(Parcel in){
        super(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
}
