package com.example.revendiquons.ExpandableRecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.example.revendiquons.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ExpandablePropAdapter extends ExpandableRecyclerViewAdapter<ParentViewHolder, myChildViewHolder> {

    public ExpandablePropAdapter(final List<? extends ExpandableGroup> groups)
    {
        super(groups);
        //expandableList.expandedGroupIndexes
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
    public ParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        Log.i("rcl", "grpOnCreate");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposition_parent, parent, false);
        return new ParentViewHolder(v);
    }

    @Override
    public myChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        Log.i("rcl", "childOnCreate");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposition_child, parent, false);

        myChildViewHolder child = new myChildViewHolder(v);
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
    public void onBindChildViewHolder(myChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Log.i("rcl", "bindChild");
        ExpandableProp parentGroup = (ExpandableProp) group;
        //Get child from parent
        //final ExpandedProp expandedProp = parentGroup.getItems().get(childIndex);
        holder.onBind(parentGroup.getProposition());
        holder.setUpBtnState(false);
        holder.setDownBtnState(false);
        holder.setWhiteBtnState(false);
    }

    @Override
    public void onBindGroupViewHolder(ParentViewHolder holder, int flatPosition, ExpandableGroup group) {
        Log.i("rcl", "bindGrp");
        holder.setScore(group);
        holder.setTitle(group);
    }
}
