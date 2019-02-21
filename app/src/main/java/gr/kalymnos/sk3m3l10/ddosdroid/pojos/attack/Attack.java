package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attack implements Parcelable {
    private String pushId, website;
    private int networkType;
    private long creationTimeMilli, launchTimeMilli;
    private Map<String, String> hostInfo = new HashMap<>();
    private List<String> botIds = new ArrayList<>(); // Ideally a Set but Firebase accepts map/list.

    public Attack() {
    }

    public Attack(String website, int networkType) {
        this.website = website;
        this.networkType = networkType;
        this.creationTimeMilli = System.currentTimeMillis();
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public long getLaunchTimeMilli() {
        return launchTimeMilli;
    }

    public void setLaunchTimeMilli(long launchTimeMilli) {
        this.launchTimeMilli = launchTimeMilli;
    }

    public String getWebsite() {
        return website;
    }

    public int getNetworkType() {
        return networkType;
    }

    public long getCreationTimeMilli() {
        return creationTimeMilli;
    }

    public List<String> getBotIds() {
        return botIds;
    }

    public Map<String, String> getHostInfo() {
        return hostInfo;
    }

    public void addSingleHostInfo(String key, String value) {
        hostInfo.put(key, value);
    }

    //  Overriding equality. Two objects are equal when their id's are equal.
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Attack))
            return false;

        Attack attack = (Attack) obj;
        return this.getPushId().equals(attack.getPushId());
    }

    //  This technique is taken from the book Effective Java, Second Edition, Item 9.
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getPushId().hashCode();
        return result;
    }

    //  Parcelable implementation
    public static final Creator<Attack> CREATOR = new Creator<Attack>() {
        @Override
        public Attack createFromParcel(Parcel in) {
            return new Attack(in);
        }

        @Override
        public Attack[] newArray(int size) {
            return new Attack[size];
        }
    };

    protected Attack(Parcel in) {
        pushId = in.readString();
        website = in.readString();
        networkType = in.readInt();
        creationTimeMilli = in.readLong();
        launchTimeMilli = in.readLong();
        botIds = in.createStringArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pushId);
        parcel.writeString(website);
        parcel.writeInt(networkType);
        parcel.writeLong(creationTimeMilli);
        parcel.writeLong(launchTimeMilli);
        parcel.writeStringList(botIds);
    }
}
