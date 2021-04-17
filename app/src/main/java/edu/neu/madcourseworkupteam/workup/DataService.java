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

import java.util.HashMap;
import java.util.List;

public class DataService {

    DatabaseReference mFirebaseDB;
    User currentUser;
    HashMap<String , String> userMap = new HashMap<>();

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

            userMap.put("Email", currentUserEmail);
            userMap.put("FirstName", fName);
            userMap.put("LastName", lName);
            userMap.put("Username", userName1);

            mFirebaseDB.child("users").push().setValue(userMap);
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

    User getUser(String userKey) {
        final User[] user = new User[1];

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user[0] = dataSnapshot.getValue(User.class);
                currentUser = user[0];
                Log.d("TAG", user[0].getUsername() + " found");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mFirebaseDB.child("users").child(userKey).addValueEventListener(userListener);
        return user[0];
    }

    User getCurrentUser() {
        return currentUser;
    }

    /******* Movements *******/

    void createMovement(Movement movement) {
        String key = mFirebaseDB.child("movements").push().getKey();
        mFirebaseDB.child("movements").child(key).setValue(movement);
    }

//    List<Movement> getMovements() {
//
//        List<Movement> movements;
//
//        ValueEventListener userListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                user[0] = dataSnapshot.getValue(User.class);
//                currentUser = user[0];
//                Log.d("TAG", user[0].getUsername() + " found");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        db.child("users").child(userKey).addValueEventListener(userListener);
//        return user[0];
//    }

    Movement getMovement(String key) {

        final Movement[] movement = new Movement[1];

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG", "DATA CHANGED");
                Log.d("TAG", String.valueOf(dataSnapshot.getKey()));
                Log.d("TAG", String.valueOf(dataSnapshot.getValue()));
                movement[0] = dataSnapshot.getValue(Movement.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };

        DatabaseReference databaseReference = mFirebaseDB.child("movements").child("MYAvLIIaCgURQcGK2mw");
        databaseReference.addValueEventListener(userListener);

        return movement[0];
    }


}