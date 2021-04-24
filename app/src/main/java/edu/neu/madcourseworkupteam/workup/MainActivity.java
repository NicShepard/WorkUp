package edu.neu.madcourseworkupteam.workup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The main activity is the entrance to the app, it allows the user login or create an account. If
 * the user is already logged in, they will skip this screen and be taking directly to their landing
 * page.
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEmail, mPassword;
    private Button login_button, registerButton;

    //Set up UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        login_button = (Button) findViewById(R.id.signInButton);
        registerButton = (Button) findViewById(R.id.register);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start new intent
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });
    }

    //Check to see if there is a current user, if so, take them directly to their landing page
    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(MainActivity.this,
                    LandingPage.class));
        }
    }

    //Login the user using Firebase authentication, authentication is currently supported with
    //username and password only.
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
                                        "Welcome back! You've taken the first step", Toast.LENGTH_SHORT).show();
                                onStart();
                                startActivity(new Intent(MainActivity.this,
                                        LandingPage.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Login failed, please try again",
                                Toast.LENGTH_SHORT).show();
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