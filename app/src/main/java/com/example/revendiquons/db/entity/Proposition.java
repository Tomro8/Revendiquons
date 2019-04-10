package com.example.revendiquons.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Proposition implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int user_id;
    private String title;
    private String description;
    private int positive;
    private int negative;

    protected Proposition(Parcel in) {
        id = in.readInt();
        user_id = in.readInt();
        title = in.readString();
        description = in.readString();
        positive = in.readInt();
        negative = in.readInt();
    }

    public static final Creator<Proposition> CREATOR = new Creator<Proposition>() {
        @Override
        public Proposition createFromParcel(Parcel in) {
            return new Proposition(in);
        }

        @Override
        public Proposition[] newArray(int size) {
            return new Proposition[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public int getNegative() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }

    public void incPositive() {
        this.positive = positive + 1;
    }

    public void decPositive() {
        this.positive = positive - 1;
    }

    public void incNegative() {
        this.negative = negative + 1;
    }

    public void decNegative() {
        this.negative = negative -1;
    }

    public Proposition(int id, int user_id, String title, String description, int positive, int negative) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.positive = positive;
        this.negative = negative;
    }

    @Override
    public String toString() {
        return "Proposition{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", positive=" + positive +
                ", negative=" + negative +
                ", hashcode=" + hashCode() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(user_id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(positive);
        parcel.writeInt(negative);
    }
}
