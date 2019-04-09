package com.example.revendiquons.ExpandableRecyclerView;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.revendiquons.Activities.PropCreationActivity;
import com.example.revendiquons.R;
import com.example.revendiquons.db.AppDatabase;
import com.example.revendiquons.db.entity.Proposition;
import com.example.revendiquons.db.entity.Vote;
import com.example.revendiquons.db.repository.VoteRepository;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ChildPropViewHolder extends ChildViewHolder {

    private CompoundButton upBtn;
    private CompoundButton whiteBtn;
    private CompoundButton downBtn;
    private TextView description;

    private Proposition proposition;
    //private ChannelViewModel channelViewModel;

    ChildPropViewHolder(final View itemView) {
        super(itemView);
        //this.channelViewModel = ((ChannelActivity) itemView.getContext()).getViewModel();
        this.description = itemView.findViewById(R.id.drop_down_description);

        upBtn = itemView.findViewById(R.id.up_vote_btn);
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //La logique est inversée, car le btn est déjà clické lorsque l'on arrive ici
                if (upBtn.isChecked()) {
                    whiteBtn.setChecked(false);
                    downBtn.setChecked(false);

                    //Increase Positive Counter
                    Log.i("rcl","Before increasing counter");

                    //Méthode 2 : changer valeur prop view et refresh UI.
                    //proposition.incPositive();
                    //((ChannelActivity) itemView.getContext()).heyhey();
                    //channelViewModel.updateProp(proposition);

                } else {
                    //Decrease Positive Counter
                }
                //Méthode 1 : update DB
                forwardVote();
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
                    //Increase Neutral Counter
                } else {
                    //Decrease Neutral Counter
                }
                forwardVote();
            }
        });

        downBtn = itemView.findViewById(R.id.down_vote_btn);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (downBtn.isChecked()) {
                    upBtn.setChecked(false);
                    whiteBtn.setChecked(false);
                    //Increase Negative Counter
                } else {
                    //Decrease Negative Counter
                }
                forwardVote();
            }
        });

    }

    void forwardVote() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
        int user_id = preferences.getInt("user_id", -1); //-1 = default value
        Vote vote = new Vote(0, user_id, proposition.getId(), getVoteValue());

        //Adding vote to DB
        VoteRepository.getInstance((Application)itemView.getContext().getApplicationContext()).
                insert(vote);
    }

    void setDescription(String description) {
        this.description.setText(description);
    }

    public void setProposition(Proposition proposition) {
        this.proposition = proposition;
    }

    void setButtonState(int voteValue) {
        Log.i("rcl", "Setting btn state: " + voteValue);

        switch (voteValue) {
            case -1:
                downBtn.setChecked(true);
                upBtn.setChecked(false);
                whiteBtn.setChecked(false);
                break;
            case 2: //Neutral
                downBtn.setChecked(false);
                upBtn.setChecked(false);
                whiteBtn.setChecked(true);
                break;
            case 1:
                downBtn.setChecked(false);
                upBtn.setChecked(true);
                whiteBtn.setChecked(false);
                break;
            case 0:
            default :
                downBtn.setChecked(false);
                upBtn.setChecked(false);
                whiteBtn.setChecked(false);
        }
    }

    void setDescriptionClickListener(View.OnClickListener listener) {
        description.setOnClickListener(listener);
    }

    int getVoteValue() {
        if (upBtn.isChecked()) {
            return 1;
        } else if (downBtn.isChecked()) {
            return -1;
        } else if (whiteBtn.isChecked()) {
            return 2;
        } else {
            return 0;
        }
    }

}
