package com.example.revendiquons.ExpandableRecyclerView;

import android.os.Parcel;
import android.os.Parcelable;

//The data to be put in the expanded area

public class ExpandedProp implements Parcelable {

    private String description;

    public ExpandedProp(String desc) {
        this.description = desc;
    }

    protected ExpandedProp(Parcel in) {
        description = in.readString();
    }

    public String getDescription() {
        return description;
    }

    public static final Creator<ExpandedProp> CREATOR = new Creator<ExpandedProp>() {
        @Override
        public ExpandedProp createFromParcel(Parcel in) {
            return new ExpandedProp(in);
        }

        @Override
        public ExpandedProp[] newArray(int size) {
            return new ExpandedProp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
    }
}
