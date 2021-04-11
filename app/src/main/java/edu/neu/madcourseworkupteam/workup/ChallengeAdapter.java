package edu.neu.madcourseworkupteam.workup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeViewHolder> {

    private final ArrayList<ChallengeCard> challengeList;

    public ChallengeAdapter(ArrayList<ChallengeCard> cards) { this.challengeList = cards; }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_card, parent,
                false);
        return new ChallengeViewHolder(view);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        ChallengeCard currentCard = challengeList.get(position);
        //holder.video.setImageResource(currentCard.getImageSource());

    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }
}