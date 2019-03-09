package com.example.revendiquons.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.example.revendiquons.Models.ExpandableProp;
import com.example.revendiquons.Models.ExpandedProp;
import com.example.revendiquons.Models.ParentViewHolder;
import com.example.revendiquons.Models.myChildViewHolder;
import com.example.revendiquons.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ExpandablePropAdapter extends ExpandableRecyclerViewAdapter<ParentViewHolder, myChildViewHolder> {

    public ExpandablePropAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public ParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent, parent, false);
        return new ParentViewHolder(v);
    }

    @Override
    public myChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child, parent, false);
        return new myChildViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(myChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ExpandedProp expandedProp = ((ExpandableProp) group).getItems().get(childIndex);
        holder.setDescription(expandedProp.getDescription());
    }

    @Override
    public void onBindGroupViewHolder(ParentViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTitle(group);
    }
}
