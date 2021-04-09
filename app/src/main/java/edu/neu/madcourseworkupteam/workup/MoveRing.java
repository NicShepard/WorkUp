package edu.neu.madcourseworkupteam.workup;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MoveRing extends AppCompatActivity implements SensorEventListener, StepListener {

    private SensorManager sensorManager;
    private boolean running;
    private int totalSteps;
    private int previousSteps;
    private Sensor accel;
    private TextView stepCount;
    private Sensor stepSensor;
    private static final String NUM_STEPS = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_ring);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepCount = findViewById(R.id.steps);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected", Toast.LENGTH_SHORT).show();
        } else{
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;

        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
//        if(running) {
//            totalSteps = event.values[0] }

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                stepSensor.updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void step(long timeNs) {

    }
}