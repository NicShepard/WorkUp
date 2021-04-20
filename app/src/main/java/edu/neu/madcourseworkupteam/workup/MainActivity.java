package edu.neu.madcourseworkupteam.workup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mEmail, mPassword;

    private static final String TAG = "WorkUp";
    String mCustomToken;
    private Button login_button, forgotButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, VideoCard.class);
        startActivity(intent);


        mEmail = findViewById(R.id.UserName);
        mPassword = findViewById(R.id.Password);
        login_button = (Button) findViewById(R.id.SignInButton);
        forgotButton = (Button) findViewById(R.id.ForgotButton);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start new intent
                //Intent intent = new Intent(MainActivity.this, Registration.class);
                //Carry the name of the user into the second screen
                //intent.putExtra("CURRENT_USER", currentUser.getText().toString());
                //startActivity(intent);
                //onStart();
                loginUser();
            }
        });


        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start new intent
                Intent intent = new Intent(MainActivity.this, Registration.class);

            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //String userEmail = mAuth.getCurrentUser().getEmail();
        //Use email to query everything
        //updateUI(currentUser);
    }

    private void loginUser() {
        String email = mEmail.getText().toString();
        String pass = mPassword.getText().toString();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!pass.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(MainActivity.this,
                                        "Login Successfully !!", Toast.LENGTH_SHORT).show();
                                onStart();

                                startActivity(new Intent(MainActivity.this,
                                        LandingPage.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Login Failed!!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,
                                Registration.class));
                    }
                });
            } else {
                mPassword.setError("Empty Fields Are not Allowed");
            }
        } else if (email.isEmpty()) {
            mEmail.setError("Empty Fields Are not Allowed");
        } else {
            mEmail.setError("Pleas Enter Correct Email");
        }
    }


}