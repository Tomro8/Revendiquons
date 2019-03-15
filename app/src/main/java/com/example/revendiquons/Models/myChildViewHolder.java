package com.example.revendiquons.Models;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.revendiquons.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class myChildViewHolder extends ChildViewHolder {

    private CompoundButton upBtn;
    private CompoundButton whiteBtn;
    private CompoundButton downBtn;
    private TextView description;

    public myChildViewHolder(View itemView) {
        super(itemView);
        this.description = itemView.findViewById(R.id.drop_down_description);

        upBtn = itemView.findViewById(R.id.up_vote_btn);
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //La logique est inversée, car le btn est déjà clické lorsque l'on arrive ici
                if (upBtn.isChecked()) {
                    whiteBtn.setChecked(false);
                    downBtn.setChecked(false);
                    upBtn.setChecked(true);
                } else {
                    upBtn.setChecked(false);
                }
            }
        });

        whiteBtn = itemView.findViewById(R.id.white_vote_btn);
        whiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Tom", "btn2 clicked");
                if (whiteBtn.isChecked()) {
                    upBtn.setChecked(false);
                    downBtn.setChecked(false);
                    whiteBtn.setChecked(true);
                } else {
                    whiteBtn.setChecked(false);
                }
            }
        });

        downBtn = itemView.findViewById(R.id.down_vote_btn);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (downBtn.isChecked()) {
                    upBtn.setChecked(false);
                    whiteBtn.setChecked(false);
                    downBtn.setChecked(true);
                } else {
                    downBtn.setChecked(false);
                }
            }
        });
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

}
