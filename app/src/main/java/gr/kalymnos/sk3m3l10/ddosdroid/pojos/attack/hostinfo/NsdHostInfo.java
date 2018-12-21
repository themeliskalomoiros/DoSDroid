package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo;

import android.os.Parcel;
import android.os.Parcelable;

final class NsdHostInfo extends HostInfo {

    public NsdHostInfo() {
    }

    public NsdHostInfo(String uuid) {
        super(uuid);
    }

    public static final Parcelable.Creator<NsdHostInfo> CREATOR = new Creator<NsdHostInfo>() {
        @Override
        public NsdHostInfo createFromParcel(Parcel parcel) {
            return new NsdHostInfo(parcel);
        }

        @Override
        public NsdHostInfo[] newArray(int size) {
            return new NsdHostInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private NsdHostInfo(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
}
