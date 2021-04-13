package edu.neu.madcourseworkupteam.workup;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DataService {

    DatabaseReference db;
    User currentUser;

    public DataService() {
        db = FirebaseDatabase.getInstance().getReference();
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

    void registerUser(User user, String uid) {
        db.child("users").child(uid).setValue(user);
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
        db.child("users").child(userKey).addValueEventListener(userListener);
        return user[0];
    }

    User getCurrentUser() {
        return currentUser;
    }

    /******* Movements *******/

    void createMovement(Movement movement) {
        String key = db.child("movements").push().getKey();
        db.child("movements").child(key).setValue(movement);
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
                Iterable i = dataSnapshot.getChildren();
                for (Object m:
                     i) {
                    Log.d("TAG",  m.toString());
                }
                movement[0] = dataSnapshot.getValue(Movement.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };

        DatabaseReference databaseReference = db.child("movements").child("MYAvLIIaCgURQcGK2mw");
        databaseReference.addValueEventListener(userListener);

        return movement[0];
    }


}
