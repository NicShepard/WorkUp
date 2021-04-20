package edu.neu.madcourseworkupteam.workup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.List;

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

        mTextView = findViewById(R.id.text);
        mButton = findViewById(R.id.test);
        ds = new DataService();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long steps = new Long(10);
                updateStepsForDay(steps);
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


}