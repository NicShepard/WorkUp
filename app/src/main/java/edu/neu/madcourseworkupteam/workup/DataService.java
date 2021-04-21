package edu.neu.madcourseworkupteam.workup;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;

public class DataService {

    DatabaseReference mFirebaseDB;
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

            mFirebaseDB.child("users").child(currentUserID).setValue(userMap);
            this.setUser();
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

    void setUser() {
        User user = null;
        Log.d("Username is", "Called" );
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("users").hasChild(currentUserID)) {
                    user.setUsername(dataSnapshot.getValue(User.class).getUsername());
                    user.setFirstName(dataSnapshot.getValue(User.class).getFirstName());
                    user.setLastName(dataSnapshot.getValue(User.class).getLastName());
                    currentUser = user;
                }
//                if(dataSnapshot.getValue() != null){
//                    User user = new User();
//                    user.setUsername(dataSnapshot.getValue(User.class).getUsername());
//                    user.setFirstName(dataSnapshot.getValue(User.class).getFirstName());
//                    user.setLastName(dataSnapshot.getValue(User.class).getLastName());
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        //db.child("users").child(userKey).addValueEventListener(userListener);
        //return user;
        mFirebaseDB.child("users").child(currentUserID).addValueEventListener(userListener);
        //return user;
        //return user[0];
    }

    User getCurrentUser() {
        return currentUser;
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


}