package com.example.revendiquons.ExpandableRecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.revendiquons.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class ParentViewHolder extends GroupViewHolder {

    private TextView score;
    private TextView title;

    public ParentViewHolder(View itemView) {
        super(itemView);
        this.score = itemView.findViewById(R.id.proposition_main_score);
        this.title = itemView.findViewById(R.id.proposition_main_title);
        Log.i("Tom", "Creating grp");
    }

    public void setTitle(ExpandableGroup group) {
        this.title.setText(group.getTitle());
    }

    public void setScore(ExpandableGroup group) {
        int scoreInt = ((ExpandableProp)group).getScore();
        this.score.setText( Integer.toString(scoreInt) );
    }
}
