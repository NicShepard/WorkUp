package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Registration extends AppCompatActivity implements SensorEventListener {

    Button testButton;
    private Button testButton2;
    EditText fName;
    EditText lName;
    EditText username;
    String uid;
    Movement m;

    SensorManager sensorManager;
    Sensor stepCounter;

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

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            fName.setText("Step counter detected");
        } else {
            fName.setText("No Step counter detected");

        }


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


    @Override
    public void onResume() {
        super.onResume();

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, stepCounter);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}