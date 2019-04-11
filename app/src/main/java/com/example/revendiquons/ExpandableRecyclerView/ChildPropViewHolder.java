package com.example.revendiquons.ExpandableRecyclerView;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.revendiquons.Activities.ChannelActivity;
import com.example.revendiquons.Activities.PropCreationActivity;
import com.example.revendiquons.R;
import com.example.revendiquons.WebService;
import com.example.revendiquons.db.AppDatabase;
import com.example.revendiquons.db.entity.Proposition;
import com.example.revendiquons.db.entity.Vote;
import com.example.revendiquons.db.repository.DBBeforeOperation;
import com.example.revendiquons.db.repository.DBOperationCallback;
import com.example.revendiquons.db.repository.PropositionRepository;
import com.example.revendiquons.db.repository.VoteRepository;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

public class ChildPropViewHolder extends ChildViewHolder {

    private CompoundButton upBtn;
    private CompoundButton whiteBtn;
    private CompoundButton downBtn;
    private TextView description;
    private int voteValue;

    private Proposition proposition;

    ChildPropViewHolder(final View itemView) {
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

                    //Increase Positive Counter
                    proposition.incPositive();
                } else {
                    //Decrease Positive Counter
                    proposition.decPositive();
                }
                forwardVote();
            }
        });

        //todo: remove white btn ?
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
                    proposition.incNegative();
                } else {
                    //Decrease Negative Counter
                    proposition.decNegative();
                }
                forwardVote();
            }
        });

    }

    private void forwardVote() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
        int user_id = preferences.getInt("user_id", -1); //-1 = default value
        updateVoteValue();
        final Vote vote = new Vote(0, user_id, proposition.getId(), voteValue);

        //Adding vote to DB
        VoteRepository.getInstance((Application)itemView.getContext().getApplicationContext()).
                insert(vote);

        //Forward vote to remote
        WebService.getInstance(itemView.getContext().getApplicationContext()).forwardVoteToAPI(vote, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);

                    if (json.has("success")) {
                        Log.i("volley","Successfully forwared vote to remote:" + vote);
                    } else {
                        Log.i("volley","Failed to forward vote to remote");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //Forward score updated Prop to DB
        PropositionRepository.getInstance((Application)itemView.getContext().getApplicationContext()).
                update(proposition, new DBOperationCallback() {
                    @Override
                    public void onOperationCompleted() {

                    }
                }, new DBBeforeOperation() {
                    @Override
                    public void onPreExecute() {
                        ((ChannelActivity) itemView.getContext()).removeLoadPropositionObserver();
                        ((ChannelActivity) itemView.getContext()).setRefreshPropositionObserver();
                    }
                });
    }

    void setDescription(String description) {
        this.description.setText(description);
    }

    public void setProposition(Proposition proposition) {
        this.proposition = proposition;
    }

    void setVoteValue(int voteValue) {
        this.voteValue = voteValue;
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

    private void updateVoteValue() {
        if (upBtn.isChecked()) {
            voteValue = 1;
        } else if (downBtn.isChecked()) {
            voteValue = -1;
        } else if (whiteBtn.isChecked()) {
            voteValue = 2;
        } else {
            voteValue = 0;
        }
        Log.i("rcl","in child view, vote value updated to: " + voteValue);
    }


}
