package edu.neu.madcourseworkupteam.workup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ExploreScreen ACTIVITY";


    private RecyclerView rView;
    private ArrayList<ChallengeCard> cardList = new ArrayList<>();
    private ChallengeAdapter challengeAdapter;
    private RecyclerView.LayoutManager layout;
    private ImageView profilePic;
    private TextView displayCurrentUser;
    private TextView displayStreak;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private User currentUser;
    private String testUser;
    //private Streak currentStreak;
    private int currentStreak;
    private Date dateToday;

    private DayOfWeek dayToday;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    BottomNavigationView bottomNavigation;

    private DatabaseReference databaseReference;
//    HashMap<String , Object> streakMap = new HashMap<>();


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

        displayStreak = findViewById(R.id.current_streak);

        currentUser = new User();

//        LocalDate today;
//        today = LocalDate.now();
//        Long streak;
//        streak = Long.valueOf(0);
//
//        streakMap.put("date", today.toString());
//        streakMap.put("currStreak", streak);
//
//        databaseReference.child("users").child(currentUserId).child("streak").setValue(streakMap);


        //setStreak();

        //calculateStreak();
        //currentStreak = new Streak();


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
        getPastChallengesForUser();
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

    List<Challenge> getPastChallengesForUser() {
        Log.d("activeChallenges", "called");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List challenges = new LinkedList();

        ValueEventListener challengeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Challenge c = new Challenge();
                    c.setPk(ds.getKey());
                    c.setStartDate(ds.getValue(Challenge.class).getStartDate());
                    c.setEndDate(ds.getValue(Challenge.class).getEndDate());
                    c.setTitle(ds.getValue(Challenge.class).getTitle());
                    c.setID(String.valueOf(ds.getKey()));
                    ChallengeCard item = new ChallengeCard(c.getStartDate() + " - " + c.getEndDate(), c.getTitle(), c.getID());
                    cardList.add(item);
                    challenges.add(c);
                    Log.d("Size of list is", String.valueOf(challenges.size()));
                    Log.d("Size of list is", challenges.toString());
                }
                createRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("pastChallenges");
        databaseReference.addValueEventListener(challengeListener);
        return challenges;
    }

    void setUser() {
        final boolean[] isAlreadyCalled = {false};
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
                currentUser.setStreak(dataSnapshot.getValue(User.class).getStreak());
                Log.d("Streak:", currentUser.getStreak().get("date").toString());

                displayCurrentUser.setText(currentUser.getUsername());
                //currentUser = user;
                //}

                if (!isAlreadyCalled[0]) {
                    updateStreak();
                    isAlreadyCalled[0] = true;
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        databaseReference.child("users").child(currentUserId).addValueEventListener(userListener);
    }

    public void updateStreak() {
        LocalDate today;
        today = LocalDate.now();
        String yesterday = currentUser.getStreak().get("date").toString();
        //LocalDate lastCheckIn;
        //lastCheckIn = LocalDate.parse(yesterday);

        String[] todayArray = today.toString().split("-", 5);
        Log.d("todayArray:", todayArray[2].toString());

        String[] lastUpdateArray = yesterday.split("-", 5);
        Log.d("yesterdayArray:", lastUpdateArray[2].toString());

        int currentCheckIn = parseInt(todayArray[2]);
        Log.d("CheckIn:", String.valueOf(currentCheckIn));

        int lastCheckIn = parseInt(lastUpdateArray[2]);
        Log.d("LastCheckIn:", String.valueOf(lastCheckIn));

        int difference = currentCheckIn - lastCheckIn;
        Log.d("difference:", String.valueOf(difference));

        int monthDiff = parseInt(todayArray[1]) - parseInt(lastUpdateArray[1]);
        Log.d("monthdifference:", String.valueOf(monthDiff));

        if (difference == 1) {
                Log.d("UpdateStreak:", "made it");
                String streakNow = currentUser.getStreak().get("currStreak").toString();
                int streakUp = parseInt(streakNow) + 1;
                databaseReference.child("users").child(currentUserId).child("streak").child("currStreak").setValue(streakUp);
            displayStreak.setText(String.valueOf(streakUp) + " Day Streak");
            } else {
            databaseReference.child("users").child(currentUserId).child("streak").child("currStreak").setValue(0);
            displayStreak.setText(String.valueOf(0));

        }

//        if (monthDiff == 0) {
//            if (difference == 1) {
//                Log.d("UpdateStreak:", "made it");
//                String streakNow = currentUser.getStreak().get("currStreak").toString();
//                int streakUp = parseInt(streakNow) + 1;
//                databaseReference.child("users").child(currentUserId).child("streak").child("currStreak").setValue(streakUp);
//            }
//
//        } else if (monthDiff == 1) {
//            if (lastCheckIn == 28 && currentCheckIn == 1) {
//                String streakNow = currentUser.getStreak().get("currStreak").toString();
//                int streakUp = parseInt(streakNow) + 1;
//                databaseReference.child("users").child(currentUserId).child("streak").child("currStreak").setValue(streakUp);
//            } else if (lastCheckIn == 30 && currentCheckIn == 1) {
//                String streakNow = currentUser.getStreak().get("currStreak").toString();
//                int streakUp = parseInt(streakNow) + 1;
//                databaseReference.child("users").child(currentUserId).child("streak").child("currStreak").setValue(streakUp);
//            } else if (lastCheckIn == 31 && currentCheckIn == 1) {
//                String streakNow = currentUser.getStreak().get("currStreak").toString();
//                int streakUp = parseInt(streakNow) + 1;
//                databaseReference.child("users").child(currentUserId).child("streak").child("currStreak").setValue(streakUp);
//            }
//        } else {
//            databaseReference.child("users").child(currentUserId).child("streak").child("currStreak").setValue(0);
//
//        }

    }

    //TODO try just pushing one date into DB and then retreiving that, comparing it and calculating the streak
    //The Streak node will have a date child and a streakLength child and if date in node is the day before today,
    //update streakLength + 1. If it's not, then update streakLength to 0
    //Optional: third child longestStreak update each time and replace if longer 


}