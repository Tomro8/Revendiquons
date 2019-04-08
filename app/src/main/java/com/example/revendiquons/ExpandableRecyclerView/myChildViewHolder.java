package com.example.revendiquons.ExpandableRecyclerView;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.revendiquons.Activities.ChannelActivity;
import com.example.revendiquons.R;
import com.example.revendiquons.ViewModel.ChannelViewModel;
import com.example.revendiquons.repository.PropositionRepository;
import com.example.revendiquons.room.entity.Proposition;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import androidx.lifecycle.ViewModel;

public class myChildViewHolder extends ChildViewHolder {

    private CompoundButton upBtn;
    private CompoundButton whiteBtn;
    private CompoundButton downBtn;
    private TextView description;
    private Proposition proposition;
    private ChannelViewModel channelViewModel;

    public myChildViewHolder(final View itemView) {
        super(itemView);
        this.channelViewModel = ((ChannelActivity) itemView.getContext()).getViewModel();
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
                    Log.i("rcl","Before increasing counter");
                    //proposition.incPositive();
                    //((ChannelActivity) itemView.getContext()).heyhey();
                    //channelViewModel.updateProp(proposition);

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

    public void onBind(Proposition proposition) {
        this.description.setText(proposition.getDescription());
        this.proposition = proposition;
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
