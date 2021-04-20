package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewChallengeActivity extends AppCompatActivity implements MultiSelectSpinner.OnMultipleItemsSelectedListener, View.OnClickListener {

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ArrayList<User> friendList = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private String currentUsername;
    private final String defaultString = "default";
    private Spinner spinner;
    private MultiSelectSpinner multiSelectSpinner;
    private String course1;
    private String course2;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_challenge);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        init(savedInstanceState);
    }

    public void addOnSpinnerFriends() {
        multiSelectSpinner = (MultiSelectSpinner) findViewById(R.id.friends);
        ArrayList<String> list = new ArrayList<>();
        list.add("Select friends to challenge");

        for (User u : friendList) {
            list.add(u.getFirstName() + u.getLastName() + u.getUsername());
        }

        String[] friends = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            friends[i] = list.get(i);
        }

        multiSelectSpinner.setItems(friends);
        multiSelectSpinner.setSelection(0);
        multiSelectSpinner.setListener(this);


    }

    private void init(Bundle savedInstanceState) {
        initialItemData(savedInstanceState);
    }

    private void initialItemData(Bundle savedInstanceState) {
        getFriends();
    }

    void getFriends() {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Get friends", "called");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User friend = new User();
                    Log.d("Get friend key", String.valueOf(ds.getKey()));

                    friend.setFirstName(ds.getValue(User.class).getFirstName()) ;
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

    }

    @Override
    public void onClick(View v) {

    }
}