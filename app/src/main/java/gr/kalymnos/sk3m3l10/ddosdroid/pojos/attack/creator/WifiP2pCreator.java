package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator;

import android.os.Parcel;
import android.os.Parcelable;

final class WifiP2pCreator extends AttackCreator {

    public WifiP2pCreator() {
    }

    public WifiP2pCreator(String uuid) {
        super(uuid);
    }

    public static final Parcelable.Creator<WifiP2pCreator> CREATOR = new Creator<WifiP2pCreator>() {
        @Override
        public WifiP2pCreator createFromParcel(Parcel parcel) {
            return new WifiP2pCreator(parcel);
        }

        @Override
        public WifiP2pCreator[] newArray(int size) {
            return new WifiP2pCreator[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private WifiP2pCreator(Parcel in){
        super(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
}
