package edu.neu.madcourseworkupteam.workup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeViewHolder> {

    private final ArrayList<ChallengeCard> challengeList;
    private ItemClickListener listener;


    public ChallengeAdapter(ArrayList<ChallengeCard> cards) { this.challengeList = cards; }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_card, parent,
                false);
        return new ChallengeViewHolder(view, listener);
        //return null;
    }

    public void setOnClickItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        ChallengeCard currentCard = challengeList.get(position);
        holder.challengeName.setText(currentCard.getChallengeName());
        holder.dates.setText(currentCard.getDate());
        holder.challengeID = currentCard.getChallengeID();
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }
}
