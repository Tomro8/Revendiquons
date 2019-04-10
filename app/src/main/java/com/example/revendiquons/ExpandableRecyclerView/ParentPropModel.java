package com.example.revendiquons.ExpandableRecyclerView;

import android.util.Log;

import com.example.revendiquons.db.entity.Proposition;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

// The list to be expanded and its data. In our case, proposition's score and title.
// We give a Proposition object as a child, because child will need to forward Proposition to DB

public class ParentPropModel extends ExpandableGroup<ChildPropModel> {

    private int score;
    private Proposition prop;
    //Le titre est dans la classe mère

    public ParentPropModel(Proposition prop, List<ChildPropModel> items) {
        super(prop.getTitle(), items);
        this.prop = prop;
        score = prop.getPositive() - prop.getNegative();
    }

    int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public Proposition getProposition() {
        Log.i("rcl","in parentModel, returning prop: " + prop.toString() + "\n hash: " + prop.hashCode());
        return prop; }

}
