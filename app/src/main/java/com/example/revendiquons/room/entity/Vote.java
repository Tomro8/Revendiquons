package com.example.revendiquons.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Vote {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int id_user;
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
