package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator;

import android.os.Parcel;
import android.os.Parcelable;

final class BluetoothCreator extends HostInfo {
   
    public BluetoothCreator() {
    }

    public BluetoothCreator(String uuid) {
        super(uuid);
    }

    public static final Parcelable.Creator<BluetoothCreator> CREATOR = new Creator<BluetoothCreator>() {
        @Override
        public BluetoothCreator createFromParcel(Parcel parcel) {
            return new BluetoothCreator(parcel);
        }

        @Override
        public BluetoothCreator[] newArray(int size) {
            return new BluetoothCreator[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private BluetoothCreator(Parcel in){
        super(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
}
