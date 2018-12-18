package gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot;

import android.os.Parcel;
import android.os.Parcelable;

public class Bot implements Parcelable {
    private String uuid;

    public Bot() {
    }

    public Bot(String instanceId) {
        this.uuid = instanceId;
    }

    public String getUuid() {
        return uuid;
    }

    /**
     * Parcelable code
     */

    protected Bot(Parcel in) {
        uuid = in.readString();
    }

    public static final Creator<Bot> CREATOR = new Creator<Bot>() {
        @Override
        public Bot createFromParcel(Parcel in) {
            return new Bot(in);
        }

        @Override
        public Bot[] newArray(int size) {
            return new Bot[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uuid);
    }
}
