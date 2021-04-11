package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;

public class LandingPage extends AppCompatActivity {

    ProgressBar simpleProgressBar;
    TextView text_prog;

    private static final String TAG = "LANDINGPAGE ACTIVITY";

    // exercise database reference
    // buttons to filter
    // card clicks

    private RecyclerView rView;
    private ArrayList<ChallengeCard> cardList = new ArrayList<>();
    private ChallengeAdapter challengeAdapter;
    private RecyclerView.LayoutManager layout;
    private String currentUser;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    BottomNavigationView bottomNavigation;

    //private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        text_prog = (TextView) findViewById(R.id.text_view_progress);
        simpleProgressBar = (ProgressBar) findViewById(R.id.progressBar); // initiate the progress bar
        simpleProgressBar.setProgress(40);
        text_prog.setText("40%");
        currentUser = getIntent().getStringExtra("CURRENT_USER");
        //createRecyclerView();

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
            ChallengeCard item1 = new ChallengeCard(new Date(), "1", "");
            ChallengeCard item2 = new ChallengeCard(new Date(), "2", "");
            ChallengeCard item3 = new ChallengeCard(new Date(), "3", "");
            ChallengeCard item4 = new ChallengeCard(new Date(), "4", "");
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
        //        Toast.makeText(LinkCollector.this, "Add an item", Toast.LENGTH_SHORT).show();

        challengeAdapter.notifyItemInserted(position);
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