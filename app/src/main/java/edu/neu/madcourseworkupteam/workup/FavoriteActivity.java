package edu.neu.madcourseworkupteam.workup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * An activity for storing content the user wants to revisit at a later date.
 */
public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView rView;
    private ArrayList<ExerciseCard> cardList = new ArrayList<>();
    private ExerciseAdapter exerciseAdapter;
    private RecyclerView.LayoutManager layout;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getMovements();
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

                    if (favorites.contains(ds.getValue(Movement.class).getVideoURL())) {
                        movement.setVideoName(ds.getValue(Movement.class).getTitle());
                        movement.setVideoDesc(ds.getValue(Movement.class).getDescription());
                        movement.setVideoUrl(ds.getValue(Movement.class).getVideoURL());
                        movement.setChecked(true);

                        cardList.add(movement);
                    }
                }
                createRecyclerView();
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

    private void createRecyclerView() {

        layout = new LinearLayoutManager(FavoriteActivity.this);
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
                if (cardList.get(position).getStatus() == false){
                    Log.w("Removing from favorites", "ATTEMPT");
                    // remove from favorites in db
                    String url = cardList.get(position).getVideoUrl();
                    db.child("users").child(user.getUid()).child("favorites").child(url).removeValue();
                    cardList.remove(position);
                }
                exerciseAdapter.notifyItemChanged(position);
            }
        };
        exerciseAdapter.setOnClickItemClickListener(itemClickListener);
        rView.setAdapter(exerciseAdapter);
        rView.setLayoutManager(layout);

    }
}