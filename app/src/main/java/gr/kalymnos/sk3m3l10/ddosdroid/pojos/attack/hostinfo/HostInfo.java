package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class HostInfo implements Parcelable {
    private String uuid;

    protected HostInfo() {
        // For firebase
    }

    protected HostInfo(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(uuid);
    }

    protected HostInfo(Parcel in) {
        uuid = in.readString();
    }
}
