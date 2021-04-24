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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class LandingPage extends AppCompatActivity {

    Integer goal = 5000;
    ProgressBar simpleProgressBar;
    TextView text_prog;
    private FirebaseAuth mAuth;
    DatabaseReference mFirebaseDB;
    User currentUser = null;
    private String currentUsername;
    ArrayList<User> curUser = new ArrayList<>();
    LocalDate ld;
    FirebaseUser user;

    private static final String TAG = "LANDINGPAGE ACTIVITY";

    private RecyclerView rView;
    private ArrayList<ChallengeCard> cardList = new ArrayList<>();
    private ChallengeAdapter challengeAdapter;
    private RecyclerView.LayoutManager layout;


    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    BottomNavigationView bottomNavigation;

    //private DatabaseReference database;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ld = LocalDate.now();
        String date = ld.toString();
        Log.w("date now:", String.valueOf(ld.toString()));

        text_prog = (TextView) findViewById(R.id.text_view_progress);
        simpleProgressBar = (ProgressBar) findViewById(R.id.progressBar); // initiate the progress bar
        // TODO: To change the user's progress, use .setProgress with new value
        simpleProgressBar.setProgress(0);
        simpleProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You can edit your step goal in your profile!", Toast.LENGTH_LONG).show();
            }
        });

        text_prog.setText("0");
        getStepsForDay();
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
                    case R.id.goChallenges:
                        goToChallenges();
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
                        Intent prof_intent = new Intent(this, ExploreScreenActivity.class);
                        startActivity(prof_intent);
                        return true;
                }
                return false;
            };

    User getCurrentUser() {
        final User[] user = {null};
        final FirebaseUser[] fbUser = {FirebaseAuth.getInstance().getCurrentUser()};

        Log.d("Username is", "Called");
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    User user = new User();
                    user.setUsername(dataSnapshot.getValue(User.class).getUsername());
                    currentUsername = dataSnapshot.getValue(User.class).getUsername();
                    user.setFirstName(dataSnapshot.getValue(User.class).getFirstName());
                    user.setLastName(dataSnapshot.getValue(User.class).getLastName());
                    user.setFavorites(dataSnapshot.getValue(User.class).getFavorites());
                    user.setTotalSteps(dataSnapshot.getValue(User.class).getTotalSteps());
                    user.setStepGoal(dataSnapshot.getValue(User.class).getStepGoal());
                    String goals = String.valueOf(dataSnapshot.getValue(User.class).getStepGoal());
                    goal = Integer.valueOf(goals);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        //return user;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child(fbUser[0].getUid()).addValueEventListener(userListener);
        return user[0];
    }

    void getStepsForDay() {
        List<String> stepGoal = getStepGoal();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ld = LocalDate.now();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Get Steps", "called");

                if (dataSnapshot != null) {
                    if (dataSnapshot.getValue() != null) {
                        Log.d("Get Steps key", String.valueOf(dataSnapshot.getValue()));
                        String str = String.valueOf(dataSnapshot.getValue());
                        text_prog.setText(str);
                        float val = ((float) Integer.valueOf(str) / goal) * 100;
                        int val1 = (int) val;
                        Log.w("Step bar:", String.valueOf(val));
                        simpleProgressBar.setProgress(val1);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get Movement", "loadPost:onCancelled", databaseError.toException());
            }
        };
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getUid()).child("dailySteps").child(ld.toString());
        databaseReference.addValueEventListener(userListener);
    }

    List<String> getStepGoal() {
        Log.d("getStepGoals", "called");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List goals = new LinkedList();

        ValueEventListener challengeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //goals.add(String.valueOf(ds.getKey()));
                    Log.w("STEPGOALS:", String.valueOf(ds.getKey()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("stepGoal");
        databaseReference.addValueEventListener(challengeListener);
        return goals;
    }

    private void initialItemData(Bundle savedInstanceState) throws MalformedURLException {
        getCurrentUser();
        getStepsForDay();
        getActiveChallengesForUser();

    }

    private void createRecyclerView() {

        layout = new LinearLayoutManager(this);
        rView = findViewById(R.id.recyclerView);
        rView.setHasFixedSize(true);

        challengeAdapter = new ChallengeAdapter(cardList);
        rView.setAdapter(challengeAdapter);
        rView.setLayoutManager(layout);

    }

    private void goToProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }

    private void goToChallenges(){
        Intent intent = new Intent(this, ChallengeActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.goProfile) {
            Intent intent = new Intent(LandingPage.this, ProfileActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.goChallenges) {
            Intent intent = new Intent(LandingPage.this, ChallengeActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_signout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(LandingPage.this);
            builder.setMessage("Are you sure you want to logout?");
            builder.setCancelable(true);
            builder.setPositiveButton("Continue using App", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Exit WorkUp", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(LandingPage.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setTextColor(Color.RED);
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(Color.BLUE);
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    List<Challenge> getActiveChallengesForUser() {
        Log.d("activeChallenges", "called");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List challenges = new LinkedList();

        ValueEventListener challengeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d("activeChallenges", "insideSnapshot");

                    Challenge c = new Challenge();
                    c.setPk(ds.getKey());
                    c.setStartDate(ds.getValue(Challenge.class).getStartDate());
                    c.setEndDate(ds.getValue(Challenge.class).getEndDate());
                    c.setTitle(ds.getValue(Challenge.class).getTitle());
                    c.setAccepted(ds.getValue(Challenge.class).getAccepted());
                    ChallengeCard item = new ChallengeCard(c.getStartDate() + " - " + c.getEndDate(), c.getTitle());
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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("activeChallenges");
        databaseReference.addValueEventListener(challengeListener);
        return challenges;
    }

}