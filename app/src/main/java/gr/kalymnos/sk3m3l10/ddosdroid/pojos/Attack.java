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
    private boolean isActive;
    private long timeMilli;

    public Attack(String website, int networkType, Bot owner) {
        this.website = website;
        this.owner = owner;
        this.networkType = networkType;
        this.timeMilli = System.currentTimeMillis();
    }

    public Attack(String website, int networkType, Bot owner, List<Bot> botsList,
                  boolean isActive, long timeMilli) {
        this(website, networkType, owner);
        this.botsList = botsList;
        this.isActive = isActive;
        this.timeMilli = timeMilli;
    }

    public Attack(String pushId, String website, int networkType, List<Bot> botsList,
                  Bot owner, boolean isActive, long timeMilli) {
        this(website, networkType, owner, botsList, isActive, timeMilli);
        this.pushId = pushId;
    }

    public int getBotNetCount() {
        if (listHasItems(botsList)) {
            return botsList.size();
        }
        return 0;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean botBelongsToBotnet(String otherBotId) {
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
        isActive = in.readByte() != 0;
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
        parcel.writeByte((byte) (isActive ? 1 : 0));
        parcel.writeLong(timeMilli);
    }

    public interface AttackType {
        String ATTACK_TYPE_KEY = TAG + "attack type key";
        int TYPE_FETCH_ALL = 101;
        int TYPE_FETCH_JOINED = 102;
        int TYPE_FETCH_NOT_JOINED = 103;
        int TYPE_FETCH_OWNER = 104;
        int TYPE_NONE = -1;
    }

    public interface NetworkType {
        int INTERNET = 0;
        int WIFI_P2P = 1;
        int NSD = 2;
        int BLUETOOTH = 3;
    }

    public interface Extra {
        String EXTRA_ATTACK = TAG + "extra attacks";
        String EXTRA_ATTACKS = TAG + "caching attacks key";
    }

    public static class NetworkTypeTranslator {
        public static String translate(int networkType) {
            switch (networkType) {
                case NetworkType.BLUETOOTH:
                    return "Bluetooth";
                case NetworkType.INTERNET:
                    return "Internet";
                case NetworkType.WIFI_P2P:
                    return "Wifi Peer to Peer";
                case NetworkType.NSD:
                    return "Network Service Discovery";
                default:
                    throw new UnsupportedOperationException(TAG + ": no such network type");
            }
        }
    }
}
