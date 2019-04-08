package com.example.revendiquons.ExpandableRecyclerView;

import com.example.revendiquons.room.entity.Proposition;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

// The list to be expanded and its data. In our case, a single proposition with its score and title

public class ExpandableProp extends ExpandableGroup<ExpandedProp> {

    private Proposition proposition;

    public ExpandableProp(Proposition prop, List<ExpandedProp> items) {
        super(prop.getTitle(), items);
        this.proposition = prop;
    }

    public Proposition getProposition() {
        return proposition;
    }

    public int getScore() {
        return proposition.getPositive() - proposition.getNegative();
    }

}
