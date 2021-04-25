package edu.neu.madcourseworkupteam.workup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.HashMap;

public class Registration extends AppCompatActivity {
    DatabaseReference rootNode;
    private FirebaseAuth mAuth;

    Button registerButton;
    EditText fName;
    EditText lName;
    EditText username;
    EditText email;
    EditText password;
    int steps;

    DataService dataService;
    HashMap<String , Object> streakMap = new HashMap<>();

    private String mEmail, mPassword, mFirstName, mLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //TODO get rid of this later
        //rootNode = FirebaseDatabase.getInstance().getReference();

        steps = 0;
        dataService = new DataService();

        //Log.d("UID: ", uid);
        registerButton = findViewById(R.id.registerButton);
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        username = findViewById(R.id.regUsername);
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);

        mAuth = FirebaseAuth.getInstance();


        //This is the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rootNode.child("users").child("newChild").setValue("Hello World");
//                m = dataService.getMovement("MYAvLIIaCgURQcGK2mw");
                createUser();
            }
        });
    }

    private void createUser() {
        String tempEmail = email.getText().toString();
        String tempPass = password.getText().toString();
        //String tempEmail = "cat@gmail.com";
        //String tempPass = "catcat";
        Log.d("email: ", tempEmail);
        Log.d("password: ", tempPass);


        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mFirebaseDB = FirebaseDatabase.getInstance().getReference();

        if (!tempEmail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches()) {
            if (!tempPass.isEmpty()) {
                Log.d("in the if state: ", tempPass);
                mAuth.createUserWithEmailAndPassword(tempEmail, tempPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Registration.this,
                                        "Registered Successfully !!", Toast.LENGTH_SHORT).show();
                                // params: first name, last name and username
                                mAuth = FirebaseAuth.getInstance();
                                if (mAuth.getCurrentUser() == null) {
                                    return;
                                } else {
                                    User u = new User((fName.getText().toString()),
                                            lName.getText().toString(), username.getText().toString());
                                    u.setStepGoal(Long.valueOf(5000));
                                    mFirebaseDB.child("users").child(mAuth.getUid()).setValue(u);
                                    LocalDate today;
                                    today = LocalDate.now();
                                    Long streak;
                                    streak = Long.valueOf(0);
                                    streakMap.put("date", today.toString());
                                    streakMap.put("currStreak", streak);
                                    mFirebaseDB.child("users").child(mAuth.getUid()).child("streak").setValue(streakMap);

                                }

                                startActivity(new Intent(Registration.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registration.this,
                                "Registration Error !!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                password.setError("Empty Fields Are not Allowed");
            }
        } else if (email.getText().toString().isEmpty()) {
            email.setError("Empty Fields Are not Allowed");
        } else {
            email.setError("Pleas Enter Correct Email");
        }
    }

}