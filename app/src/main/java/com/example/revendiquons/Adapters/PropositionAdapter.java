package com.example.revendiquons.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.revendiquons.Models.Proposition;
import com.example.revendiquons.R;

import java.util.List;

public class PropositionAdapter extends RecyclerView.Adapter<PropositionAdapter.AuthViewHolder> {

    private List<Proposition> propositionList;

    public PropositionAdapter(List<Proposition> propositionList) {
        this.propositionList = propositionList;

    }

    public static class AuthViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;

        public AuthViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.test_text);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AuthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("Tom", "CreatingViewHolder: parent: " + parent + " viewType: " + viewType);
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test, parent, false);

        return new AuthViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull AuthViewHolder authViewHolder, int position) {
        Log.i("Tom", "BindingHolder: holder: " + authViewHolder+ " pos: " + position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        authViewHolder.mTextView.setText(propositionList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return propositionList.size();
    }
}
