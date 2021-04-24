package edu.neu.madcourseworkupteam.workup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;

public class LandingPage extends AppCompatActivity {

    ProgressBar simpleProgressBar;
    TextView text_prog;
    private FirebaseAuth mAuth;
    DatabaseReference mFirebaseDB;

    private static final String TAG = "LANDINGPAGE ACTIVITY";

    private RecyclerView rView;
    private ArrayList<ChallengeCard> cardList = new ArrayList<>();
    private ChallengeAdapter challengeAdapter;
    private RecyclerView.LayoutManager layout;
    private String currentUser;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    BottomNavigationView bottomNavigation;

    //private DatabaseReference database;


    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        text_prog = (TextView) findViewById(R.id.text_view_progress);
        simpleProgressBar = (ProgressBar) findViewById(R.id.progressBar); // initiate the progress bar
        // TODO: To change the user's progress, use .setProgress with new value
        simpleProgressBar.setProgress(40);
        text_prog.setText("40%");
        currentUser = getIntent().getStringExtra("CURRENT_USER");
        createRecyclerView();

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.goProfile:
                        goToProfile();
                        break;
                    case R.id.gotToExplore:
                        goToExplore();
                        break;
                }
                return true;
            }
        });


        mFirebaseDB = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Toast.makeText(LandingPage.this,
                    "Current user is null", Toast.LENGTH_SHORT).show();
        } else {
            String emailCheck = currentUser.getEmail();
            Toast.makeText(LandingPage.this,
                    "Current user is:" + emailCheck, Toast.LENGTH_SHORT).show();


            Query query = mFirebaseDB.child("users").orderByChild("email").equalTo(emailCheck);

            String x = query.toString();

            //query.addListenerForSingleValueEvent(valueEventListener);

            //Ok here is the issue:
            //String rtdbEmail = mFirebaseDB.child("users").child(emailCheck).get().toString();
            Toast.makeText(LandingPage.this,
                    "Current user is:" + x, Toast.LENGTH_SHORT).show();


        }

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
                        Intent prof_intent = new Intent(this, ChallengeActivity.class);
                        startActivity(prof_intent);
                        return true;
                }
                return false;
            };

    private void initialItemData(Bundle savedInstanceState) throws MalformedURLException {
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (cardList == null || cardList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
                size = cardList.size();
                for (int i = 0; i < size; i++) {
                    Integer image = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
                    ChallengeCard sCard = new ChallengeCard(new Date(), "1st Place", "Cindy, Rob, Alice");
                    cardList.add(sCard);
                }
            }
        }
        // Load the initial cards
        else {
            ChallengeCard item1 = new ChallengeCard(new Date(), "1st Place", "");
            ChallengeCard item2 = new ChallengeCard(new Date(), "2nd Place", "");
            ChallengeCard item3 = new ChallengeCard(new Date(), "3rd Place", "");
            ChallengeCard item4 = new ChallengeCard(new Date(), "4th Place", "");
            cardList.add(item1);
            cardList.add(item2);
            cardList.add(item3);
            cardList.add(item4);
        }
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

        cardList.add(position, new ChallengeCard(new Date(), "1st Place", "Aaron, Nate, Damion"));

        challengeAdapter.notifyItemInserted(position);
        return 1;
    }

    private void goToProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }

    private void goToExplore(){
        Intent intent = new Intent(this, ExploreScreenActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}