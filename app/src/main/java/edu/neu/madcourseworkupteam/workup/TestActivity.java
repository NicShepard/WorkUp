package edu.neu.madcourseworkupteam.workup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class TestActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button mButton;
    private DataService ds;
    FirebaseUser user;
    LocalDate ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        startActivity(new Intent(TestActivity.this,
                NewChallengeActivity.class));

        mTextView = findViewById(R.id.text);
        mButton = findViewById(R.id.test);
        ds = new DataService();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateChallenges();
            }
        });
    }

    boolean userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d("UID", "Getting close to UID");
            Log.d("UID", user.getUid());
            mTextView.setText(user.getUid());
            return true;
        }
        Log.d("UID", "is null");
        return false;
    }

    void getStepsForDay() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        ld = LocalDate.now();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Get Steps", "called");

                if (dataSnapshot != null) {
                    Log.d("Get Steps key", String.valueOf(dataSnapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get Movement", "loadPost:onCancelled", databaseError.toException());
            }
        };
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getUid()).child("dailySteps").child(ld.toString());
        databaseReference.addValueEventListener(userListener);
    }

    void setStepsForDay() {
        ld = LocalDate.now();
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getUid()).child("dailySteps").child(ld.toString());
        databaseReference.setValue("0");
    }

    void updateStepsForDay(Long stepsToAdd) {

        final Boolean[] alreadyRun = {false};

        final Long[] existingSteps = new Long[1];
        user = FirebaseAuth.getInstance().getCurrentUser();
        ld = LocalDate.now();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getUid()).child("dailySteps").child(ld.toString());

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Get Steps", "called");

                if (dataSnapshot != null && !alreadyRun[0]) {
                    existingSteps[0] = (Long) dataSnapshot.getValue();
                    databaseReference.setValue(stepsToAdd + existingSteps[0]);
                    alreadyRun[0] = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get Movement", "loadPost:onCancelled", databaseError.toException());
            }
        };

        databaseReference.addValueEventListener(userListener);
    }



    //TODO Async doesn't matter here, just need to perform all the functions.
    //Move to dataservice?
    void updateChallenges() {
        Log.d("Update Challenges is", "Called");

        Integer rank;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        //Get current date to compare everything against
        LocalDate ld;
        ld = LocalDate.now();

        //Get active challenges
        List<Challenge> challenges = new LinkedList();

        ValueEventListener challengeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d("activeChallenges", "insideSnapshot");

                    Challenge c = new Challenge();
                    c.setUserPoints(ds.getValue(Challenge.class).getUserPoints());
                    c.createRankings();
                    c.setPk(ds.getKey());
                    c.setStartDate(ds.getValue(Challenge.class).getStartDate());
                    c.setEndDate(ds.getValue(Challenge.class).getEndDate());
                    c.setTitle(ds.getValue(Challenge.class).getTitle());
                    c.setUserPoints(ds.getValue(Challenge.class).getUserPoints());

                    challenges.add(c);
                    Log.d("Size of list is", String.valueOf(challenges.size()));
                    Log.d("Size of list is", challenges.toString());
                }

                //Iterate through all of them
                for (Challenge challenge : challenges) {
                    //Add total steps for each day in the challenge and update it in challenge

                    //Compare dates to see if it is over
                    LocalDate start = LocalDate.parse(challenge.getStartDate());
                    LocalDate end = LocalDate.parse(challenge.getEndDate());
                    LocalDate today = LocalDate.now();

                    //If challenge is over create ranking list in challenge

                    Log.d("Update Challenges is", "evaluating dates");

                    Log.d("Update Challenges is", today.toString());
                    Log.d("Update Challenges is", end.toString());

                    //Add to past challenges in user node, and then delete from active
                    if(today.isAfter(end)){
                        Log.d("Update Challenges is", "correcting start and end dates");
                        challenge.createRankings();
                        db.child("users").child(user.getUid()).child("pastChallenges").child(challenge.getPk()).setValue(challenge);
                        db.child("users").child(user.getUid()).child("activeChallenges").child(challenge.getPk()).setValue(null);

                    }
                    //Add rank to past challenge
                    //Send FCM to say, come see how you did!
                    //End of challenge make sure they log their steps?
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("activeChallenges");
        databaseReference.addValueEventListener(challengeListener);
    }



}