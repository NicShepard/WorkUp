package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity {

    Button testButton;
    EditText fName;
    EditText lName;
    EditText username;
    String uid;

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

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UID", uid);
                User userToRegister = new User();
                userToRegister.setFirstName(fName.getText().toString());
                userToRegister.setLastName(lName.getText().toString());
                userToRegister.setUsername(username.getText().toString());
                dataService.registerUser(userToRegister, uid);
            }
        });

    }
}