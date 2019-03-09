package com.example.revendiquons.Models;

import android.view.View;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ExpandableProp extends ExpandableGroup<ExpandedProp> {

    public ExpandableProp(String title, List<ExpandedProp> items) {
        super(title, items);
    }



}
