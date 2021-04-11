package edu.neu.madcourseworkupteam.workup;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class DataService {

    FirebaseDatabase db;


    public DataService() {
        db = FirebaseDatabase.getInstance();
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


}
