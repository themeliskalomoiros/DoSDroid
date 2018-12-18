package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Bot implements Parcelable {
    private String id;

    public Bot() {
    }

    public Bot(String instanceId) {
        this.id = instanceId;
    }

    public String getId() {
        return id;
    }

    /**
     * Parcelable code
     */

    protected Bot(Parcel in) {
        id = in.readString();
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
        parcel.writeString(id);
    }
}
