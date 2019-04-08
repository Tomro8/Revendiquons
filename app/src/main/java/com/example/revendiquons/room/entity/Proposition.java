package com.example.revendiquons.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Proposition {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int user_id;
    private String title;
    private String description;
    private int positive;
    private int negative;

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
                '}';
    }
}
