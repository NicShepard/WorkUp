package edu.neu.madcourseworkupteam.workup;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataService {

    DatabaseReference db;


    public DataService() {
        db = FirebaseDatabase.getInstance().getReference();
    }

    /******* Users *******/
    void createUser(){

    }

    String getUID(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("UID","Getting close to UID");

        Log.d("UID",user.getUid());
        return user.getUid();
    }

    void registerUser(User user, String uid){
        db.child("users").child(uid).setValue(user);
    }

}
