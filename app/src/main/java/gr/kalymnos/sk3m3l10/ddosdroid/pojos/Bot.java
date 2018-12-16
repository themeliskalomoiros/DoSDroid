package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.instance_id.FirebaseInstanceId;

public class Bot implements Parcelable {
    private String id;

    public Bot() {
    }

    public Bot(String instanceId) {
        this.id = instanceId;
    }

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

    public String getId() {
        return id;
    }

    public static Bot getLocalUser() {
        //  TODO: Replace with real InstanceIdProvider
        return new Bot(new FirebaseInstanceId().getInstanceId());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
    }
}
