package com.example.revendiquons.ExpandableRecyclerView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

// The list to be expanded and its data. In our case, a single proposition with its score and title

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
