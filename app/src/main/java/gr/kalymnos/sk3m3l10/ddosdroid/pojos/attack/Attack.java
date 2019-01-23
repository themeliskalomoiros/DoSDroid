package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attack implements Parcelable {
    public static final String STARTED_PASS = "this_attack_has_started";
    public static final String STOPPED_PASS = "this_attack_has_stopped";
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

    private String pushId, website;
    private int networkType;
    private long timeMillis;
    private Map<String, String> hostInfo = new HashMap<>();
    private List<String> botIds = new ArrayList<>(); // Ideally a Set but Firebase accepts map/list.

    public Attack() {
    }

    public Attack(String website, int networkType) {
        this.website = website;
        this.networkType = networkType;
        this.timeMillis = System.currentTimeMillis();
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public List<String> getBotIds() {
        return botIds;
    }

    public void setBotIds(List<String> botIds) {
        this.botIds = botIds;
    }

    public Map<String, String> getHostInfo() {
        return hostInfo;
    }

    public void addSingleHostInfo(String key, String value) {
        hostInfo.put(key, value);
    }

    protected Attack(Parcel in) {
        pushId = in.readString();
        website = in.readString();
        networkType = in.readInt();
        timeMillis = in.readLong();
        botIds = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pushId);
        parcel.writeString(website);
        parcel.writeInt(networkType);
        parcel.writeLong(timeMillis);
        parcel.writeStringList(botIds);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
