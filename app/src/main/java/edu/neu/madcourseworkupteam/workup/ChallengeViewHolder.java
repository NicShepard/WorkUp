package edu.neu.madcourseworkupteam.workup;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ChallengeViewHolder extends RecyclerView.ViewHolder {

    public TextView dates;
    public TextView challengeName;
    public TextView friendNames;
    public String challengeID;


    public ChallengeViewHolder(@NonNull View itemView, final ItemClickListener listener) {
        super(itemView);
        dates = itemView.findViewById(R.id.challenge_dates);
        challengeName = itemView.findViewById(R.id.challenge_name);
        friendNames = itemView.findViewById(R.id.challenge_friends);


        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    listener.onItemClick(position);
                    Intent intent = new Intent(itemView.getContext(), ChallengeView.class);
                    intent.putExtra("challengeID", challengeID);
                    itemView.getContext().startActivity(intent);

                }
            }
        });
    }

}
