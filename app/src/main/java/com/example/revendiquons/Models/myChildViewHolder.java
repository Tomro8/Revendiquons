package com.example.revendiquons.Models;

import android.view.View;
import android.widget.TextView;

import com.example.revendiquons.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class myChildViewHolder extends ChildViewHolder {

    private TextView description;

    public myChildViewHolder(View itemView) {
        super(itemView);
        this.description = itemView.findViewById(R.id.child_text);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

}
