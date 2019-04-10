package com.example.revendiquons.ExpandableRecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.example.revendiquons.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ExpandablePropAdapter extends ExpandableRecyclerViewAdapter<ParentPropViewHolder, ChildPropViewHolder> {

    public ExpandablePropAdapter(final List<? extends ExpandableGroup> groups)
    {
        super(groups);
        setOnGroupExpandCollapseListener(new GroupExpandCollapseListener() {
            /**
             *  Lorsque l'on étend le groupe, on regarde si les autres groupes sont étendus et on les ferme.
             */
            @Override
            public void onGroupExpanded(ExpandableGroup group) {
                for (ExpandableGroup grp : groups) {
                    if (grp != group && isGroupExpanded(grp)) {
                        toggleGroup(grp);
                    }
                }
            }

            @Override
            public void onGroupCollapsed(ExpandableGroup group) {

            }
        });
    }

    @Override
    public ParentPropViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        Log.i("rcl", "Creating group");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposition_parent, parent, false);
        return new ParentPropViewHolder(v);
    }

    @Override
    public ChildPropViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        Log.i("rcl", "Creating Child");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposition_child, parent, false);

        ChildPropViewHolder child = new ChildPropViewHolder(v);
        child.setDescriptionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ExpandableGroup grp : getGroups()) {
                    if (isGroupExpanded(grp)) {
                        toggleGroup(grp);
                    }
                }
            }
        });

        return child;
    }

    @Override
    public void onBindChildViewHolder(ChildPropViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        //Get children (in our case child) of corresponding group
        ChildPropModel childModel = (ChildPropModel) group.getItems().get(childIndex);
        Log.i("rcl", "binding Child of desc: '" + childModel.getDescription() + "' to ChildViewHolder");

        //Link child data to its viewHolder
        holder.setProposition(((ParentPropModel)group).getProposition());
        holder.setDescription(childModel.getDescription());
        holder.setButtonState(childModel.getVoteValue());
        holder.setVoteValue(childModel.getVoteValue());
    }

    @Override
    public void onBindGroupViewHolder(ParentPropViewHolder holder, int flatPosition, ExpandableGroup group) {
        Log.i("rcl", "Binding parent of title: '" + group.getTitle() + "' to group");
        holder.setScore(((ParentPropModel)group).getScore());
        holder.setTitle(group.getTitle());
    }
}
