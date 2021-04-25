package edu.neu.madcourseworkupteam.workup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DataService {

    User currentUser;
    String currentUsername;
    DatabaseReference mFirebaseDB;
    Long stepGoal = new Long(5000);
    HashMap<String, String> userMap = new HashMap<>();

    private FirebaseAuth mAuth;

    public DataService() {
        mFirebaseDB = FirebaseDatabase.getInstance().getReference();
    }

    void calculateRankings(String challengeKey) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final Boolean[] rankingCalculated = {false};
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        final Boolean[] finalRankingCalculated = {rankingCalculated[0]};
        ValueEventListener challengeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Challenge c = new Challenge();
                c.setUserPoints(snapshot.getValue(Challenge.class).getUserPoints());
                c.createRankings();
                c.setPk(snapshot.getKey());
                c.setStartDate(snapshot.getValue(Challenge.class).getStartDate());
                c.setEndDate(snapshot.getValue(Challenge.class).getEndDate());
                c.setTitle(snapshot.getValue(Challenge.class).getTitle());

                if (!finalRankingCalculated[0]) {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("challenges").child(challengeKey);
                    db.child("users").child(user.getUid()).child("pastChallenges").child(c.getPk()).setValue(c);
                    db.setValue(c);
                    finalRankingCalculated[0] = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("challenges").child(challengeKey);
        databaseReference.addValueEventListener(challengeListener);
    }

    void archiveChallenges() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        //Get active challenges
        List<Challenge> challenges = new LinkedList();

        ValueEventListener challengeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d("activeChallenges", "insideSnapshot");

                    Challenge c = new Challenge();
                    c.setUserPoints(ds.getValue(Challenge.class).getUserPoints());
                    c.setPk(ds.getKey());
                    c.setStartDate(ds.getValue(Challenge.class).getStartDate());
                    c.setEndDate(ds.getValue(Challenge.class).getEndDate());
                    c.setTitle(ds.getValue(Challenge.class).getTitle());

                    challenges.add(c);
                    Log.d("Size of list is", String.valueOf(challenges.size()));
                    Log.d("Size of list is", challenges.toString());
                }

                //Iterate through all of them
                for (Challenge challenge : challenges) {
                    //Add total steps for each day in the challenge and update it in challenge

                    //Compare dates to see if it is over
                    LocalDate end = LocalDate.parse(challenge.getEndDate());
                    LocalDate today = LocalDate.now();

                    //Create rankings
                    calculateRankings(challenge.getPk());

                    //Add to past challenges in user node, and then delete from active
                    if (today.isAfter(end)) {
                        Log.d("Update Challenges is", "correcting start and end dates");
                        db.child("users").child(user.getUid()).child("pastChallenges").child(challenge.getPk()).setValue(challenge);
                        db.child("users").child(user.getUid()).child("activeChallenges").child(challenge.getPk()).setValue(null);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("activeChallenges");
        databaseReference.addValueEventListener(challengeListener);
    }

    void getCurrentUser() {

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        User user = new User();
        Log.d("Username is", "Called");
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.d("Username is", dataSnapshot.getValue().toString());

                    user.setUsername(dataSnapshot.getValue(User.class).getUsername());
                    currentUsername = dataSnapshot.getValue(User.class).getUsername();
                    user.setFirstName(dataSnapshot.getValue(User.class).getFirstName());
                    user.setLastName(dataSnapshot.getValue(User.class).getLastName());
                    currentUser = user;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        //db.child("users").child(userKey).addValueEventListener(userListener);
        //return user;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child(fbUser.getUid()).addValueEventListener(userListener);
    }

    public String getCurrentUsername(){
        return currentUser.getUsername();
    }



}