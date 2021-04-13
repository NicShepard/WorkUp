package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Registration extends AppCompatActivity {

    Button testButton;
    private Button testButton2;
    EditText fName;
    EditText lName;
    EditText username;
    String uid;
    Movement m;

    DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dataService = new DataService();

        uid = getIntent().getStringExtra("CURRENT_USER");
        testButton = findViewById(R.id.testButton);
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        username = findViewById(R.id.username);

        testButton2 = findViewById(R.id.getUser);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                m = dataService.getMovement("MYAvLIIaCgURQcGK2mw");
            }
        });

        testButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Movement", m.getTitle());
            }
        });

    }
}