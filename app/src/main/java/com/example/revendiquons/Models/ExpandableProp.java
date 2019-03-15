package com.example.revendiquons.Models;

import android.view.View;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ExpandableProp extends ExpandableGroup<ExpandedProp> {

    private int score;

    public ExpandableProp(String title, int score, List<ExpandedProp> items) {
        super(title, items);
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

}
