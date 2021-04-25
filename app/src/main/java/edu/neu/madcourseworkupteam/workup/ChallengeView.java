package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.util.List;

public class ChallengeView extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    private TextView challengeName;
    private TextView startDate;
    private TextView endDate;
    String challengeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_view);
        challengeName = findViewById(R.id.challenge_name_display);
        startDate = findViewById(R.id.start_date_display);
        endDate = findViewById(R.id.end_date_display);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                challengeID = null;
            } else {
                challengeID = extras.getString("challengeID");
            }
        } else {
            challengeID = (String) savedInstanceState.getSerializable("challengeID");
        }

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        try {
            initialItemData(savedInstanceState);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        Intent explore_intent = new Intent(this, LandingPage.class);
                        startActivity(explore_intent);
                        return true;
                    case R.id.navigation_favorite:
                        Intent fav_intent = new Intent(this, FavoriteActivity.class);
                        startActivity(fav_intent);
                        return true;
                    case R.id.navigation_explore:
                        Intent prof_intent = new Intent(this, ExploreScreenActivity.class);
                        startActivity(prof_intent);
                        return true;
                }
                return false;
            };

    private void initialItemData(Bundle savedInstanceState) throws MalformedURLException {
        getChallenge();
    }

    void getChallenge() {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Get challenge on data change!!!", "called");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("Get challenge key", String.valueOf(ds.getKey()));
                    Log.w("ChallengeID:", String.valueOf(challengeID));
                    if (ds.getKey().equals(challengeID)) {
                        Challenge challenge = new Challenge();
                        challenge.setTitle(ds.getValue(Challenge.class).getTitle());
                        challengeName.setText(ds.getValue(Challenge.class).getTitle());
                        challenge.setStartDate(ds.getValue(Challenge.class).getStartDate());
                        startDate.setText(ds.getValue(Challenge.class).getStartDate());
                        challenge.setEndDate(ds.getValue(Challenge.class).getEndDate());
                        endDate.setText(ds.getValue(Challenge.class).getEndDate());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get Movement", "loadPost:onCancelled", databaseError.toException());
            }
        };
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("challenges");
        databaseReference.addValueEventListener(userListener);
    }
}