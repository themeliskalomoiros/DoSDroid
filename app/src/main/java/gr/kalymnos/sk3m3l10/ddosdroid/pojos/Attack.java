package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

public class Attack implements Parcelable {

    private String pushId, website;
    int networkType;
    private List<Bot> bots = new ArrayList<>();
    private Bot owner;
    private long timeMilli;

    public Attack() {
    }

    public Attack(String website, int networkType, Bot owner) {
        this.website = website;
        this.owner = owner;
        this.networkType = networkType;
        this.timeMilli = System.currentTimeMillis();
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

    public int getBotCount() {
        if (listHasItems(bots)) {
            return bots.size();
        }
        return 0;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void addBot(Bot bot) {
        bots.add(bot);
    }

    public void removeBot(Bot bot) {
        bots.remove(bot);
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

    public boolean includes(String otherBotId) {
        if (ValidationUtils.listHasItems(bots)) {
            for (Bot bot : bots) {
                if (bot.getId().equals(otherBotId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOwnedBy(String ownerId) {
        return owner.getId().equals(ownerId);
    }

    protected Attack(Parcel in) {
        pushId = in.readString();
        website = in.readString();
        networkType = in.readInt();
        bots = in.createTypedArrayList(Bot.CREATOR);
        owner = in.readParcelable(Bot.class.getClassLoader());
        timeMilli = in.readLong();
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
        parcel.writeTypedList(bots);
        parcel.writeParcelable(owner, i);
        parcel.writeLong(timeMilli);
    }
}
