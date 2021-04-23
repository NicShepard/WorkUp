package edu.neu.madcourseworkupteam.workup;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class ChallengeViewHolder extends RecyclerView.ViewHolder {

    public TextView dates;
    public TextView challengeName;
    public TextView friendNames;


    public ChallengeViewHolder(@NonNull View itemView) {
        super(itemView);
        dates = itemView.findViewById(R.id.challenge_dates);
        challengeName = itemView.findViewById(R.id.challenge_name);
        friendNames = itemView.findViewById(R.id.challenge_friends);
    }
}
