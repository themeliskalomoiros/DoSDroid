package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

public class Attack implements Parcelable {
    private static final String TAG = Attack.class.getSimpleName();

    private String pushId, website;
    int networkType;
    private List<Bot> botsList;
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

    public Attack(String website, int networkType, Bot owner, List<Bot> botsList, long timeMilli) {
        this(website, networkType, owner);
        this.botsList = botsList;
        this.timeMilli = timeMilli;
    }

    public Attack(String pushId, String website, int networkType, List<Bot> botsList,
                  Bot owner, long timeMilli) {
        this(website, networkType, owner, botsList, timeMilli);
        this.pushId = pushId;
    }

    public int getBotCount() {
        if (listHasItems(botsList)) {
            return botsList.size();
        }
        return 0;
    }

    public List<Bot> getBotsList() {
        return botsList;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void addBot(Bot bot) {
        if (listHasItems(botsList)) {
            botsList.add(bot);
        }
        throw new UnsupportedOperationException(TAG + "bot list is null or empty");
    }

    public void removeBot(Bot bot) {
        if (listHasItems(botsList)) {
            botsList.remove(bot);
        }
        throw new UnsupportedOperationException(TAG + "bot list is null or empty");
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
        if (ValidationUtils.listHasItems(botsList)) {
            for (Bot bot : botsList) {
                if (bot.getId().equals(otherBotId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasNetworkTypeOf(int networkType) {
        return networkType == networkType;
    }

    public boolean isOwnedBy(String ownerId) {
        return owner.getId().equals(ownerId);
    }

    protected Attack(Parcel in) {
        pushId = in.readString();
        website = in.readString();
        networkType = in.readInt();
        botsList = in.createTypedArrayList(Bot.CREATOR);
        owner = in.readParcelable(Bot.class.getClassLoader());
        timeMilli = in.readLong();
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
        parcel.writeTypedList(botsList);
        parcel.writeParcelable(owner, i);
        parcel.writeLong(timeMilli);
    }
}
