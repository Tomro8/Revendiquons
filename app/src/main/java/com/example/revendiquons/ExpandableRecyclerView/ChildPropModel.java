
package com.example.revendiquons.ExpandableRecyclerView;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

//The data to be put in the expanded area

public class ChildPropModel implements Parcelable {

    private String description;
    private int voteValue;

    public ChildPropModel(String description, int voteValue) {
        this.description = description;
        this.voteValue = voteValue;
    }

    private ChildPropModel(Parcel in) {
        description = in.readString();
        voteValue = in.readInt();
    }

    public static final Creator<ChildPropModel> CREATOR = new Creator<ChildPropModel>() {
        @Override
        public ChildPropModel createFromParcel(Parcel in) {
            return new ChildPropModel(in);
        }

        @Override
        public ChildPropModel[] newArray(int size) {
            return new ChildPropModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeInt(voteValue);
    }

    public String getDescription() {
        return description;
    }

    public int getVoteValue() {
        Log.i("rcl","in child model, return vote value: " + voteValue);
        return voteValue;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVoteValue(int voteValue) {
        this.voteValue = voteValue;
    }

    @Override
    public String toString() {
        return "ChildPropModel{" +
                "description='" + description + '\'' +
                ", voteValue=" + voteValue +
                '}';
    }
}
