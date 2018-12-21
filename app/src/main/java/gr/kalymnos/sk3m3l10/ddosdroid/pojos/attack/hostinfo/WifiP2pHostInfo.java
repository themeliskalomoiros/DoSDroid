package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo;

import android.os.Parcel;
import android.os.Parcelable;

final class WifiP2pHostInfo extends HostInfo {

    public WifiP2pHostInfo() {
    }

    public WifiP2pHostInfo(String uuid) {
        super(uuid);
    }

    public static final Parcelable.Creator<WifiP2pHostInfo> CREATOR = new Creator<WifiP2pHostInfo>() {
        @Override
        public WifiP2pHostInfo createFromParcel(Parcel parcel) {
            return new WifiP2pHostInfo(parcel);
        }

        @Override
        public WifiP2pHostInfo[] newArray(int size) {
            return new WifiP2pHostInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private WifiP2pHostInfo(Parcel in){
        super(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
}
