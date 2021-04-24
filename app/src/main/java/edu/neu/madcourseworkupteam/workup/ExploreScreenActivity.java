package edu.neu.madcourseworkupteam.workup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ExploreScreenActivity extends AppCompatActivity {

    private static final String TAG = "ExploreScreen ACTIVITY";

    private DatabaseReference databaseReference;
    User currentUser = null;
    private String currentUsername;



    private RecyclerView rView;
    private ArrayList<ExerciseCard> cardList = new ArrayList<>();
    private ArrayList<ExerciseCard> listCopy = new ArrayList<>();

    private ArrayList<ExerciseCard> filteredList = new ArrayList<>();

    private ExerciseAdapter exerciseAdapter;
    private RecyclerView.LayoutManager layout;

    private ChipGroup chipGroup;


    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_screen);
        currentUser = getCurrentUser();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        chipGroup = findViewById(R.id.chip_group);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    Log.w("Chip clicked", String.valueOf(chip.getText()));
                    Toast.makeText(getApplicationContext(), "Chip is " + chip.getChipText(), Toast.LENGTH_LONG).show();
                    filteredList.clear();
                    getFilteredMovements(chip.getText().toString().toLowerCase());
                } else {
                    createRecyclerView(cardList);
                }
            }
        });

        init(savedInstanceState);
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



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int size = cardList == null ? 0 : cardList.size();
        int size2 = filteredList == null ? 0 : filteredList.size();

        outState.putInt(NUMBER_OF_ITEMS, size);

        // Need to generate unique key for each item
        for (int i = 0; i < size; i++) {
            // put image information id into instance
            outState.putString(KEY_OF_INSTANCE + i + "0", cardList.get(i).getVideoUrl());
            // put itemName information into instance
            outState.putString(KEY_OF_INSTANCE + i + "1", cardList.get(i).getName());
            // put itemDesc information into instance
            outState.putString(KEY_OF_INSTANCE + i + "2", cardList.get(i).getDesc());
            // put isChecked information into instance
            outState.putBoolean(KEY_OF_INSTANCE + i + "3", cardList.get(i).getStatus());
        }
        super.onSaveInstanceState(outState);
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
                    user[0].setFavorites(dataSnapshot.getValue(User.class).getFavorites());
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

    private void init(Bundle savedInstanceState) {
        initialItemData(savedInstanceState);
    }

    private void initialItemData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (cardList == null || cardList.size() == 0) {
                Log.w("GOING INTO THIS WEIRD IF STATEMENT", "ATTEMPT");
                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
                // Retrieve keys we store in the instance
                for (int i = 0; i < size; i++) {
                    String videoUrl = savedInstanceState.getString(KEY_OF_INSTANCE + i + "0");
                    String itemName = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    String itemDesc = savedInstanceState.getString(KEY_OF_INSTANCE + i + "2");
                    boolean isChecked = savedInstanceState.getBoolean(KEY_OF_INSTANCE + i + "3");

                    ExerciseCard itemCard = new ExerciseCard(videoUrl, itemName, itemDesc, isChecked);
                    cardList.add(itemCard);
                }
            }
        }
        // Load the initial cards
        else {
            getMovements();
        }
    }

    List<String> getFavorites() {
        Log.d("favorites", "called");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List favorites = new LinkedList();

        ValueEventListener challengeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    favorites.add(ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("favorites");
        databaseReference.addValueEventListener(challengeListener);
        return favorites;
    }

    void getFilteredMovements(String filter) {
        List<String> favorites = getFavorites();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Get filtered movement", "called");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.w("filter: ", String.valueOf(filter));
                    Log.w("category: ", String.valueOf(ds.getValue(Movement.class).getType()));
                    if (String.valueOf(ds.getValue(Movement.class).getType()) == filter) {
                        ExerciseCard movement = new ExerciseCard();
                        Log.d("Get movement key", String.valueOf(ds.getKey()));
                        Log.d("Size is", String.valueOf(filteredList.size()));
                        movement.setVideoName(ds.getValue(Movement.class).getTitle());
                        movement.setVideoDesc(ds.getValue(Movement.class).getDescription());
                        movement.setVideoUrl(ds.getValue(Movement.class).getVideoURL());
                        movement.setCategory(ds.getValue(Movement.class).getType());
                        if (favorites.contains(ds.getValue(Movement.class).getVideoURL())) {
                            movement.setChecked(true);
                        } else {
                            movement.setChecked(false);
                        }
                        filteredList.add(movement);
                    }
                }
                Log.w("Filter list size: ", String.valueOf(filteredList.size()));
                createRecyclerView(filteredList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get Movement", "loadPost:onCancelled", databaseError.toException());
            }
        };
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("movements");
        databaseReference.addValueEventListener(userListener);
        Log.d("Size of list is", String.valueOf(filteredList.size()));
    }

    void getMovements() {
        List<String> favorites = getFavorites();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Get movement", "called");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ExerciseCard movement = new ExerciseCard();
                    Log.d("Get movement key", String.valueOf(ds.getKey()));
                    Log.d("Size is", String.valueOf(cardList.size()));
                    movement.setVideoName(ds.getValue(Movement.class).getTitle());
                    movement.setVideoDesc(ds.getValue(Movement.class).getDescription());
                    movement.setVideoUrl(ds.getValue(Movement.class).getVideoURL());
                    movement.setCategory(ds.getValue(Movement.class).getType());
                    if (favorites.contains(ds.getValue(Movement.class).getVideoURL())) {
                        movement.setChecked(true);
                    } else {
                        movement.setChecked(false);
                    }
                    cardList.add(movement);
                }
                createRecyclerView(cardList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get Movement", "loadPost:onCancelled", databaseError.toException());
            }
        };
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("movements");
        databaseReference.addValueEventListener(userListener);
        Log.d("Size of list is", String.valueOf(cardList.size()));
    }


    private void createRecyclerView(ArrayList<ExerciseCard> cardList) {

        layout = new LinearLayoutManager(ExploreScreenActivity.this);
        rView = findViewById(R.id.recyclerView);
        rView.setHasFixedSize(true);
        exerciseAdapter = new ExerciseAdapter(cardList);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                cardList.get(position).onItemClick(position);
            }

            @Override
            public void onCheckBoxClick(int position) {
                // attributions bond to the item has been changed
                cardList.get(position).onCheckBoxClick(position);
                Log.w("Clicking checkbox at: ", String.valueOf(position));
                // if it is checked, add to favorites in db
                if (cardList.get(position).getStatus()){
                    Log.w("Adding to favorites: ", "ATTEMPT");
                    addToFavorites(cardList.get(position));
                } else if (cardList.get(position).getStatus() == false){
                    Log.w("Removing from favorites", "ATTEMPT");
                    // remove from favorites in db
                    String[] url = cardList.get(position).getVideoUrl().split("v=");
                    db.child("users").child(user.getUid()).child("favorites").child(url[1]).removeValue();
                }
                exerciseAdapter.notifyItemChanged(position);
            }
        };
        exerciseAdapter.setOnClickItemClickListener(itemClickListener);
        rView.setAdapter(exerciseAdapter);
        rView.setLayoutManager(layout);
    }

    void addToFavorites(ExerciseCard card) {

        Log.d("Add to favorites", "called");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Log.w("Adding videoURL to favorites", String.valueOf(card.getVideoUrl()));
        String key = db.child("users").child(user.getUid()).child("favorites").push().getKey();
        // strip until the v=
        String[] url = card.getVideoUrl().split("v=");
        db.child("users").child(user.getUid()).child("favorites").child(url[1]).setValue(url[1]);
    }
}