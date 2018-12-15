package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashSet;
import java.util.Set;

public class Attack implements Parcelable {

    private String pushId, website;
    private int networkType;
    private long timeMilli;
    private Bot owner;
    private Set<String> bots = new HashSet<>();

    public Attack() {
    }

    public Attack(String website, int networkType, Bot owner) {
        this.website = website;
        this.owner = owner;
        this.networkType = networkType;
        this.timeMilli = System.currentTimeMillis();
    }

    protected Attack(Parcel in) {
        pushId = in.readString();
        website = in.readString();
        networkType = in.readInt();
        timeMilli = in.readLong();
        owner = in.readParcelable(Bot.class.getClassLoader());
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

    public int getNetworkType() {
        return networkType;
    }

    public void addBot(Bot bot) {
        bots.add(bot.getId());
    }

    public void removeBot(Bot bot) {
        bots.remove(bot.getId());
    }

    public int getBotCount() {
        return bots.size();
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

    public Bot getOwner() {
        return owner;
    }

    public long getTimeMilli() {
        return timeMilli;
    }

    public boolean includes(String botId) {
        return bots.contains(botId);
    }

    public boolean isOwnedBy(String ownerId) {
        return owner.getId().equals(ownerId);
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
        parcel.writeLong(timeMilli);
        parcel.writeParcelable(owner, i);
    }
}
