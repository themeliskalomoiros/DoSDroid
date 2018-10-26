package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

public class DDoSAttack implements Parcelable {
    private static final String TAG = DDoSAttack.class.getSimpleName();
    public static final String CACHED_ATTACKS_KEY = TAG + "caching attacks key";

    public interface AttackType {
        String ATTACK_TYPE_KEY = TAG + "attack type key";
        int TYPE_FETCH_ALL = 101;
        int TYPE_FETCH_FOLLOWING = 102;
        int TYPE_FETCH_OWNER = 103;
        int TYPE_NONE = -1;
    }

    public interface NetworkType {
        int INTERNET = 0;
        int WIFI_P2P = 1;
        int NSD = 2;
        int BLUETOOTH = 3;
    }

    private String pushId, targetWebsite;
    int networkType;
    private List<DDoSBot> botsList;
    private DDoSBot owner;
    private boolean isActive;
    private long timeMilli;

    public DDoSAttack(String targetWebsite, int networkType, DDoSBot owner) {
        this.targetWebsite = targetWebsite;
        this.owner = owner;
        this.networkType = networkType;
        this.timeMilli = System.currentTimeMillis();
    }

    public DDoSAttack(String targetWebsite, int networkType, DDoSBot owner, List<DDoSBot> botsList,
                      boolean isActive, long timeMilli) {
        this(targetWebsite, networkType, owner);
        this.botsList = botsList;
        this.isActive = isActive;
        this.timeMilli = timeMilli;
    }

    public DDoSAttack(String pushId, String targetWebsite, int networkType, List<DDoSBot> botsList,
                      DDoSBot owner, boolean isActive, long timeMilli) {
        this(targetWebsite, networkType, owner, botsList, isActive, timeMilli);
        this.pushId = pushId;
    }

    public int getBotNetCount() {
        if (listHasItems(botsList)) {
            return botsList.size();
        }
        return 0;
    }

    public void addBot(DDoSBot bot) {
        if (listHasItems(botsList)) {
            botsList.add(bot);
        }
        throw new UnsupportedOperationException(TAG + "bot list is null or empty");
    }

    public void removeBot(DDoSBot bot) {
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

    public String getTargetWebsite() {
        return targetWebsite;
    }

    public DDoSBot getOwner() {
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
            for (DDoSBot bot : botsList) {
                if (bot.getId().equals(otherBotId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getNetworkType() {
        return networkType;
    }

    protected DDoSAttack(Parcel in) {
        pushId = in.readString();
        targetWebsite = in.readString();
        networkType = in.readInt();
        isActive = in.readByte() != 0;
        timeMilli = in.readLong();
    }

    public static final Creator<DDoSAttack> CREATOR = new Creator<DDoSAttack>() {
        @Override
        public DDoSAttack createFromParcel(Parcel in) {
            return new DDoSAttack(in);
        }

        @Override
        public DDoSAttack[] newArray(int size) {
            return new DDoSAttack[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pushId);
        parcel.writeString(targetWebsite);
        parcel.writeInt(networkType);
        parcel.writeByte((byte) (isActive ? 1 : 0));
        parcel.writeLong(timeMilli);
    }
}
