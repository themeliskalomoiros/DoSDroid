package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

public class DDoSAttack implements Parcelable {

    private static final String TAG = DDoSAttack.class.getSimpleName();
    
    public interface AttackType{
         String ATTACK_TYPE_KEY = TAG + "attack type key";
         String CACHED_ATTACKS_KEY = TAG + "caching attacks key";
         int TYPE_FETCH_ALL = 101;
         int TYPE_FETCH_FOLLOWING = 102;
         int TYPE_FETCH_OWNER = 103;
         int TYPE_NONE = -1;
    }

    private String pushId, targetWebsite;
    private List<DDoSBot> botsList;
    private DDoSBot owner;
    private boolean isActive;
    private long timeMilli;

    public DDoSAttack(String targetWebsite, DDoSBot owner) {
        this.targetWebsite = targetWebsite;
        this.owner = owner;
        this.timeMilli = System.currentTimeMillis();
    }

    public DDoSAttack(String targetWebsite, List<DDoSBot> botsList, DDoSBot owner, boolean isActive, long timeMilli) {
        this.targetWebsite = targetWebsite;
        this.botsList = botsList;
        this.owner = owner;
        this.isActive = isActive;
        this.timeMilli = timeMilli;
    }

    public DDoSAttack(String pushId, String targetWebsite, List<DDoSBot> botsList, DDoSBot owner, boolean isActive, long timeMilli) {
        this(targetWebsite, botsList, owner, isActive, timeMilli);
        this.pushId = pushId;
    }

    protected DDoSAttack(Parcel in) {
        pushId = in.readString();
        targetWebsite = in.readString();
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

    public void start() {
        // TODO: needs implementation
    }

    public void stop() {
        // TODO: needs implementation
    }

    public String getPushId() {
        return pushId;
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

    public void setPushId(String pushId) {
        this.pushId = pushId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pushId);
        parcel.writeString(targetWebsite);
        parcel.writeByte((byte) (isActive ? 1 : 0));
        parcel.writeLong(timeMilli);
    }
}
