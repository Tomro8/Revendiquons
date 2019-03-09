package com.example.revendiquons.Models;

import android.view.View;
import android.widget.TextView;

import com.example.revendiquons.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class ParentViewHolder extends GroupViewHolder {

    private TextView title;

    public ParentViewHolder(View itemView) {
        super(itemView);
        this.title = itemView.findViewById(R.id.parent_text);
    }

    public void setTitle(ExpandableGroup group) {
        this.title.setText(group.getTitle());
    }
}
