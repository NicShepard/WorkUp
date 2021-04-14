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
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_screen);
        //database = FirebaseDatabase.getInstance().getReference();
        currentUser = getIntent().getStringExtra("CURRENT_USER");

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
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
        outState.putInt(NUMBER_OF_ITEMS, size);

        // Need to generate unique key for each item
        // TODO: This is only a possible way to do, please find own way to generate the key
        for (int i = 0; i < size; i++) {
            // put image information id into instance
            outState.putInt(KEY_OF_INSTANCE + i + "0", cardList.get(i).getImageSource());
            // put itemName information into instance
            outState.putString(KEY_OF_INSTANCE + i + "1", cardList.get(i).getName());
            // put itemDesc information into instance
            outState.putString(KEY_OF_INSTANCE + i + "2", cardList.get(i).getDesc());
            // put isChecked information into instance
            outState.putBoolean(KEY_OF_INSTANCE + i + "3", cardList.get(i).getStatus());
        }
        super.onSaveInstanceState(outState);
    }

    private void init(Bundle savedInstanceState) {
        initialItemData(savedInstanceState);
        createRecyclerView();
    }

    private void initialItemData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (cardList == null || cardList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
                // Retrieve keys we store in the instance
                for (int i = 0; i < size; i++) {
                    Integer imgId = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
                    String itemName = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    String itemDesc = savedInstanceState.getString(KEY_OF_INSTANCE + i + "2");
                    boolean isChecked = savedInstanceState.getBoolean(KEY_OF_INSTANCE + i + "3");

                    // Need to make sure names such as "XXX(checked)" will not duplicate
                    // Use a tricky way to solve this problem, but not the best though
                    if (isChecked) {
                        itemName = itemName.substring(0, itemName.lastIndexOf("("));
                    }
                    ExerciseCard itemCard = new ExerciseCard(imgId, itemName, itemDesc, isChecked);
                    cardList.add(itemCard);
                }
            }
        }
        // Load the initial cards
        else {
            ExerciseCard item1 = new ExerciseCard(R.drawable.neutral_face, "videoName1", "Video Desc", false);
            ExerciseCard item2 = new ExerciseCard(R.drawable.neutral_face, "videoName2", "Video Desc", false);
            ExerciseCard item3 = new ExerciseCard(R.drawable.neutral_face, "videoName3", "Video Desc", false);
            ExerciseCard item4 = new ExerciseCard(R.drawable.neutral_face, "videoName4", "Video Desc", false);
            cardList.add(item1);
            cardList.add(item2);
            cardList.add(item3);
            cardList.add(item4);
        }
    }

    private void createRecyclerView() {

        layout = new LinearLayoutManager(ExploreScreenActivity.this);
        rView = findViewById(R.id.recyclerView);
        rView.setHasFixedSize(true);
        exerciseAdapter = new ExerciseAdapter(cardList);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                cardList.get(position).onItemClick(position);
            }
            @Override
            public void onCheckBoxClick(int position) {
                // attributions bond to the item has been changed
                cardList.get(position).onCheckBoxClick(position);
                exerciseAdapter.notifyItemChanged(position);
            }
        };
        exerciseAdapter.setOnClickItemClickListener(itemClickListener);
        rView.setAdapter(exerciseAdapter);
        rView.setLayoutManager(layout);

    }

}