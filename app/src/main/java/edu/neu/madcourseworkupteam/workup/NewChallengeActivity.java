package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This activity is used for creating new challenges. It uses a date, title, and a selection of users
 * to generate each challenge- both in a central node and in each users node as well.
 */
//TODO bottom navigation
public class NewChallengeActivity extends AppCompatActivity implements MultiSelectSpinner.OnMultipleItemsSelectedListener, View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ArrayList<User> friendList = new ArrayList<>();
    private Long start = null;
    private Long end = null;
    private boolean challengeCreated = false;

    private String currentUsername = null;
    private MultiSelectSpinner multiSelectSpinner;

    private Button saveButton;
    BottomNavigationView bottomNavigation;
    private Challenge challengeToAdd;
    private TextView nameOfChallenge;
    private List selectedUsers;
    private HashMap<String, Long> pointsMap;
    Button save;
    Button cancel;

    User currentUser;
    private Button startDate;
    private MaterialDatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_challenge);
        currentUser = getCurrentUser();
        getFriends();

        startDate = (Button) findViewById(R.id.start_date);
        saveButton = findViewById(R.id.save_button);
        nameOfChallenge = findViewById(R.id.name_of_challenge);

        start = null;
        end = null;
        save = (Button) findViewById(R.id.save_button);
        cancel = (Button) findViewById(R.id.cancel_button);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Challenge Dates");
        datePicker = builder.build();

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            }
        });
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                //Get the start end end date by casting to Long
                start = (Long) ((Pair) selection).first;
                end = (Long) ((Pair) selection).second;

                startDate.setText(datePicker.getHeaderText());

                //Create the dates, format them, and add 1 day to correct a quirk with the date picker
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date newStartDate = new Date(start);
                Date newEndDate = new Date(end);
                String startDate = dateFormat.format(newStartDate);
                startDate = LocalDate.parse(startDate).plusDays(1).toString();
                String endDate = dateFormat.format(newEndDate);
                endDate = LocalDate.parse(endDate).plusDays(1).toString();

                if(start < System.currentTimeMillis()){
                    Toast.makeText(NewChallengeActivity.this,
                            "Please choose a start date further the future", Toast.LENGTH_SHORT).show();
                    challengeToAdd = null;
                } else {
                    //Create the challenge to be added
                    challengeToAdd = new Challenge();
                    challengeToAdd.setStartDate(startDate);
                    challengeToAdd.setEndDate(endDate);
                    String selected = multiSelectSpinner.getSelectedItemsAsString();
                    Log.d("Items Selected", selected);
                    challengeToAdd.setUserPoints(pointsMap);

                    if (nameOfChallenge.getText().length() > 0) {
                        challengeToAdd.setTitle(nameOfChallenge.getText().toString());
                    } else {
                        challengeToAdd.setTitle("Unnamed Challenge");
                    }
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(challengeToAdd == null){
                    Toast.makeText(NewChallengeActivity.this,
                            "Please create a valid challenge with a future start date", Toast.LENGTH_LONG).show();
                } else{
                    createChallenge(challengeToAdd);
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        bottomNavigation = findViewById(R.id.bottom_navigation);
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
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


    }


    //TODO uncheck first one, filter out self
    public void addOnSpinnerFriends() {
        multiSelectSpinner = (MultiSelectSpinner) findViewById(R.id.friends);
        ArrayList<String> list = new ArrayList<>();

        for (User u : friendList) {
            if(!u.getUsername().equalsIgnoreCase(currentUsername)){
                list.add(u.getUsername());
            }
        }

        String[] friends = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            friends[i] = list.get(i);
        }

        multiSelectSpinner.setItems(friends);
        multiSelectSpinner.setListener(this);

    }

    public void getFriends() {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Get friends", "called");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User friend = new User();

                    friend.setFirstName(ds.getValue(User.class).getFirstName());
                    friend.setLastName(ds.getValue(User.class).getLastName());
                    friend.setUsername(ds.getValue(User.class).getUsername());

                    friendList.add(friend);
                }
                addOnSpinnerFriends();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get Friends", "loadPost:onCancelled", databaseError.toException());
            }
        };
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addValueEventListener(userListener);
        Log.d("Size of list is", String.valueOf(friendList.size()));
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        selectedUsers = strings;
        selectedUsers.add(currentUsername);
        pointsMap = new HashMap<>();
        for (String selected : strings) {
            pointsMap.put(selected, Long.valueOf(0));
        }
        pointsMap.put(currentUsername, Long.valueOf(0));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();


    }

    //PLAN TODO
    //Save instance state TODO
    //https://stackoverflow.com/questions/32283853/android-save-state-on-orientation-change

    /**
     * Put challenge in own node
     * Add challenge to all user's nodes
     * For each user, on visit the app it will calculate the total steps from start to now and
     * update their map. If it is past the last day, update the map, create a sorted list of rankings.
     * <p>
     * A user declining the challenge will delete it from the map, and from their user node
     * <p>
     * Create active and past challenges
     */

    void createChallenge(Challenge challenge) {

        Log.d("Create challenge", "called");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Challenge cCopy = new Challenge();

        cCopy.setTitle(challenge.getTitle());
        cCopy.setStartDate(challenge.getStartDate());
        cCopy.setEndDate(challenge.getEndDate());


        String key = db.child("users").child(user.getUid()).child("challenges").push().getKey();

        db.child("challenges").child(key).setValue(challenge);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User friend = new User();
                    Log.d("Get friend key", String.valueOf(ds.getKey()));

                    String currentKey = String.valueOf(ds.getKey());

                    for (Object user : selectedUsers) {
                        if (ds.getValue(User.class).getUsername().equalsIgnoreCase(String.valueOf(user)) && !challengeCreated) {
                            databaseReference.child("users").child(currentKey).child("activeChallenges").child(key).setValue(cCopy);
                        }
                    }
                }
                challengeCreated = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get Friends", "loadPost:onCancelled", databaseError.toException());
            }
        };
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addValueEventListener(userListener);
    }

    User getCurrentUser() {
        final User[] user = {null};
        final FirebaseUser[] fbUser = {FirebaseAuth.getInstance().getCurrentUser()};

        Log.d("Username is", "Called");
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    user[0] = new User();
                    user[0].setUsername(dataSnapshot.getValue(User.class).getUsername());
                    currentUsername = dataSnapshot.getValue(User.class).getUsername();
                    user[0].setFirstName(dataSnapshot.getValue(User.class).getFirstName());
                    user[0].setLastName(dataSnapshot.getValue(User.class).getLastName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        //db.child("users").child(userKey).addValueEventListener(userListener);
        //return user;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child(fbUser[0].getUid()).addValueEventListener(userListener);
        return user[0];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                finish();
        }
    }

}