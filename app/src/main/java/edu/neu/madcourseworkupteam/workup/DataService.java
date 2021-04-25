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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DataService {

    DatabaseReference mFirebaseDB;
    Long stepGoal = new Long(5000);
    User currentUser;
    HashMap<String , String> userMap = new HashMap<>();
    private String currentUserID;

    private FirebaseAuth mAuth;

    public DataService() {
        mFirebaseDB = FirebaseDatabase.getInstance().getReference();
    }

    /******* Users *******/
    void createUser() {

    }

    String getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("UID", "Getting close to UID");

        Log.d("UID", user.getUid());
        return user.getUid();
    }

    void registerUser(String firstName, String lastName, String userName) {
        //TODO add a hashmap here to add all the user info
        //String email, String userName and maybe other credentials
        String fName, lName, userName1;
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
           return;
        } else {
            String currentUserEmail = mAuth.getCurrentUser().getEmail();
            String currentUserID = mAuth.getUid();
            //mFirebaseDB.child("users").child("email").setValue(currentUserEmail);

            if (firstName.isEmpty()) {
                fName = "";
            } else fName = firstName;

            if (lastName.isEmpty()) {
                lName = "";
            } else lName = lastName;

            if (userName.isEmpty()) {
                userName1 = "";
            } else userName1 = userName;

            userMap.put("email", currentUserEmail);
            userMap.put("firstName", fName);
            userMap.put("lastName", lName);
            userMap.put("username", userName1);
            userMap.put("stepGoal", String.valueOf(5000));

            mFirebaseDB.child("users").child(currentUserID).setValue(userMap);
            mFirebaseDB.child("users").child(currentUserID).child("stepGoal").setValue(stepGoal);

//            this.setUser();
//                    .addOnCompleteListener( new OnCompleteListener<Void>()
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//            )
//
        }
//
//        mFirebaseDB.child("users").child("username").setValue(userName);
//        mFirebaseDB.child("users").child("email").setValue(email);
    }

    /******* Movements *******/

    void createMovement(Movement movement) {
        String key = mFirebaseDB.child("movements").push().getKey();
        mFirebaseDB.child("movements").child(key).setValue(movement);
    }

    List<Movement> getMovements() {

        List movements = new ArrayList();

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Get movement", "called");

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Movement movement = new Movement();
                    Log.d("Get movement key", String.valueOf(ds.getKey()));
                    Log.d("Size is", String.valueOf(movements.size()));

                    movement.setTitle(ds.getValue(Movement.class).getTitle());
                    movement.setDescription(ds.getValue(Movement.class).getDescription());
                    movement.setDifficulty(ds.getValue(Movement.class).getDifficulty());
                    movement.setVideoURL(ds.getValue(Movement.class).getVideoURL());
                    movement.setType(ds.getValue(Movement.class).getType());
                    movements.add(movement);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get Movement", "loadPost:onCancelled", databaseError.toException());
            }
        };

        //DatabaseReference databaseReference = db.child("movements");

        DatabaseReference databaseReference = mFirebaseDB.child("movements").child("MYAvLIIaCgURQcGK2mw");
        databaseReference.addValueEventListener(userListener);
        Log.d("Size of list is", String.valueOf(movements.size()));

        return movements;

    }

    void archiveChallenges() {
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
                    if(today.isAfter(end)){
                        Log.d("Update Challenges is", "correcting start and end dates");
                        db.child("users").child(user.getUid()).child("pastChallenges").child(challenge.getPk()).setValue(challenge);
                        db.child("users").child(user.getUid()).child("activeChallenges").child(challenge.getPk()).setValue(null);
                    }
                    //Add rank to past challenge
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("activeChallenges");
        databaseReference.addValueEventListener(challengeListener);
    }

    void calculateRankings(String challengeKey) {

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

                    if(!finalRankingCalculated[0]){
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("challenges").child(challengeKey);
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




}