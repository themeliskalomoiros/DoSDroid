package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class AttackCreator implements Parcelable {
    private String uuid;

    protected AttackCreator() {
        // For firebase
    }

    protected AttackCreator(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(uuid);
    }

    protected AttackCreator(Parcel in) {
        uuid = in.readString();
    }
}
