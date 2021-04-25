package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChallengeView extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    private TextView challengeName;
    private TextView startDate;
    private TextView endDate;
    String challengeID;
    Map<String, Long> userPoints;
    private TextView user;
    private ListView list;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_view);
        challengeName = findViewById(R.id.challenge_name_display);
        startDate = findViewById(R.id.start_date_display);
        endDate = findViewById(R.id.end_date_display);
        list = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);

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
                        challenge.setStartDate(ds.getValue(Challenge.class).getStartDate());
                        challenge.setEndDate(ds.getValue(Challenge.class).getEndDate());
                        challenge.setUserPoints(ds.getValue(Challenge.class).getUserPoints());

                        challengeName.setText(ds.getValue(Challenge.class).getTitle());
                        startDate.setText(ds.getValue(Challenge.class).getStartDate());
                        endDate.setText(ds.getValue(Challenge.class).getEndDate());
                        userPoints = challenge.getUserPoints();
                        for (Map.Entry<String, Long> user : userPoints.entrySet()) {
                            listItems.add(user.getKey() + ": " + user.getValue());
                            adapter.notifyDataSetChanged();
                        }

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