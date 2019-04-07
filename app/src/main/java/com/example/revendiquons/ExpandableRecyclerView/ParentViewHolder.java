package com.example.revendiquons.ExpandableRecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.revendiquons.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class ParentViewHolder extends GroupViewHolder {

    private TextView score_text;
    private TextView title_text;

    public ParentViewHolder(View itemView) {
        super(itemView);
        this.score_text = itemView.findViewById(R.id.proposition_main_score);
        this.title_text = itemView.findViewById(R.id.proposition_main_title);
        Log.i("rcl", "Creating grp");
    }

    public void setTitle(ExpandableGroup group) {
        title_text.setText(group.getTitle());
    }

    public void setScore(ExpandableGroup group) {
        int score = ((ExpandableProp)group).getScore();

        //Convert Score to string
        StringBuilder scoreString = new StringBuilder();
        if (score >= 1000) {
            scoreString.append(Integer.toString(score).charAt(0));
            scoreString.append(',');
            scoreString.append(Integer.toString(score).charAt(1));
            scoreString.append('K');
        } else if (score <= -1000) {
            scoreString.append(Integer.toString(score).charAt(0)); // '-'
            scoreString.append(Integer.toString(score).charAt(1));
            scoreString.append(',');
            scoreString.append(Integer.toString(score).charAt(2));
            scoreString.append('K');
        } else {
            scoreString.append(score);
        }

        //Apply style to textView
        if (score > 0) {
            score_text.setTextAppearance(R.style.ColorScorePositive);
        } else if (score < 0) {
            score_text.setTextAppearance(R.style.ColorScoreNegative);
        } else {
            score_text.setTextAppearance(R.style.ColorScoreNeutral);
        }

        score_text.setText(scoreString);
    }
}
