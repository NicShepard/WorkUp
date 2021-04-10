package edu.neu.madcourseworkupteam.workup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.neu.madcourseworkupteam.workup.fragments.ExploreFragment;

public class ExploreScreenActivity extends AppCompatActivity {

    private static final String TAG = "ExploreScreen ACTIVITY";

    // exercise database reference
    // buttons to filter
    // card clicks

    private RecyclerView rView;
    private ArrayList<ExerciseCard> cardList = new ArrayList<>();
    private ExerciseAdapter exerciseAdapter;
    private RecyclerView.LayoutManager layout;
    private String currentUser;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    private static final String ACTIVE_FRAGMENT = "ACTIVE_FRAGMENT";
    BottomNavigationView bottomNavigation;

    //private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_screen);
        //database = FirebaseDatabase.getInstance().getReference();
        currentUser = getIntent().getStringExtra("CURRENT_USER");
        createRecyclerView();

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
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (cardList == null || cardList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
//                List<String> emojis = getEmojisForUser(database, currentUser);
//                Log.d("emojis size:", String.valueOf(emojis.size()));
//
//                for(String each : emojis) {
//                    if (each == "smiley") {
//                        cardList.add(new StickerCard(R.drawable.smiley_face));
//                    } else if (each == "laughing") {
//                        cardList.add(new StickerCard(R.drawable.laughing_face));
//                    } else if (each == "angry") {
//                        cardList.add(new StickerCard(R.drawable.angry_face));
//                    } else if (each == "sad") {
//                        cardList.add(new StickerCard(R.drawable.sad_face));
//                    }
//                }
//                size = emojis.size();
                for (int i = 0; i < size; i++) {
                    Integer image = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
                    ExerciseCard sCard = new ExerciseCard(1);
                    cardList.add(sCard);
                }
            }
        }
        // Load the initial cards
        else {
            /*List<String> emojis = getEmojisForUser(database, currentUser);
            Log.d("emojis size:", String.valueOf(emojis.size()));

            for(String each : emojis) {
                if (each == "smiley") {
                    cardList.add(new StickerCard(R.drawable.smiley_face));
                } else if (each == "laughing") {
                    cardList.add(new StickerCard(R.drawable.laughing_face));
                } else if (each == "angry") {
                    cardList.add(new StickerCard(R.drawable.angry_face));
                } else if (each == "sad") {
                    cardList.add(new StickerCard(R.drawable.sad_face));
                }
            }
            */
            ExerciseCard item1 = new ExerciseCard(R.drawable.neutral_face);
            ExerciseCard item2 = new ExerciseCard(R.drawable.neutral_face);
            ExerciseCard item3 = new ExerciseCard(R.drawable.neutral_face);
            ExerciseCard item4 = new ExerciseCard(R.drawable.neutral_face);
            //StickerCard item3 = new StickerCard(R.drawable.common_google_signin_btn_icon_light));
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

        exerciseAdapter = new ExerciseAdapter(cardList);
        rView.setAdapter(exerciseAdapter);
        rView.setLayoutManager(layout);

    }
    private int addItem(int position) {

        cardList.add(position, new ExerciseCard(R.drawable.common_google_signin_btn_icon_light));
        //        Toast.makeText(LinkCollector.this, "Add an item", Toast.LENGTH_SHORT).show();

        exerciseAdapter.notifyItemInserted(position);
        return 1;
    }

    /**
     * Get the emojis for a user
     */
    /*
    public List<String> getEmojisForUser(DatabaseReference database, String user) {

        List emojiList = new LinkedList();

        database.child("users").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.e("SNAPSHOT IS", snapshot.getKey());
                    if (snapshot.getKey().equalsIgnoreCase("received")) {
                        for (DataSnapshot receivedMessageUser : snapshot.getChildren()) {
                            if (snapshot.getKey() != null) {
                                for (DataSnapshot message : receivedMessageUser.getChildren()) {
                                    Log.e("ADDING", message.getValue().toString());
                                    emojiList.add(message.getValue().toString());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error while reading data");
            }
        });

        return emojiList;
    }*/

}