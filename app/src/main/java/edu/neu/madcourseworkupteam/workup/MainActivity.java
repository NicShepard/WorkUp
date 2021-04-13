package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WorkUp";
    String token;
    private Button login_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//
//        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
//                .setAndroidPackageName("edu.neu.madcourseworkupteam.workup", true,
//                /* minimumVersion= */ null)
//        .setHandleCodeInApp(true) // This must be set to true
//                .setUrl("https://google.com") // This URL needs to be whitelisted
//                .build();
//
//
//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build());

//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setIsSmartLockEnabled(false)
//                        .setAvailableProviders(providers)
//                        .build(),
//                123);


        //Get views
        login_button = (Button) findViewById(R.id.SignInButton);

        TextView currentUser = findViewById(R.id.UserName);

        //Carry the name of the user into the second screen
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start new intent
                Intent intent = new Intent(MainActivity.this, Registration.class);
                intent.putExtra("CURRENT_USER", currentUser.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("UID",user.getUid());
                Intent intent = new Intent(MainActivity.this, Registration.class);
                intent.putExtra("CURRENT_USER", user.getUid());
                startActivity(intent);
            } else {
                Log.d("UID", "Result was not okay");
            }
        }
    }
}