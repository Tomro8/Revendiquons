package com.example.revendiquons.ExpandableRecyclerView;

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

    public myChildViewHolder(final View itemView) {
        super(itemView);
        this.description = itemView.findViewById(R.id.drop_down_description);
        Log.i("rcl", "Creating child");

        upBtn = itemView.findViewById(R.id.up_vote_btn);
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //La logique est inversée, car le btn est déjà clické lorsque l'on arrive ici
                if (upBtn.isChecked()) {
                    whiteBtn.setChecked(false);
                    downBtn.setChecked(false);
                    upBtn.setChecked(true);
                    //Increase Positive Counter
                } else {
                    upBtn.setChecked(false);
                    //Decrease Positive Counter
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
                    //Increase Neutral Counter
                } else {
                    whiteBtn.setChecked(false);
                    //Decrease Neutral Counter
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
                    //Increase Negative Counter
                } else {
                    downBtn.setChecked(false);
                    //Decrease Negative Counter
                }
            }
        });

    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public void setDescriptionClickListener(View.OnClickListener listener) {
        description.setOnClickListener(listener);
    }

    public void setUpBtnState (boolean checked) {
        upBtn.setChecked(checked);
    }

    public void setDownBtnState (boolean checked) {
        downBtn.setChecked(checked);
    }

    public void setWhiteBtnState (boolean checked) {
        whiteBtn.setChecked(checked);
    }

}
