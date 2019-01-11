package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo;

import android.os.Parcel;
import android.os.Parcelable;

final class InternetHostInfo extends HostInfo {

    public InternetHostInfo() {
    }

    public InternetHostInfo(String uuid) {
        super(uuid);
    }

    public static final Parcelable.Creator<InternetHostInfo> CREATOR = new Creator<InternetHostInfo>() {
        @Override
        public InternetHostInfo createFromParcel(Parcel parcel) {
            return new InternetHostInfo(parcel);
        }

        @Override
        public InternetHostInfo[] newArray(int size) {
            return new InternetHostInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private InternetHostInfo(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
}
