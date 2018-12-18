package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator.AttackCreator;

public class Attack implements Parcelable {
    private String pushId, website;
    private int networkType;
    private long timeMillis;
    private AttackCreator attackCreator;
    private List<String> botIds = new ArrayList<>(); // Ideally a Set but Firebase accepts map/list.


    public Attack() {
    }

    public Attack(String website, int networkType, AttackCreator attackCreator) {
        this.website = website;
        this.networkType = networkType;
        this.attackCreator = attackCreator;
    }

    public String getPushId() {
        return pushId;
    }

    public String getWebsite() {
        return website;
    }

    public int getNetworkType() {
        return networkType;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public AttackCreator getAttackCreator() {
        return attackCreator;
    }

    public List<String> getBotIds() {
        return botIds;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    protected Attack(Parcel in) {
        pushId = in.readString();
        website = in.readString();
        networkType = in.readInt();
        timeMillis = in.readLong();
        attackCreator = in.readParcelable(AttackCreator.class.getClassLoader());
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
        parcel.writeParcelable(attackCreator, i);
        parcel.writeStringList(botIds);
    }
}
