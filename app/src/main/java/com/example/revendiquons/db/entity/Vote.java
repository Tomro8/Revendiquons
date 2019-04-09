package com.example.revendiquons.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"id_proposition"},
        unique = true)})
public class Vote {
    @ForeignKey(entity = Proposition.class,
            parentColumns = "id",
            childColumns = "id_proposition")

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int id_user;
    @ColumnInfo(name = "id_proposition")
    private int id_proposition;
    private int forOrAgainst;

    public Vote(int id, int id_user, int id_proposition, int forOrAgainst) {
        this.id = id;
        this.id_user = id_user;
        this.id_proposition = id_proposition;
        this.forOrAgainst = forOrAgainst;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_proposition() {
        return id_proposition;
    }

    public void setId_proposition(int id_proposition) {
        this.id_proposition = id_proposition;
    }

    public int getForOrAgainst() {
        return forOrAgainst;
    }

    public void setForOrAgainst(int forOrAgainst) {
        this.forOrAgainst = forOrAgainst;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", id_user=" + id_user +
                ", id_proposition=" + id_proposition +
                ", forOrAgainst=" + forOrAgainst +
                '}';
    }
}
