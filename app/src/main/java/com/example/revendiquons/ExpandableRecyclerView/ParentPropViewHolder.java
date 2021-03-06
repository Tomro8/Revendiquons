package com.example.revendiquons.ExpandableRecyclerView;

import android.view.View;
import android.widget.TextView;

import com.example.revendiquons.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

class ParentPropViewHolder extends GroupViewHolder {

    private TextView score_text;
    private TextView title_text;

    ParentPropViewHolder(View itemView) {
        super(itemView);
        this.score_text = itemView.findViewById(R.id.proposition_main_score);
        this.title_text = itemView.findViewById(R.id.proposition_main_title);
    }

    void setTitle(String title) {
        title_text.setText(title);
    }

    void setScore(int score) {
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
