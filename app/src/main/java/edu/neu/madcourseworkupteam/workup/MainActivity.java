package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WorkUp";
    String token;
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db = database.getReference("test123");

        db.setValue("Hello, World!");

        User user = new User("Nic", "nbshepard@gmail.com");

        db.child("users").child("1").setValue(user);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object and use the values to update the UI
                User dbuser = dataSnapshot.getValue(User.class);
                Log.w(TAG, dbuser.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        db.addValueEventListener(userListener);

        //Get views
        login_button = (Button) findViewById(R.id.SignInButton);
        TextView currentUser = findViewById(R.id.UserName);

        //Carry the name of the user into the second screen
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start new intent
                Intent intent = new Intent(getApplicationContext(), ExploreScreenActivity.class);
                intent.putExtra("CURRENT_USER", currentUser.getText().toString());
                startActivity(intent);
            }
        });

    }
}