package com.example.revendiquons.Models;

public class Proposition {

    private String title;
    private String description;
    private int upVotes = 0;
    private int downVotes = 0;

    public Proposition() {
        super();
    }

    public Proposition(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Proposition(String title, String description, int upVotes, int downVotes) {
        this.title = title;
        this.description = description;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
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

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }
}
