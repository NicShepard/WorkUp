package edu.neu.madcourseworkupteam.workup;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class ChallengeViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView place;
    public TextView friend1_name;
    public TextView friend2_name;
    public TextView friend3_name;


    public ChallengeViewHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.challenge_date);
        place = itemView.findViewById(R.id.challenge_placement);
        friend1_name = itemView.findViewById(R.id.p1_name);
        friend2_name = itemView.findViewById(R.id.p2_name);
        friend3_name = itemView.findViewById(R.id.p3_name);
    }
}
