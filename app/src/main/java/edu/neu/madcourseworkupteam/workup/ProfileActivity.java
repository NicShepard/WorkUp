package edu.neu.madcourseworkupteam.workup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ExploreScreen ACTIVITY";

    // exercise database reference
    // buttons to filter
    // card clicks

    private RecyclerView rView;
    private ArrayList<ChallengeCard> cardList = new ArrayList<>();
    private ChallengeAdapter challengeAdapter;
    private RecyclerView.LayoutManager layout;
    private ImageView profilePic;
    private TextView displayCurrentUser;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private User currentUser;
    private String testUser;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    BottomNavigationView bottomNavigation;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            return;
        } else {
            currentUserId = mAuth.getUid();
            //databaseReference.child("users").child(currentUserId).get();
            //Log.d("currentUserId is", currentUserId); //this is right
            //setUser();
        }

        testUser = getIntent().getStringExtra("CURRENT_USER");

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        profilePic = findViewById(R.id.profilePic);

        displayCurrentUser = findViewById(R.id.user_name);

        currentUser = new User();


        try {
            initialItemData(savedInstanceState);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //displayCurrentUser.setText(currentUser.getUsername());
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
        setUser();
//        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
//            if (cardList == null || cardList.size() == 0) {
//
//                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
//                for (int i = 0; i < size; i++) {
//                    Integer image = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
//                    ChallengeCard sCard = new ChallengeCard(new Date(), "1st Place", "Cindy, Rob, Alice");
//                    cardList.add(sCard);
//                }
//            }
//        }
//        // Load the initial cards
//        else {
//            //TODO: CHANGE TO CHALLENGE CARD
//            ChallengeCard item1 = new ChallengeCard(new Date(), "", "");
//            ChallengeCard item2 = new ChallengeCard(new Date(), "", "");
//            ChallengeCard item3 = new ChallengeCard(new Date(), "", "");
//            ChallengeCard item4 = new ChallengeCard(new Date(), "", "");
//            cardList.add(item1);
//            cardList.add(item2);
//            cardList.add(item3);
//            cardList.add(item4);
//        }
    }

    private void createRecyclerView() {

        layout = new LinearLayoutManager(this);
        rView = findViewById(R.id.recyclerView);
        rView.setHasFixedSize(true);

        challengeAdapter = new ChallengeAdapter(cardList);
        rView.setAdapter(challengeAdapter);
        rView.setLayoutManager(layout);

    }

    private int addItem(int position) {

        //TODO: CHANGE TO CHALLENGE CARD
        cardList.add(position, new ChallengeCard(new Date(), "1st Place", "Aaron, Nate, Damion"));
        //        Toast.makeText(LinkCollector.this, "Add an item", Toast.LENGTH_SHORT).show();

        challengeAdapter.notifyItemInserted(position);
        return 1;
    }


    void setUser() {
        User user = new User();
        Log.d("Username is", "Called");
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //if(dataSnapshot.child("users").hasChild(currentUserId)) {
                Log.d("currentUserId is", currentUserId);
                currentUser.setUsername(dataSnapshot.getValue(User.class).getUsername());
                currentUser.setFirstName(dataSnapshot.getValue(User.class).getFirstName());
                currentUser.setLastName(dataSnapshot.getValue(User.class).getLastName());
                Log.d("Username is:", currentUser.getUsername());
                displayCurrentUser.setText(currentUser.getUsername());
                //currentUser = user;
                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        databaseReference.child("users").child(currentUserId).addValueEventListener(userListener);
    }

}