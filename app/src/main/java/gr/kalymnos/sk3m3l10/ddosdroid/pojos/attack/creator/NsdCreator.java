package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator;

import android.os.Parcel;
import android.os.Parcelable;

final class NsdCreator extends AttackCreator {

    public NsdCreator() {
    }

    public NsdCreator(String uuid) {
        super(uuid);
    }

    public static final Parcelable.Creator<NsdCreator> CREATOR = new Creator<NsdCreator>() {
        @Override
        public NsdCreator createFromParcel(Parcel parcel) {
            return new NsdCreator(parcel);
        }

        @Override
        public NsdCreator[] newArray(int size) {
            return new NsdCreator[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private NsdCreator(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
}
