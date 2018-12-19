package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator.HostInfo;

public class Attack implements Parcelable {
    private String pushId, website;
    private int networkType;
    private long timeMillis;
    private HostInfo hostInfo;
    private List<String> botIds = new ArrayList<>(); // Ideally a Set but Firebase accepts map/list.


    public Attack() {
    }

    public Attack(String website, int networkType, HostInfo hostInfo) {
        this.website = website;
        this.networkType = networkType;
        this.hostInfo = hostInfo;
        this.timeMillis = System.currentTimeMillis();
    }

    public Attack(String pushId, String website, int networkType, long timeMillis, HostInfo hostInfo, List<String> botIds) {
        this.pushId = pushId;
        this.website = website;
        this.networkType = networkType;
        this.timeMillis = timeMillis;
        this.hostInfo = hostInfo;
        if (botIds != null) // Maybe null because an attack can be created with zero bots.
            this.botIds = botIds;
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

    public HostInfo getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(HostInfo hostInfo) {
        this.hostInfo = hostInfo;
    }

    public List<String> getBotIds() {
        return botIds;
    }

    public void setBotIds(List<String> botIds) {
        this.botIds = botIds;
    }

    protected Attack(Parcel in) {
        pushId = in.readString();
        website = in.readString();
        networkType = in.readInt();
        timeMillis = in.readLong();
        hostInfo = in.readParcelable(HostInfo.class.getClassLoader());
        botIds = in.createStringArrayList();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pushId);
        parcel.writeString(website);
        parcel.writeInt(networkType);
        parcel.writeLong(timeMillis);
        parcel.writeParcelable(hostInfo, i);
        parcel.writeStringList(botIds);
    }
}
