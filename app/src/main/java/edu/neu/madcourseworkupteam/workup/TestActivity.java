package edu.neu.madcourseworkupteam.workup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button mButton;
    private DataService ds;

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
                Boolean loggedIn = userIsLoggedIn();
//               User u = ds.getUser("3WS6DrguO9NWtJ4q3WxsvNNhitH2");
//               if(u != null){
//                   mTextView.setText(u.getUsername());
//               }
//                mTextView.setText("It's been called");

            }
        });
    }

    boolean userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d("UID", "Getting close to UID");
            Log.d("UID", user.getUid());
            mTextView.setText(user.getUid());
            return true;
        }
        Log.d("UID","is null");
        return false;
    }



}