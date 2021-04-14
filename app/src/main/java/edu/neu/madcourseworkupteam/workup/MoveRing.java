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
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MoveRing extends AppCompatActivity implements SensorEventListener, StepListener {
    private SensorManager sensorManager;
    private boolean running;
    private int totalSteps;
    private int previousSteps;
    private TextView stepsTxtView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_ring);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        running = false;
        totalSteps = 0;
        previousSteps = 0;
        stepsTxtView = findViewById(R.id.steps);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }


//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        stepCount = findViewById(R.id.steps);
//        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//
//        if (stepSensor == null) {
//            Toast.makeText(this, "No sensor detected", Toast.LENGTH_SHORT).show();
//        } else{
//            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
//        }
//
//    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;

        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(stepSensor == null) {
            Toast.makeText(this, "No sensor detected", Toast.LENGTH_SHORT).show();
        } else{
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override public void onPause() {
        super.onPause();


//        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
//            sensorManager.unregisterListener(this, stepSensor);
//        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if(running) {
            totalSteps = (int) event.values[0];
            stepsTxtView.setText(String.valueOf(totalSteps));
        }


//        if(running) {
//            totalSteps = event.values[0] }

//            if (event.sensor == stepSensor) {
//                totalSteps = (int)event.values[0];
//                stepCount.setText(String.valueOf(totalSteps) );


//                totalSteps = (int)event.values[0];

                //updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);

        }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void step(long timeNs) {

    }

    //TODO reset steps at the end of day?
    private void resetSteps() {

    }
}