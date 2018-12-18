package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator;

import android.os.Parcel;
import android.os.Parcelable;

public class InternetCreator extends AttackCreator {

    public InternetCreator() {
    }

    public InternetCreator(String uuid) {
        super(uuid);
    }

    public static final Parcelable.Creator<InternetCreator> CREATOR = new Creator<InternetCreator>() {
        @Override
        public InternetCreator createFromParcel(Parcel parcel) {
            return new InternetCreator(parcel);
        }

        @Override
        public InternetCreator[] newArray(int size) {
            return new InternetCreator[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private InternetCreator(Parcel in){
        super(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
}
