package com.example.revendiquons.ExpandableRecyclerView;

import android.os.Parcel;
import android.os.Parcelable;

//The data to be put in the expanded area

public class ExpandedProp implements Parcelable {

    private String description;
    private int prop_id;

    public ExpandedProp(String desc, int prop_id) {
        this.description = desc;
        this.prop_id = prop_id;
    }

    protected ExpandedProp(Parcel in) {
        description = in.readString();
        prop_id = in.readInt();
    }

    public String getDescription() {
        return description;
    }
    public int getPropId() { return prop_id; }

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
